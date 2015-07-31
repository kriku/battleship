package ru.innopolis.battleship;

import java.awt.*;
import java.util.Scanner;

/**
 * Created by krikun on 7/31/2015.
 */
public class Human implements Player{
    private Sea sea;

    public Human(Sea sea) {
        this.sea = sea;
    }

    @Override
    public Sea getSea() {
        return sea;
    }

    @Override
    public void setSea(Sea sea) {
        this.sea = sea;
    }

    @Override
    public Point nextShot() {
        Scanner scanner = new Scanner(System.in);
        int x, y;
        do {
            System.out.print("enter shots coordinates: ");
            x = scanner.nextInt();
            y = scanner.nextInt();
            if (!Sea.inSea(x) || !Sea.inSea(y)) {
                return new Point(-1, -1);
//                System.out.println("wrong coordinates! try again...");
            }
            if (sea.getShots()[x][y] > 0) {
                System.out.println("already shots, try again");
                x = -1;
                y = -1;
            }

        } while (!Sea.inSea(x) || !Sea.inSea(y));
        return new Point(x, y);
    }
}
