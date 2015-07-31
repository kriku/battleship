package ru.innopolis.battleship;

import java.io.Serializable;

/**
 * Created by krikun on 7/29/2015.
 */
public class Ship {
    int xN = -1;
    int yN = -1;
    int xT = -1;
    int yT = -1;
    int length = -1;
    Direction direction;
    boolean isOk = false;
    boolean horizontal = false;
    boolean vertical = false;

    public Ship(int x, int y, int length, Direction direction) {
        if ((x < 0)||(x > 9)) return;
        if ((y < 0)||(y > 9)) return;
        if ((length > 3)||(length < 0)) return;
        switch (direction) {
            case EAST:
                if (x - length < 0) return;
                xN = x - length;
                xT = x;
                yN = y;
                yT = y;
                horizontal = true;
                break;
            case WEST:
                if (x + length > 9) return;
                xN = x;
                xT = x + length;
                yN = y;
                yT = y;
                horizontal = true;
                break;
            case NORTH:
                if (y - length < 0) return;
                xN = x;
                xT = x;
                yN = y - length;
                yT = y;
                vertical = true;
                break;
            case SOUTH:
                if (y + length > 9) return;
                xN = x;
                xT = x;
                yN = y;
                yT = y + length;
                vertical = true;
                break;
        }
        this.length = length;
        this.direction = direction;
        this.isOk = true;
    }
}
