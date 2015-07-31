package ru.innopolis;


import ru.innopolis.battleship.*;

import java.awt.*;
import java.io.IOException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Player player1;
        Player player2;

        int x = 0, y = 0;
        int toBeContinue = 1;

        Scanner scanner = new Scanner(System.in);

        if (gameTypeChoise(scanner) == 1) {
            player1 = new Human(new Sea());
            player2 = new Bot(new Sea());
        } else {
            player1 = new Bot(new Sea());
            player2 = new Bot(new Sea());
        }

        try {
            Game newGame = new Game(null, null);
            newGame = newGame.load("temp.txt");
            System.out.println("find save game, do you want to load? y/n");
            char answer = scanner.next().charAt(0);
            if (answer == 'y' || answer == 'Y') {
                player1.setSea(newGame.getSea1());
                player2.setSea(newGame.getSea2());
            } else {
                generateNew(player1, player2);
            }
        } catch (Exception e) {
            generateNew(player1, player2);
        }

        do {
            Point shot;

            do {
                if (player1 instanceof Human) {
                    draw(player1.getSea(), player2.getSea());
                }
                shot = player1.nextShot();
                if (!Sea.inSea(shot)) {
                    toBeContinue = 0;
                    break;
                }
                if (player1 instanceof Bot) {
                    System.out.print("[" + shot.x + ":" + shot.y + "] ");
                }
            } while (player2.getSea().shot(player1.getSea().getShots(), shot.x, shot.y) > 1);
            System.out.println();

            if (toBeContinue == 0) { break; }

            do {
                shot = player2.nextShot();
                System.out.print("[" + shot.x + ":" + shot.y + "] ");
            } while (player1.getSea().shot(player2.getSea().getShots(), shot.x, shot.y) > 1);
            System.out.println();

            if (player1 instanceof Bot) {
                draw(player1.getSea(), player2.getSea());
                System.out.print("to continue enter 1, for stop 0: ");
                toBeContinue = scanner.nextInt();
            }


        } while ((toBeContinue > 0)&&(player1.getSea().getHits() < 20)&&(player2.getSea().getHits() < 20));

        System.out.println("first player: " + player2.getSea().getHits() + " hits\n" +
                "second player: " + player1.getSea().getHits() + " hits");

        Game game = new Game(player1.getSea(), player2.getSea());
        try {
            game.save("temp.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void draw(Sea sea1, Sea sea2) {
        int ships1[][] = sea1.getShips();
        int ships2[][] = sea2.getShips();
        int shots1[][] = sea1.getShots();
        int shots2[][] = sea2.getShots();
        System.out.print("#  ");
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 10; x++) System.out.print(x + " ");
            System.out.print("  ");
            if (y==1) System.out.print("|  #  ");
        }
        System.out.println();
        for (int y = 0; y < 10; y++) {
            System.out.print(y + "  ");
            for (int x = 0; x < 10; x++) {
                System.out.print(prettify(ships1[x][y]) + " ");
            }
            System.out.print("  ");
            for (int x = 0; x < 10; x++) {
                System.out.print(prettify(shots1[x][y]) + " ");
            }
            System.out.print("  |  " + y + "  ");
            for (int x = 0; x < 10; x++) {
                System.out.print(prettify(ships2[x][y]) + " ");
            }
            System.out.print("  ");
            for (int x = 0; x < 10; x++) {
                System.out.print(prettify(shots2[x][y]) + " ");
            }
            System.out.print("\n");
        }
    }

    private static String prettify(int what) {
        switch (what) {
            case 0:
                return ".";
            case 1:
                return ":";
            case 2:
                return "S";
            case 3:
                return "W";
            case 4:
                return "X";
            default:
                return "";
        }
    }

    private static int gameTypeChoise(Scanner scanner) {
        System.out.println("Choice game type:\n" +
                "1: human vs bot\n" +
                "2: bot vs bot");
        return scanner.nextInt();
    }

    private static void generateNew(Player one, Player two) {
        System.out.println("generate ships...");
        one.getSea().autoPutShip(0);
        two.getSea().autoPutShip(0);
    }
}
