package tictactoeai;
import java.io.*;
import java.util.Scanner;
class Functions {
    boolean quickLearn;
    int inputNodes = 9;
    int hidden1Nodes = 27;
    int hidden2Nodes = 27;
    int hidden3Nodes = 27;
    int outputNodes = 1;
    Scanner sc = new Scanner(System.in);
    private final int[][] boardValues = {{0,0,0},{0,0,0},{0,0,0}};
    private final double[][] inputsArray = new double[1][inputNodes];
    private final int player1mark = 1;
    private final int player2mark = -1;
    private int win = 2;
    private int oTurnAICounter = 0;
    private int xTurnAICounter = 0;
    boolean game = true;
    double costSum;
    int xWinCounter = 0;
    int oWinCounter = 0;
    ANN oAnn = new ANN(inputNodes, hidden1Nodes, hidden2Nodes, hidden3Nodes, outputNodes);
    Storage oStorage = new Storage();
    private double[][] oBoard1 = new double[1][inputNodes];
    private double[][] oBoard2 = new double[1][inputNodes];
    private double[][] oBoard3 = new double[1][inputNodes];
    private double[][] oBoard4 = new double[1][inputNodes];
    ANN xAnn = new ANN(inputNodes, hidden1Nodes, hidden2Nodes, hidden3Nodes, outputNodes);
    Storage xStorage = new Storage();
    private double[][] xBoard1 = new double[1][inputNodes];
    private double[][] xBoard2 = new double[1][inputNodes];
    private double[][] xBoard3 = new double[1][inputNodes];
    private double[][] xBoard4 = new double[1][inputNodes];
    void clear(){
        if(!quickLearn){
//            System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
            System.out.println("");
        }
    }
    void resetGame(){
        Matrix.resetInt(boardValues);
        Matrix.resetDouble(oBoard1);
        Matrix.resetDouble(oBoard2);
        Matrix.resetDouble(oBoard3);
        Matrix.resetDouble(oBoard4);
        Matrix.resetDouble(xBoard1);
        Matrix.resetDouble(xBoard2);
        Matrix.resetDouble(xBoard3);
        Matrix.resetDouble(xBoard4);
        game = true;
        win = 2;
        oTurnAICounter = 0;
        xTurnAICounter = 0;
    }
    void resetScores(){
        xWinCounter = 0;
        oWinCounter = 0;
        costSum = 0;
    }
    void resetSettings(){
        oAnn.weightsIH = Matrix.randomize(xAnn.weightsIH,2,-1);
        oAnn.weightsHO = Matrix.randomize(xAnn.weightsHO,2,-1);
        oAnn.biasesIH = Matrix.randomize(xAnn.biasesIH,2,-1);
        oAnn.biasesHO = Matrix.randomize(xAnn.biasesHO,2,-1);
        xAnn.weightsIH = Matrix.randomize(xAnn.weightsIH,2,-1);
        xAnn.weightsHO = Matrix.randomize(xAnn.weightsHO,2,-1);
        xAnn.biasesIH = Matrix.randomize(xAnn.biasesIH,2,-1);
        xAnn.biasesHO = Matrix.randomize(xAnn.biasesHO,2,-1);
    }
    void printSettings(){
        Matrix.print(xAnn.weightsIH,"XweightsIH");
        Matrix.print(xAnn.biasesIH,"XbiasesIH");
        if(xAnn.numHiddens >= 2){
            Matrix.print(xAnn.weightsH1H2,"XweightsH1H2");
            Matrix.print(xAnn.biasesH1H2,"XbiasesH1H2");
        }
        if(xAnn.numHiddens >= 3){
            Matrix.print(xAnn.weightsH2H3,"XweightsH2H3");
            Matrix.print(xAnn.biasesH2H3,"XbiasesH2H3");
        }
        Matrix.print(xAnn.weightsHO,"XweightsHO");
        Matrix.print(xAnn.biasesHO,"XbiasesHO");
        Matrix.print(oAnn.weightsIH,"OweightsIH");
        Matrix.print(oAnn.biasesIH,"ObiasesIH");
        if(oAnn.numHiddens >= 2){
            Matrix.print(oAnn.weightsH1H2,"OweightsH1H2");
            Matrix.print(oAnn.biasesH1H2,"ObiasesH1H2");
        }
        if(oAnn.numHiddens >= 3){
            Matrix.print(oAnn.weightsH2H3,"OweightsH2H3");
            Matrix.print(oAnn.biasesH2H3,"ObiasesH2H3");
        }
        Matrix.print(oAnn.weightsHO,"OweightsHO");
        Matrix.print(oAnn.biasesHO,"ObiasesHO");
    }
    private void boardToRow(){
        int k = 0;
        int rows = boardValues.length;
        int columns = boardValues[0].length;
        for(int i = 0; i < rows; i++)
            for(int j = 0; j < columns; j++){
                inputsArray[0][k] = (double)boardValues[i][j];
                k++;
            }
    }
    private void board(){
        if(!quickLearn){
            clear();
            System.out.println("  a   b   c");
            for(int i = 0; i < 3; i++){
                for(int j = 0; j < 3; j++){
                    System.out.print("|");
                    if(boardValues[i][j] == 0)
                        System.out.print("   ");
                    else if(boardValues[i][j] == 1)
                        System.out.print(" X ");
                    else if(boardValues[i][j] == -1)
                        System.out.print(" O ");
                    else
                        System.out.print(" ? ");
                }
                if(i == 0)
                    System.out.println("| 1\n-------------");
                if(i == 1)
                    System.out.println("| 2\n-------------");
                if(i == 2)
                    System.out.println("| 3");
            }
        }
    }
    void turnPlayers(String player1, String player2, int player1or2){
        int x;
        int y;
        board();
        if(player1or2 == 1)
            System.out.println(player1 + ":");
        if(player1or2 == 2)
            System.out.println(player2 + ":");
        while(true){
            System.out.println("Where do you want to mark?");
            String slot = sc.next();
            char[] slotCoord = slot.toLowerCase().toCharArray();
            if(slotCoord.length != 2){
                System.out.println("Location not found, try again...");
                continue;
            }
            if(slotCoord[0] == 'a')
                x = 0;
            else if(slotCoord[0] == 'b')
                x = 1;
            else if(slotCoord[0] == 'c')
                x = 2;
            else{
                System.out.println("Location not found, try again...");
                continue;
            }
            if(slotCoord[1] == '1')
                y = 0;
            else if(slotCoord[1] == '2')
                y = 1;
            else if(slotCoord[1] == '3')
                y = 2;
            else{
                System.out.println("Location not found, try again...");
                continue;
            }
            if(boardValues[y][x] != 0){
                System.out.println("Location is not empty...");
                continue;
            }
            else{
                if(player1or2 == 1)
                    boardValues[y][x] = player1mark;
                else if(player1or2 == 2)
                    boardValues[y][x] = player2mark;
            }
            break;
        }
    }
    private void win(){
        if( (boardValues[0][0] == 1 && boardValues[0][1] == 1 && boardValues[0][2] == 1) || 
            (boardValues[1][0] == 1 && boardValues[1][1] == 1 && boardValues[1][2] == 1) || 
            (boardValues[2][0] == 1 && boardValues[2][1] == 1 && boardValues[2][2] == 1) || 
            (boardValues[0][0] == 1 && boardValues[1][0] == 1 && boardValues[2][0] == 1) || 
            (boardValues[0][1] == 1 && boardValues[1][1] == 1 && boardValues[2][1] == 1) || 
            (boardValues[0][2] == 1 && boardValues[1][2] == 1 && boardValues[2][2] == 1) || 
            (boardValues[0][0] == 1 && boardValues[1][1] == 1 && boardValues[2][2] == 1) || 
            (boardValues[2][0] == 1 && boardValues[1][1] == 1 && boardValues[0][2] == 1) ){
            win = 1;
            xWinCounter++;
        }
        else if((boardValues[0][0] == -1 && boardValues[0][1] == -1 && boardValues[0][2] == -1) || 
                (boardValues[1][0] == -1 && boardValues[1][1] == -1 && boardValues[1][2] == -1) || 
                (boardValues[2][0] == -1 && boardValues[2][1] == -1 && boardValues[2][2] == -1) || 
                (boardValues[0][0] == -1 && boardValues[1][0] == -1 && boardValues[2][0] == -1) || 
                (boardValues[0][1] == -1 && boardValues[1][1] == -1 && boardValues[2][1] == -1) || 
                (boardValues[0][2] == -1 && boardValues[1][2] == -1 && boardValues[2][2] == -1) || 
                (boardValues[0][0] == -1 && boardValues[1][1] == -1 && boardValues[2][2] == -1) || 
                (boardValues[2][0] == -1 && boardValues[1][1] == -1 && boardValues[0][2] == -1) ){
            win = -1;
            oWinCounter++;
        }
        else if((boardValues[0][0] != 0 && boardValues[0][1] != 0 && boardValues[0][2] != 0) && 
                (boardValues[1][0] != 0 && boardValues[1][1] != 0 && boardValues[1][2] != 0) && 
                (boardValues[2][0] != 0 && boardValues[2][1] != 0 && boardValues[2][2] != 0) && 
                (boardValues[0][0] != 0 && boardValues[1][0] != 0 && boardValues[2][0] != 0) && 
                (boardValues[0][1] != 0 && boardValues[1][1] != 0 && boardValues[2][1] != 0) && 
                (boardValues[0][2] != 0 && boardValues[1][2] != 0 && boardValues[2][2] != 0) && 
                (boardValues[0][0] != 0 && boardValues[1][1] != 0 && boardValues[2][2] != 0) && 
                (boardValues[2][0] != 0 && boardValues[1][1] != 0 && boardValues[0][2] != 0) ){
            win = 0;
        }
    }
    void winCheck(String player1, String player2){
        win();
            if(win == 1){
                board();
                if(!quickLearn)
                    System.out.println(player1 + " Wins, " + player2 + " Loses!");
                game = false;
            }
            else if(win == -1){
                board();
                if(!quickLearn)
                    System.out.println(player2 + " Wins, " + player1 + " Loses!");
                game = false;
            }
            else if(win == 0){
                board();
                if(!quickLearn)
                    System.out.println("Tie between " + player1 + " and " + player2 + "!");
                game = false;
            }
    }
    double costPerGame(){
        boardToRow();
        if(win == 1){
            double[][] target = {{-1}};
            return ( oAnn.costTotal(oBoard1, target) + oAnn.costTotal(oBoard2, target) + oAnn.costTotal(oBoard3, target) + oAnn.costTotal(oBoard4, target) )/ oTurnAICounter;
        }
        else if(win == 0){
            double[][] target = {{0}};
            return ( oAnn.costTotal(oBoard1, target) + oAnn.costTotal(oBoard2, target) + oAnn.costTotal(oBoard3, target) + oAnn.costTotal(oBoard4, target) )/ oTurnAICounter;        }
        else if(win == -1){
            double[][] target = {{1}};
            return ( oAnn.costTotal(oBoard1, target) + oAnn.costTotal(oBoard2, target) + oAnn.costTotal(oBoard3, target) + oAnn.costTotal(oBoard4, target) )/ oTurnAICounter;
        }
        return 0;
    }
    
