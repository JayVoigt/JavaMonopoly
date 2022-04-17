package cc.jayv.monopoly3;

import java.awt.*;
import java.io.*;

/**
 * @author jay
 */
public class JavaMonopoly implements Serializable {

    DynamicView dynamicView;

    public JavaMonopoly() {
        Runnable runner = () -> dynamicView = new DynamicView();
        EventQueue.invokeLater(runner);
    }

    public static void main(String[] args) throws IOException {
        new JavaMonopoly();
    }

    public void loadFromFile() {
        try {
            FileInputStream fileInputStream = new FileInputStream("testFile.dat");
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            dynamicView = (DynamicView) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void saveToFile() {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream("testFile.dat");
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(dynamicView);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}