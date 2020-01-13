package tictactoeai;
import java.util.Scanner;
public class TictactoeSupervised {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Game g = new Game();
        int sessions = 1000;
        g.load();
        boolean flag = true;
        while(flag){
            g.resetGame();
            System.out.println("1: How To Play"
                    + "\n2: Play Local"
                    + "\n3: Play AI"
                    + "\n4: Train AI with AI"
                    + "\nElse: Exit");
            String option = sc.nextLine();
            switch(option){
                case "1":
                    System.out.println("The squares on the board is numbered in the same way as a numeric keypad");
                    System.out.println("The number 1 is the bottom left square and the number 9 is the top right square\n");
                    break;
                case "2":
                    while(true){
                        g.playerTurn(1);
                        if(g.game != 0){
                            break;
                        }
                        g.playerTurn(2);
                        if(g.game != 0){
                            break;
                        }
                    }
                    break;
                case "3":
                    while(true){
                        g.playerTurn(1);
                        if(g.game != 0){
                            break;
                        }
                        g.aiTurn(2);
                        if(g.game != 0){
                            break;
                        }
                    }
                    break;
                case "4":
                    int times;
                    new Thread(() -> {
                        NNest.launch(NNest.class);
                    }).start();
                    while(true){
                        while(true){
                            System.out.println("Train for " + sessions + " sessions how many times?");
                            try{
                                times = sc.nextInt();
                                sc.nextLine();
                                break;
                            }
                            catch(java.util.InputMismatchException e){
                                System.out.println("Not a number...");
                                sc.nextLine();
                            }
                        }
                        for(int l = 0; l < times; l++){
                            for(int i = 0; i < sessions; i++){
                                g.resetGame();
                                while(true){
                                    g.aiTrain(1);
                                    if(g.game != 0){
                                        break;
                                    }
                                    g.aiTrain(2);
                                    if(g.game != 0){
                                        break;
                                    }
                                }
                                g.aiLearn();
                            }
                            g.save();
                            if(g.quickLearn){
                                g.printTrainStats();
                                g.resetScores();
                            }
                        }
                        if(!g.quickLearn){
                            g.printTrainStats();
                            g.resetScores();
                        }
                        System.out.println("\n1: Train again"
                                + "\nElse: Main Menu");
                        String select = sc.nextLine();
                        if("1".equals(select)){
                            continue;
                        }
                        else{
                            break;
                        }
                    }
                    break;
                default:
                    flag = false;
                    break;
            }
        }
    }
}
