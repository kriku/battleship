package ru.innopolis.battleship;

import java.awt.*;

/**
 * Created by krikun on 7/30/2015.
 */
public class Bot implements Player {
    private Sea sea;

    public Bot(Sea sea) {
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
        int[][] shots = sea.getShots();
        Point shot = wounded();
        if (Sea.inSea(shot)) return shot;
        shot = getOnFirstDiagonal();
        if (Sea.inSea(shot)) return shot;
        shot = getOnSecondDiagonal();
        if (Sea.inSea(shot)) return shot;
        shot = getFromRemainig();
        return shot;
    }

//  search for wounded ships, if wound in line - keep fire for line
    private Point wounded() {
        int[][] shots = sea.getShots();
        for (int x = 0; x < 10; x++)
            for (int y = 0; y < 10; y++)
                if (shots[x][y]==3) {
                    Point inLine = this.inLine(x, y);
                    if (Sea.inSea(inLine)) return inLine;
                    if (Sea.inSea(x - 1) && shots[x - 1][y] == 0) return new Point(x - 1, y);
                    if (Sea.inSea(x + 1) && shots[x + 1][y] == 0) return new Point(x + 1, y);
                    if (Sea.inSea(y - 1) && shots[x][y - 1] == 0) return new Point(x, y - 1);
                    if (Sea.inSea(y + 1) && shots[x][y + 1] == 0) return new Point(x, y + 1);
                }
        return new Point(-1, -1);
    }

    private Point inLine(int x, int y) {
        int[][] shots = sea.getShots();
        if ((Sea.inSea(x - 1) && shots[x - 1][y] == 3)||(Sea.inSea(x + 1) && shots[x + 1][y] == 3)) {
            for (int i = x + 1; Sea.inSea(i) && shots[i][y] != 1; i++)
                if (shots[i][y] == 0) return new Point(i, y);
            for (int i = x - 1; Sea.inSea(i) && shots[i][y] != 1; i--)
                if (shots[i][y] == 0) return new Point(i, y);
        }
        if ((Sea.inSea(y - 1) && shots[x][y - 1] == 3)||(Sea.inSea(y + 1) && shots[x][y + 1] == 3)) {
            for (int i = y + 1; Sea.inSea(i) && shots[x][i] != 1; i++)
                if (shots[x][i] == 0) return new Point(x, i);
            for (int i = y - 1; Sea.inSea(i) && shots[x][i] != 1; i--)
                if (shots[x][i] == 0) return new Point(x, i);
        }
        return new Point(-1, -1);
    }

    private Point getOnFirstDiagonal() {
        int shots[][] = sea.getShots();
        for (int i = 1; i <= 2; i++) {
            for (int j = 0; j < i*4; j++) {
                int x = i*4 - j - 1;
                int y = i*4 - x - 1;
                if (shots[x][y] == 0) return new Point(x, y);
                x = 9 - x;
                y = 9 - y;
                if (shots[x][y] == 0) return new Point(x, y);
            }
        }
        return new Point(-1, -1);
    }

    private Point getOnSecondDiagonal() {
        int shots[][] = sea.getShots();
        for (int i = 1; i <= 3; i++) {
            for (int j = 0; j < i*3; j++) {
                int x = i*3 - j - 1;
                int y = i*3 - x - 1;
                if (shots[x][y] == 0) return new Point(x, y);
                x = 9 - x;
                y = 9 - y;
                if (shots[x][y] == 0) return new Point(x, y);
            }
        }
        return new Point(-1, -1);
    }

    private Point getFromRemainig() {
        int shots[][] = sea.getShots();
        Point shot = new Point(-1, -1);
        for (int i = 0; i < 10; i++)
            for (int j = 0; j < 10; j++)
                if (shots[i][j] == 0) return new Point(i, j);
        return new Point(-1, -1);
    }
}
