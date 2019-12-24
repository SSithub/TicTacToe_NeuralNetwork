package tictactoeai;
import java.util.Scanner;
public class TictactoeAI {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Functions fc = new Functions();
        System.out.println("Quick Learn?");
        String quickLearn = sc.next();
        sc.nextLine();
        if("y".equalsIgnoreCase(quickLearn) || "yes".equalsIgnoreCase(quickLearn) || "1".equals(quickLearn))
            fc.quickLearn = true;
        else
            fc.quickLearn = false;
        int batches = 10000;
        fc.oLoad();
        fc.xLoad();
        while(true){
            fc.resetGame();
            fc.resetScores();
            System.out.println("1: Play locally\n2: Play against AI\n3: Train AI With Human\n4: Train AI With Random\n5: Train AI With AI\n6: Train Alternating\n7: Reset Settings\nAnything Else: Exit");;
            String options = sc.nextLine();
            if("1".equals(options)){
                System.out.println("Enter name for Player 1:");
                String player1 = sc.nextLine();
                System.out.println("\nEnter name for Player 2");
                String player2 = sc.nextLine();
                while(true){
                    fc.resetGame();
                    while(fc.game){
                        fc.turnPlayers(player1, player2, 1);
                        fc.winCheck(player1, player2);
                        if(fc.game == false)
                            break;
                        fc.turnPlayers(player1, player2, 2);
                        fc.winCheck(player1, player2);
                        if(fc.game == false)
                            break;
                    }
                    System.out.println("\n1: Play again\nAnything else: Main Menu");
                    String gameOptions = sc.nextLine();
                    if(!"1".equals(gameOptions)) {
                        break;
                    }
                }
            }
            else if("2".equals(options)){
                fc.resetGame();
                while(fc.game){
                    fc.turnPlayers("You", null, 1);
                    fc.winCheck("You", "AI");
                    if(fc.game == false)
                        break;
                    fc.oTurnAI();
                    fc.winCheck("You", "AI");
                    if(fc.game == false)
                        break;
                }
            }
            else if("3".equals(options)){
                fc.resetGame();
                while(fc.game){
                    while(fc.game){
                        fc.turnPlayers("You", null, 1);
                        fc.winCheck("Human", "AI");
                        if(fc.game == false)
                            break;
                        fc.oTurnAI();
                        fc.winCheck("Human", "AI");
                        if(fc.game == false)
                            break;
                    }
                    fc.oTrainAI();
                    fc.oSave();
                    while(true){
                        System.out.println("1.Save settings\n2.Train Again\nAnything Else: Main Menu");
                        String choice = sc.nextLine();
                        if("1".equals(choice))
                            fc.oSave();
                        else if("2".equals(choice)){
                            fc.resetGame();
                            break;
                        }
                        else
                            break;
                    }
                }
            }
            else if("4".equals(options)){
                while(fc.game){
                    System.out.println("Train how many batches of " + batches + "?");
                    int sessions = sc.nextInt();
                    sc.nextLine();
                    for(int i = 1; i <= sessions; i++){
                        for(int j = 1; j <= batches; j++){
                            fc.resetGame();
                            while(fc.game){
                                fc.xTurnRandom();
                                fc.winCheck("Random", "AI");
                                if(fc.game == false)
                                    break;
                                fc.oTurnAI();
                                fc.winCheck("Random", "AI");
                                if(fc.game == false)
                                    break;
                            }
                            fc.oTrainAI();
                            fc.costSum += fc.costPerGame();
                            if(!fc.quickLearn)
                                System.out.println(i+"/"+sessions);
                        }
                        if(fc.quickLearn)
                            System.out.println(i+"/"+sessions);
//                        fc.mutateWeights();
                        fc.oSave();
                    }
                    System.out.println(fc.costSum + " / " + (sessions*batches));
                    System.out.println("Average O Cost: " + fc.costSum/(sessions*batches));
                    System.out.println("X's games won: " + fc.xWinCounter);
                    System.out.println("O's games won: " + fc.oWinCounter);
                    System.out.println("Win/Loss Ratio: " + (double)fc.oWinCounter / fc.xWinCounter);
                    while(true){
                        System.out.println("1.Save settings\n2.Train Again\nAnything Else: Main Menu");
                        String choice = sc.nextLine();
                        if("1".equals(choice))
                            fc.oSave();
                        else if("2".equals(choice)){
                            fc.resetGame();
                            fc.resetScores();
                            break;
                        }
                        else
                            break;
                    }
                }
            }
            else if("5".equals(options)){
                while(fc.game){
                    System.out.println("Train how many batches of " + batches + "?");
                    int sessions = sc.nextInt();
                    sc.nextLine();
                    for(int i = 1; i <= sessions; i++){
                        int counterBefore = fc.oWinCounter;
                        for(int j = 1; j <= batches; j++){
                            fc.resetGame();
                            while(fc.game){
                                fc.XturnAI();
                                fc.winCheck("AI X", "AI O");
                                if(fc.game == false)
                                    break;
                                fc.oTurnAI();
                                fc.winCheck("AI X", "AI O");
                                if(fc.game == false)
                                    break;
                            }
                            fc.oTrainAI();
                            fc.xTrainAI();
                            fc.costSum += fc.costPerGame();
                            if(!fc.quickLearn)
                                System.out.println(i+"/"+sessions);
                        }
                        if(counterBefore == fc.oWinCounter)
                            fc.oAnn.lr = 1*fc.oAnn.startinglr;
                        else
                            fc.oAnn.lr = 1*fc.oAnn.startinglr;
                        if(fc.quickLearn)
                            System.out.println(i+"/"+sessions);
                        fc.mutateWeights();
                        fc.oSave();
                        fc.xSave();
                    }
                    System.out.println("Average O Cost: " + fc.costSum/(sessions*batches));
                    System.out.println("X's games won: " + fc.xWinCounter);
                    System.out.println("O's games won: " + fc.oWinCounter);
                    System.out.println("O Win/Loss Ratio: " + (double)fc.oWinCounter / fc.xWinCounter);
                    System.out.println("X Win/Loss Ratio: " + (double)fc.xWinCounter / fc.oWinCounter);
                    while(true){
                        System.out.println("1.Save settings\n2.Train Again\nAnything Else: Main Menu");
                        String choice = sc.nextLine();
                        if("1".equals(choice))
                            fc.oSave();
                        else if("2".equals(choice)){
                            fc.resetGame();
                            fc.resetScores();
                            break;
                        }
                        else
                            break;
                    }
                }
            }
            else if("6".equals(options)){
                fc.resetGame();
                while(fc.game){
                System.out.println("Train how many batches of " + batches + "?");
                int sessions = sc.nextInt();
                sc.nextLine();
                for(int i = 1; i <= sessions; i++){
                    for(int j = 1; j <= batches; j++){
                        fc.resetGame();
                        if(j % 4 == 1){
                            while(fc.game){
                                fc.xTurnRandom();
                                fc.winCheck("Random X", "AI O");
                                if(fc.game == false)
                                    break;
                                fc.oTurnAI();
                                fc.winCheck("Random X", "AI O");
                                if(fc.game == false)
                                    break;
                            }
                            fc.oTrainAI();
                            fc.xTrainAI();
                            fc.costSum += fc.costPerGame();
                            if(!fc.quickLearn)
                                System.out.println(i+"/"+sessions);
                        }
                        else if(j % 4 == 2){
                            fc.resetGame();
                            while(fc.game){
                                fc.XturnAI();
                                fc.winCheck("AI X", "AI O");
                                if(fc.game == false)
                                    break;
                                fc.oTurnAI();
                                fc.winCheck("AI X", "AI O");
                                if(fc.game == false)
                                    break;
                            }
                            fc.oTrainAI();
                            fc.xTrainAI();
                            fc.costSum += fc.costPerGame();
                            if(!fc.quickLearn)
                                System.out.println(i+"/"+sessions);
                        }
                        if(j % 4 == 3){
                            while(fc.game){
                                fc.xTurnRandom();
                                fc.winCheck("Random X", "Random O");
                                if(fc.game == false)
                                    break;
                                fc.oTurnRandom();
                                fc.winCheck("Random X", "Random O");
                                if(fc.game == false)
                                    break;
                            }
                            fc.oTrainAI();
                            fc.xTrainAI();
                            fc.costSum += fc.costPerGame();
                            if(!fc.quickLearn)
                                System.out.println(i+"/"+sessions);
                        }
                        else if(j % 4 == 0){
                            fc.resetGame();
                            while(fc.game){
                                fc.XturnAI();
                                fc.winCheck("AI X", "Random O");
                                if(fc.game == false)
                                    break;
                                fc.oTurnRandom();
                                fc.winCheck("AI X", "Random O");
                                if(fc.game == false)
                                    break;
                            }
                            fc.oTrainAI();
                            fc.xTrainAI();
                            fc.costSum += fc.costPerGame();
                            if(!fc.quickLearn)
                                System.out.println(i+"/"+sessions);
                        }
                    }
                    if(fc.quickLearn)
                        System.out.println(i+"/"+sessions);
//                    fc.mutateWeights();
                    fc.oSave();
                    fc.xSave();
                }
                System.out.println("Average O Cost: " + fc.costSum/(sessions*batches));
                System.out.println("X's games won: " + fc.xWinCounter);
                System.out.println("O's games won: " + fc.oWinCounter);
                System.out.println("Win/Loss Ratio: " + (double)fc.oWinCounter / fc.xWinCounter);
                while(true){
                    System.out.println("1.Save settings\n2.Train Again\nAnything Else: Main Menu");
                    String choice = sc.nextLine();
                    if("1".equals(choice))
                        fc.oSave();
                    else if("2".equals(choice)){
                        fc.resetGame();
                        fc.resetScores();
                        break;
                    }
                    else
                        break;
                    }
                }
            }
            else if("7".equals(options)){
                while(true){
                    fc.printSettings();
                    System.out.println("Are you sure?\n1: Yes\nElse: No");
                    String choice = sc.nextLine();
                    if("1".equals(choice)){
                        fc.resetSettings();
                        break;
                    }
                    else
                        break;
                }
            }
            else{
                break;
            }
        }
    }
}
