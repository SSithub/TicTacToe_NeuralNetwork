package tictactoeai;
import java.util.*;
import java.io.*;
public class Game {
    boolean quickLearn = false;
    NNest.NN nn = new NNest().new NN(.0001,"leakyrelu","softmax","log",18,12000,1);
    Scanner sc = new Scanner(System.in);
    private double[][] board = {{0,0,0},
                                {0,0,0},
                                {0,0,0}};
    int game;//0 = ongoing; 1 = x wins; 2 = o wins; 3 = tie;
    int winsX;
    int winsO;
    int ties;
    int spot;
    int x;
    int y;
    double max;
    double output;
    double[][] inputs;
    ArrayList<double[][]> states = new ArrayList<>();
    ArrayList<double[][]> actions = new ArrayList<>();
    public void save(){
        try{
            FileOutputStream fileOut = new FileOutputStream(System.getProperty("user.dir") + "/neuralnetwork.ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(nn.network);
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }
    public void load(){
        try{
            FileInputStream fileIn = new FileInputStream(System.getProperty("user.dir") + "/neuralnetwork.ser");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            nn.network = (ArrayList)in.readObject();
        }
        catch (IOException | ClassNotFoundException e) {
        }
    }
    public void resetGame(){
        game = 0;
        for(int i = 0; i < board[0].length; i++){
            board[0][i] = 0;
        }
        states.clear();
        actions.clear();
    }
    public void resetScores(){
        winsX = 0;
        winsO = 0;
        ties = 0;
    }
    private void printBoard(){
        if(!quickLearn){
            for(int i = 0; i < 3; i++){
                for(int j = 0; j < 3; j++){
                    System.out.print("|");
                    if(board[i][j] == 0)
                        System.out.print("   ");
                    else if(board[i][j] == 1)
                        System.out.print(" X ");
                    else if(board[i][j] == 2)
                        System.out.print(" O ");
                }
                if(i == 0 || i == 1)
                    System.out.println("|\n-------------");
                if(i == 2)
                    System.out.println("|");
            }
            System.out.println("");
        }
    }
    public void printTrainStats(){
        System.out.println("\nX wins: " + winsX
                + "\nO wins: " + winsO
                + "\nTies: " + ties
                + "\nCost: " + nn.cost);
    }
    public void playerTurn(int mark){
        printBoard();
        while(true){
            System.out.println("Player " + mark + ": Where would you like to go?");
            try{
                spot = Integer.parseInt(sc.nextLine());
                if(spot < 1 || spot > 9){
                    System.out.println("Location not found...");
                    continue;
                }
            }
            catch(NumberFormatException e){
                System.out.println("Location not found...");
                continue;
            }
            if(spot >= 7){
                y = 0;
                x = spot-7;
            }
            else if(spot >= 4){
                y = 1;
                x = spot-4;
            }
            else if(spot >= 1){//Not necessary but more intuitive
                y = 2;
                x = spot-1;
            }
            if(board[y][x] != 0){
                System.out.println("Location is occupied...");
            }
            else{
                board[y][x] = mark;
                break;
            }
        }
        win(mark);
    }
    public void win(int mark){
        if((board[0][0] == mark && board[0][1] == mark && board[0][2] == mark) ||
            (board[1][0] == mark && board[1][1] == mark && board[1][2] == mark) ||
            (board[2][0] == mark && board[2][1] == mark && board[2][2] == mark) ||
            (board[0][0] == mark && board[1][0] == mark && board[2][0] == mark) ||
            (board[0][1] == mark && board[1][1] == mark && board[2][1] == mark) ||
            (board[0][2] == mark && board[1][2] == mark && board[2][2] == mark) ||
            (board[0][0] == mark && board[1][1] == mark && board[2][2] == mark) ||
            (board[0][2] == mark && board[1][1] == mark && board[2][0] == mark)){
            printBoard();
            if(!quickLearn)
                System.out.println("Player " + mark + " wins!");
            game = mark;
            if(mark == 1){
                winsX++;
            }
            else{
                winsO++;
            }
        }
        else{
            boolean tie = true;
            for(int i = 0; i < 3; i++){
                for(int j = 0; j < 3; j++){
                    if(board[i][j] == 0){
                        tie = false;
                    }
                }
            }
            if(tie){
                printBoard();
                if(!quickLearn)
                    System.out.println("It's a tie!");
                game = 3;
                ties++;
            }
            else{
                game = 0;
            }
        }
    }
    private double[][] toRow(){//Flattens the 2D board into one row
        int k = 0;
        double[][] result = new double[1][9];
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                result[0][k] = board[i][j];
                k++;
            }
        }
        return result;
    }
    private void toBoard(int mark){//Turns s 1D location to a 2D location
        if(spot < 3){
            y = 0;
            x = spot;
        }
        else if(spot < 6){
            y = 1;
            x = spot - 3;
        }
        else if(spot < 9){//Not necessary but intuitive;
            y = 2;
            x = spot - 6;
        }
    }
    private void toSpot(int row, int column){//Turns a 2D location to a 1D location
        if(row < 1){
            spot = column;
        }
        else if(row < 2){
            spot = column + 3;
        }
        else if(row < 3){
            spot = column + 6;
        }
    }
    private double[][] toInputs(double[][] in){
        double[][] result = new double[1][18];
        int k = 0;
        for(int i = 0; i < 9; i++){
            if(in[0][i] == 0){
                result[0][k] = 0;
                k++;
                result[0][k] = 0;
            }
            else if(in[0][i] == 1){
                result[0][k] = 1;
                k++;
                result[0][k] = 0;
            }
            else if(in[0][i] == 2){
                result[0][k] = 0;
                k++;
                result[0][k] = 1;
            }
        }
        return result;
    }
    public void aiTurn(int mark){
        printBoard();
        for(int i = 0; i < 9; i++){//Iterate over each square
            if(toRow()[0][i] == 0){
                max = Double.NEGATIVE_INFINITY;//Exaggerated for clarity
                inputs = toRow();
                inputs[0][i] = mark;
                output = nn.feedforward(toInputs(inputs))[0][0];
                if(output > max){
                    max = output;
                    spot = i;//Saving the location in row form
                }
            }
        }
        toBoard(mark);
        board[y][x] = mark;
        win(mark);
    }
    public void aiTrain(int mark){
        printBoard();
        if(Math.random() < .2){
            while(true){
                int r1 = (int)(Math.random()*3);
                int r2 = (int)(Math.random()*3);
                if(board[r1][r2] == 0){
                    board[r1][r2] = mark;
                    actions.add(toRow());
                    break;
                }
            }
        }
        else{
            for(int i = 0; i < 9; i++){//Iterate over each square
                if(toRow()[0][i] == 0){
                    max = Double.NEGATIVE_INFINITY;//Exaggerated for clarity
                    inputs = toRow();
                    inputs[0][i] = mark;
                    output = nn.feedforward(toInputs(inputs))[0][0];
                    if(output > max){
                        max = output;
                        spot = i;//Saving the location in row form
                    }
                }
            }
        }
        win(mark);
    }
    public void aiLearn(){
        int size = states.size();
        if(game == 1){
            for(int i = 0; i < size; i += 2){//states on x's turn is on even indexes and 0
                double[][] action = nn.create(1, 9, 0);
                action[0][actions.get(i)] = 1;
                nn.backpropagation(states.get(i), action);
            }
            for(int i = 1; i < size; i += 2){//states on o's turn is on odd indexes
                double[][] action = nn.create(1, 9, 0);
                while(true){
                    int random = (int)(Math.random()*9);
                    if(random != actions.get(i)){
                        action[0][random] = 1;
                        break;
                    }
                }
                nn.backpropagation(states.get(i), action);
            }
        }
        else if(game == 2){
            for(int i = 1; i < size; i += 2){//states on o's turn is on odd indexes
                double[][] action = nn.create(1, 9, 0);
                action[0][actions.get(i)] = 1;
                nn.backpropagation(states.get(i), action);
            }
            for(int i = 0; i < size; i += 2){//states on x's turn is on even indexes and 0
                double[][] action = nn.create(1, 9, 0);
                while(true){
                    int random = (int)(Math.random()*9);
                    if(random != actions.get(i)){
                        action[0][random] = 1;
                        break;
                    }
                }
                nn.backpropagation(states.get(i), action);
            }
        }
        else if(game == 3){
            for(int i = 0; i < size; i++){
                double[][] action = nn.create(1, 9, 0);
                action[0][actions.get(i)] = 1;
                nn.backpropagation(states.get(i), action);
            }
        }
    }
}
