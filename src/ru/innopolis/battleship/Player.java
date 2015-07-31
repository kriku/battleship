package ru.innopolis.battleship;

import java.awt.*;

/**
 * Created by krikun on 7/31/2015.
 */
public interface Player {
    Point nextShot();

    Sea getSea();

    void setSea(Sea sea);
}
