package ru.innopolis.battleship;

import java.io.*;

/**
 * Created by krikun on 7/30/2015.
 */
public class Game implements Serializable {
    private Sea sea1;
    private Sea sea2;

    public Game(Sea sea1, Sea sea2) {
        this.sea1 = sea1;
        this.sea2 = sea2;
    }

    public Sea getSea1() {
        return sea1;
    }

    public Sea getSea2() {
        return sea2;
    }

    public void save(String fileName) throws IOException {
        FileOutputStream fos = new FileOutputStream(fileName);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(this);
        oos.close();
    }

    public Game load(String fileName) throws IOException, ClassNotFoundException {
        FileInputStream fis = new FileInputStream(fileName);
        ObjectInputStream ois = new ObjectInputStream(fis);
        return (Game) ois.readObject();
    }
}
