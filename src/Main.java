import javax.swing.*;
import java.awt.*;
import java.io.FileInputStream;
import java.util.Properties;
import java.util.Scanner;

public class Main extends JFrame {

    public static String version;
    public static int resX;
    public static int resY;
    public static Window window = new Window();
    public static int scale;
    public static String background, vertexColor, textColor;

    public Main(){
        //Fetch initial variables
        try {
            Properties properties = new Properties();
            properties.load(new FileInputStream("properties.ini"));

            version = properties.getProperty("version");
            resX = Integer.parseInt(properties.getProperty("resx"));
            resY = Integer.parseInt(properties.getProperty("resy"));
            scale = Integer.parseInt(properties.getProperty("scale"));
            background = properties.getProperty("background");
            vertexColor = properties.getProperty("vertexColor");
            textColor = properties.getProperty("textColor");

        }
        catch(Exception e){
            System.out.println("Failed to load variables!!! ");
            System.out.println(e);
        }
        setTitle("Graphical calculator");
        Dimension dimension = new Dimension(resX, resY);
        setPreferredSize(dimension);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
        add(window);
        pack();
    }

    public static void main(String[] args){
        Main main = new Main();
        window.createVertex(100,300,"a");
        window.createVertex(400,500,"b");
        window.createVertex(650,100,"c");
        window.createVertex(550,300,"d");
        window.createVertex(1000,300,"e");
        window.createVertex(1000,600,"f");
        window.joinVertices("a","b",70);
        window.joinVertices("a","d",120);
        window.joinVertices("d","c",40);
        window.joinVertices("c","e",90);
        window.joinVertices("d","e",60);
        window.joinVertices("e","f",70);
        window.joinVertices("d","f",160);
        window.joinVertices("d","b",40);
        window.joinVertices("b","f",200);

    }
}