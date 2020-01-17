package tictactoeai;
import java.util.Scanner;
import java.util.function.Consumer;
public class TictactoeSupervised {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Game g = new Game();
        int sessions = 1000;
        g.load();
        boolean flag = true;
        String player;
        Consumer<Integer> turn1;
        Consumer<Integer> turn2;
        while(flag){
            g.quickLearn = false;
            g.resetGame();
            System.out.println("1: How To Play"
                    + "\n2: Play Local"
                    + "\n3: Play AI"
                    + "\n4: Train AI with AI"
                    + "\n5: AI vs Random"
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
                        System.out.println("Play as Player 1 or 2?");
                        player = sc.nextLine();
                        if(!player.equals("1") && !player.equals("2")){
                            System.out.println("Not an option...");
                            continue;
                        }
                        break;
                    }
                    turn1 = player.equals("1") ? (x) -> g.playerTurn(x) : (x) -> g.aiTurn(x);
                    turn2 = player.equals("2") ? (x) -> g.playerTurn(x) : (x) -> g.aiTurn(x);
                    game:while(true){
                        while(true){
                            turn1.accept(1);
                            if(g.game != 0){
                                break;
                            }
                            turn2.accept(2);
                            if(g.game != 0){
                                break;
                            }
                        }
                        System.out.println("1: Again\nElse: Main Menu");
                        String choice = sc.nextLine();
                        if(!choice.equals("1"))
                            break;
                        g.resetGame();
                    }
                    break;
                case "4":
                    System.out.println("Disable printing?");
                    String fast = sc.nextLine();
                    g.quickLearn = fast.equalsIgnoreCase("1") ? true : fast.equalsIgnoreCase("y");
                    long times;
                    new Thread(() -> {
                        NNest.launch(NNest.class);
                    }).start();
                    while(true){
                        while(true){
                            System.out.println("Train for " + sessions + " sessions how many times?");
                            try{
                                times = sc.nextLong();
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
                case "5":
                    while(true){
                        System.out.println("AI as Player 1 or 2?");
                        player = sc.nextLine();
                        if(!player.equals("1") && !player.equals("2")){
                                System.out.println("Not an option...");
                                continue;
                            }
                        break;
                    }
                    turn1 = player.equals("1") ? (x) -> g.aiTurn(x) : (x) -> g.randomTurn(x);
                    turn2 = player.equals("2") ? (x) -> g.aiTurn(x) : (x) -> g.randomTurn(x);
                    while(true){
                        while(true){
                            turn1.accept(1);
                            if(g.game != 0){
                                break;
                            }
                            turn2.accept(2);
                            if(g.game != 0){
                                break;
                            }
                        }
                        System.out.println("1: Again\nElse: Main Menu");
                        String choice = sc.nextLine();
                        if(!choice.equals("1"))
                            break;
                        g.resetGame();
                    }
                    break;
                default:
                    flag = false;
                    break;
            }
        }
    }
}
