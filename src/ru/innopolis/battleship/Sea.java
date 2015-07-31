package ru.innopolis.battleship;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by krikun on 7/29/2015.
 */
public class Sea implements Serializable {
    private int[][] ships = new int[10][10];
    private int[][] shots = new int[10][10];
    private int[] decks = new int[4];
    private int hits = 0;

    public int getHits() {
        return hits;
    }

//  number of enemy hits
    public int[][] getShips() {
        return ships;
    }

//  it's about shots 
    public int[][] getShots() {
        return shots;
    }

    private int putInSea(int c){
        if (c < 0) return 0;
        if (c > 9) return 9;
        return c;
    }

    public static boolean inSea(int c) {
        return (c >= 0) && (c <= 9);
    }

    public static boolean inSea(Point c) {
        return (inSea(c.x) && inSea(c.y));
    }

    public boolean canPutShip(Ship ship) {
        if (!ship.isOk) return false;
//      check for max count of ship for
        if (decks[ship.length] > (3 - ship.length)) return false;
        if (ship.horizontal) {
            for (int x = ship.xN; x <= ship.xT; x++)
                if (ships[x][ship.yN] > 0) return false;
        }
        if (ship.vertical) {
            for (int y = ship.yN; y <= ship.yT; y++)
                if (ships[ship.xN][y] > 0) return false;
        }
        return true;
    }

    public void putShip(Ship ship) {
        if (!canPutShip(ship)) return;
//      mark area around ship
        for (int x = putInSea(ship.xN - 1); x <= putInSea(ship.xT + 1); x++)
            for (int y = putInSea(ship.yN - 1); y <= putInSea(ship.yT + 1); y++)
                ships[x][y] = 1;
//      mark exactly the ship
        for (int x = ship.xN; x <= ship.xT; x++)
            for (int y = ship.yN; y <= ship.yT; y++)
                ships[x][y] = 2;
//      add to count of this type ships
        decks[ship.length]++;
    }

    public void autoPutShip(int padding) {
        for (int l=3; l>=0; l--) {
            if (decks[l] == (4-l)) continue;
            for (int i = 0; i < (4-l); i++) {
                if (decks[l] == (4-l)) continue;
                int side = (int) (Math.random() * 4);
                int indent = (int) (Math.random() * (10 - l));
                int x = 0, y = 0;
                Direction dir = Direction.values()[side];
                switch (side) {
                    case 0:
                        x = padding;
                        y = indent;
                        break;
                    case 1:
                        x = indent;
                        y = 9 - padding;
                        break;
                    case 2:
                        x = 9 - padding;
                        y = 9 - indent;
                        break;
                    case 3:
                        x = 9 - indent;
                        y = padding;
                        break;
                }
                Ship ship = new Ship(x, y, l, dir);
                if (canPutShip(ship)) putShip(ship);
            }
        }
        if (!isAllShipsOn()&& padding < 9) autoPutShip(padding + 1);
    }

    private boolean isAllShipsOn() {
        for (int i=0; i<4; i++)
            if (decks[i] < (4-i)) return false;
        return true;
    }

    private boolean checkAlive(int x, int y) {
        boolean result = false;
        for (int i = x - 1; inSea(i) && ships[i][y] > 1; i--) result = result || ships[i][y] == 2;
        for (int i = x + 1; inSea(i) && ships[i][y] > 1; i++) result = result || ships[i][y] == 2;
        for (int i = y - 1; inSea(i) && ships[x][i] > 1; i--) result = result || ships[x][i] == 2;
        for (int i = y + 1; inSea(i) && ships[x][i] > 1; i++) result = result || ships[x][i] == 2;
        return result;
    }

    private void fillShipDied(int fill[][], int x, int y) {
        fill[x][y] = 4;
        for (int i = x - 1; inSea(i) && fill[i][y] > 2; i--) fill[i][y] = 4;
        for (int i = x + 1; inSea(i) && fill[i][y] > 2; i++) fill[i][y] = 4;
        for (int i = y - 1; inSea(i) && fill[x][i] > 2; i--) fill[x][i] = 4;
        for (int i = y + 1; inSea(i) && fill[x][i] > 2; i++) fill[x][i] = 4;
    }

    private void fillAreaAround(int[][] shots, int x, int y) {
        for (int i = putInSea(x - 1); i <= putInSea(x + 1); i++)
            for (int j = putInSea(y - 1); j <= putInSea(y + 1); j++)
                if (shots[i][j] == 0) shots[i][j] = 1;
    }

    private void fillShotsArea(int[][] shots, int x, int y) {
        for (int i = x; inSea(i) && shots[i][y] == 4; i--) fillAreaAround(shots, i, y);
        for (int i = x; inSea(i) && shots[i][y] == 4; i++) fillAreaAround(shots, i, y);
        for (int i = y; inSea(i) && shots[x][i] == 4; i--) fillAreaAround(shots, x, i);
        for (int i = y; inSea(i) && shots[x][i] == 4; i++) fillAreaAround(shots, x, i);
    }

    public int shot(int[][] shots, int x, int y) {
        if (shots[x][y] > 0) return 1;
        shots[x][y] = 1;
        if (ships[x][y] > 1) {
            hits++;
            ships[x][y] = 3;
            shots[x][y] = 3;
            if (!checkAlive(x, y)) {
                fillShipDied(ships, x, y);
                fillShipDied(shots, x, y);
                fillShotsArea(shots, x, y);
                return 4;
            }
        }
        return ships[x][y];
    }
}