    void xTurnRandom(){
        board();
        boardToRow();
        while(true){
            int random = (int)(9 * Math.random());
            if(inputsArray[0][random] == 0){
                if(random < 3){
                    boardValues[0][random] = 1;
                    break;
                }
                else if(random < 6){
                    boardValues[1][random - 3] = 1;
                    break;
                }
                else if(random < 9){
                    boardValues[2][random - 6] = 1;
                    break;
                }
            }
        }
        xTurnAICounter++;
        boardToRow();
        if(xTurnAICounter == 1)
            xBoard1 = inputsArray;
        else if(xTurnAICounter == 2)
            xBoard2 = inputsArray;
        else if(xTurnAICounter == 3)
            xBoard3 = inputsArray;
        else if(xTurnAICounter == 4)
            xBoard4 = inputsArray;
    }
    void oTurnRandom(){
        board();
        boardToRow();
        while(true){
            int random = (int)(9 * Math.random());
            if(inputsArray[0][random] == 0){
                if(random < 3){
                    boardValues[0][random] = -1;
                    break;
                }
                else if(random < 6){
                    boardValues[1][random - 3] = -1;
                    break;
                }
                else if(random < 9){
                    boardValues[2][random - 6] = -1;
                    break;
                }
            }
        }
        oTurnAICounter++;
        boardToRow();
        if(oTurnAICounter == 1)
            oBoard1 = inputsArray;
        else if(oTurnAICounter == 2)
            oBoard2 = inputsArray;
        else if(oTurnAICounter == 3)
            oBoard3 = inputsArray;
        else if(oTurnAICounter == 4)
            oBoard4 = inputsArray;
    }
    void oTurnAI(){
        board();
        double max = -2;
        int spot = 0;
        int columns = inputsArray[0].length;
        for(int i = 0; i < columns; i++){
            boardToRow();
            if(inputsArray[0][i] == 0){
                inputsArray[0][i] = -1;
                double[][] guess = oAnn.feedforward(inputsArray);
//                double[][] random = new double[guess.length][guess[0].length];
//                random = Matrix.randomize(random, .002, -.001);
//                guess = Matrix.add(guess, random);
                if(!quickLearn)
                    Matrix.print(guess,"O guess");
                if(guess[0][0] > max){
                    max = guess[0][0];
                    spot = i;
                }
            }
        }
        if(spot < 3)//first row (0,1,2)
            boardValues[0][spot] = -1;
        else if(spot < 6)//second row (3,4,5)
            boardValues[1][spot-3] = -1;
        else if(spot < 9)//third row (6,7,8)
            boardValues[2][spot-6] = -1;
        oTurnAICounter++;
        boardToRow();
        if(oTurnAICounter == 1)
            oBoard1 = inputsArray;
        else if(oTurnAICounter == 2)
            oBoard2 = inputsArray;
        else if(oTurnAICounter == 3)
            oBoard3 = inputsArray;
        else if(oTurnAICounter == 4)
            oBoard4 = inputsArray;
    }
    void fixZeroes(){
        for(int i = 0; i < inputNodes; i++){
            if(oBoard1[0][i] == 0)
                oBoard1[0][i] = .001;
            if(oBoard2[0][i] == 0)
                oBoard2[0][i] = .001;
            if(oBoard3[0][i] == 0)
                oBoard3[0][i] = .001;
            if(oBoard4[0][i] == 0)
                oBoard4[0][i] = .001;
            if(xBoard1[0][i] == 0)
                xBoard1[0][i] = .001;
            if(xBoard2[0][i] == 0)
                xBoard2[0][i] = .001;
            if(xBoard3[0][i] == 0)
                xBoard3[0][i] = .001;
            if(xBoard4[0][i] == 0)
                xBoard4[0][i] = .001;
        }
    }
    void oTrainAI(){
        fixZeroes();
        if(win == 1){
            double randomness = 1;
            double[][] target = {{-1}};
            oAnn.backpropagation(oBoard1, target, randomness);
            oAnn.backpropagation(oBoard2, target, randomness);
            if(oTurnAICounter > 2)
                oAnn.backpropagation(oBoard3, target, randomness);
            if(oTurnAICounter > 3)
                oAnn.backpropagation(oBoard4, target, randomness);
        }
        else if(win == 0){
            double randomness = 1;
            double[][] target = {{0}};
            oAnn.backpropagation(oBoard1, target, randomness);
            oAnn.backpropagation(oBoard2, target, randomness);
            if(oTurnAICounter > 2)
                oAnn.backpropagation(oBoard3, target, randomness);
            if(oTurnAICounter > 3)
                oAnn.backpropagation(oBoard4, target, randomness);
        }
        else if(win == -1){
            double randomness = 1;
            double[][] target = {{1}};
            oAnn.backpropagation(oBoard1, target, randomness);
            oAnn.backpropagation(oBoard2, target, randomness);
            if(oTurnAICounter > 2)
                oAnn.backpropagation(oBoard3, target, randomness);
            if(oTurnAICounter > 3)
                oAnn.backpropagation(oBoard4, target, randomness);
        }
    }
    void oLoad(){
        try{
            try(FileInputStream fileIn = new FileInputStream(System.getProperty("user.dir") + "/neuralsettingsO.ser");//find file in current directory
                ObjectInputStream in = new ObjectInputStream(fileIn)){//read file for object
                oStorage = (Storage)in.readObject();//import object
            }
        }
        catch (IOException e) {
            e.printStackTrace();
            return;
        }
        catch (ClassNotFoundException e) {
            System.out.println("Missing Class");
                   e.printStackTrace();
            return;
        }
        oAnn.weightsIH = oStorage.savedWeightsIH;//set values
        oAnn.weightsH1H2 = oStorage.savedWeightsH1H2;
        oAnn.weightsH2H3 = oStorage.savedWeightsH2H3;
        oAnn.weightsHO = oStorage.savedWeightsHO;
        oAnn.biasesIH = oStorage.savedBiasesIH;
        oAnn.biasesH1H2 = oStorage.savedBiasesH1H2;
        oAnn.biasesH2H3 = oStorage.savedBiasesH2H3;
        oAnn.biasesHO = oStorage.savedBiasesHO;
    }
    void oSave(){
        oStorage.savedWeightsIH = oAnn.weightsIH;//store values
        oStorage.savedWeightsH1H2 = oAnn.weightsH1H2;
        oStorage.savedWeightsH2H3 = oAnn.weightsH2H3;
        oStorage.savedWeightsHO = oAnn.weightsHO;
        oStorage.savedBiasesIH = oAnn.biasesIH;
        oStorage.savedBiasesH1H2 = oAnn.biasesH1H2;
        oStorage.savedBiasesH2H3 = oAnn.biasesH2H3;
        oStorage.savedBiasesHO = oAnn.biasesHO;
        try{
            try (FileOutputStream fileOut = new FileOutputStream(System.getProperty("user.dir") + "/neuralsettingsO.ser"); 
                ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
                out.writeObject(oStorage);
            }
//           System.out.println("Stored neural network settings in /neuralsettingsO.ser\n");
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }
    void xLoad(){
        try{
            try(FileInputStream fileIn = new FileInputStream(System.getProperty("user.dir") + "/neuralsettingsX.ser");//find file in current directory
                ObjectInputStream in = new ObjectInputStream(fileIn)){//read file for object
                xStorage = (Storage)in.readObject();//import object
            }
        }
        catch (IOException e) {
            e.printStackTrace();
            return;
        }
        catch (ClassNotFoundException e) {
            System.out.println("Missing Class");
                   e.printStackTrace();
            return;
        }
        xAnn.weightsIH = xStorage.savedWeightsIH;//set values
        xAnn.weightsH1H2 = xStorage.savedWeightsH1H2;
        xAnn.weightsH2H3 = xStorage.savedWeightsH2H3;
        xAnn.weightsHO = xStorage.savedWeightsHO;
        xAnn.biasesIH = xStorage.savedBiasesIH;
        xAnn.biasesH1H2 = xStorage.savedBiasesH1H2;
        xAnn.biasesH2H3 = xStorage.savedBiasesH2H3;
        xAnn.biasesHO = xStorage.savedBiasesHO;
    }
    void xSave(){
        xStorage.savedWeightsIH = xAnn.weightsIH;//store values
        xStorage.savedWeightsH1H2 = xAnn.weightsH1H2;
        xStorage.savedWeightsH2H3 = xAnn.weightsH2H3;
        xStorage.savedWeightsHO = xAnn.weightsHO;
        xStorage.savedBiasesIH = xAnn.biasesIH;
        xStorage.savedBiasesH1H2 = xAnn.biasesH1H2;
        xStorage.savedBiasesH2H3 = xAnn.biasesH2H3;
        xStorage.savedBiasesHO = xAnn.biasesHO;
        try{
            try (FileOutputStream fileOut = new FileOutputStream(System.getProperty("user.dir") + "/neuralsettingsX.ser"); 
                ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
                out.writeObject(xStorage);
            }
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }
    void xTrainAI(){
        fixZeroes();
        if(win == 1){
            double randomness = 1;
            double[][] target = {{1}};
            xAnn.backpropagation(xBoard1, target, randomness);
            xAnn.backpropagation(xBoard2, target, randomness);
            if(xTurnAICounter > 2)
                xAnn.backpropagation(xBoard3, target, randomness);
            if(xTurnAICounter > 3)
                xAnn.backpropagation(xBoard4, target, randomness);
        }
        else if(win == 0){
            double randomness = 1;
            double[][] target = {{-1}};
            xAnn.backpropagation(xBoard1, target, randomness);
            xAnn.backpropagation(xBoard2, target, randomness);
            if(xTurnAICounter > 2)
                xAnn.backpropagation(xBoard3, target, randomness);
            if(xTurnAICounter > 3)
                xAnn.backpropagation(xBoard4, target, randomness);
        }
        else if(win == -1){
            double randomness = 1;
            double[][] target = {{-1}};
            xAnn.backpropagation(xBoard1, target, randomness);
            xAnn.backpropagation(xBoard2, target, randomness);
            if(oTurnAICounter > 2)
                xAnn.backpropagation(xBoard3, target, randomness);
            if(oTurnAICounter > 3)
                xAnn.backpropagation(xBoard4, target, randomness);
        }
    }
    void XturnAI(){
        board();
        double max = -2;
        int spot = 0;
        int columns = inputsArray[0].length;
        for(int i = 0; i < columns; i++){
            boardToRow();
            if(inputsArray[0][i] == 0){
                inputsArray[0][i] = 1;
                double[][] guess = xAnn.feedforward(inputsArray);
                if(!quickLearn)
                    Matrix.print(guess, "X guess");
                if(guess[0][0] > max){
                    max = guess[0][0];
                    spot = i;
                }
            }
        }
        if(spot < 3)//first row (0,1,2)
            boardValues[0][spot] = 1;
        else if(spot < 6)//second row (3,4,5)
            boardValues[1][spot-3] = 1;
        else if(spot < 9)//third row (6,7,8)
            boardValues[2][spot-6] = 1;
        xTurnAICounter++;
        boardToRow();
        if(oTurnAICounter == 1)
            xBoard1 = inputsArray;
        else if(oTurnAICounter == 2)
            xBoard2 = inputsArray;
        else if(oTurnAICounter == 3)
            xBoard3 = inputsArray;
        else if(oTurnAICounter == 4)
            xBoard4 = inputsArray;
    }
    void mutateWeights(){
        oAnn.mutate();
        xAnn.mutate();
    }
}