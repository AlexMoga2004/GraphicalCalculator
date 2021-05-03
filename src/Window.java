import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

public class Window extends JPanel implements ActionListener {
    public static Map<String, Vertex> vertices = new HashMap<String, Vertex>();
    public static List<String> vertexNames = new ArrayList<>();
    public static Timer timer;

    public void createVertex(int xpos, int ypos, String name) {
        Vertex vertex = new Vertex(xpos, ypos, name);
        if (!vertices.containsKey(name)) {
            vertices.put(name, vertex);
            vertexNames.add(name);
            timer.start();
        }
    }

    public Window() {
        requestFocus();
        setLayout(new BorderLayout());
        timer = new Timer(100, this);
    }

    public void joinVertices(String vertex1, String vertex2, int weight) {
        vertices.get(vertex1).addConnection(vertex2, weight);
        vertices.get(vertex2).addConnection(vertex1, weight);
    }

    @Override
    protected void paintComponent(Graphics g) {
        this.setBackground(Color.decode(Main.background));
        super.paintComponent(g);
        List<String> completedVertices = new ArrayList<>();
        g.setColor(Color.decode(Main.vertexColor));
        for (String vertexName : vertexNames) {
            //draw a circle on each vertex
            g.fillOval(vertices.get(vertexName).getXpos() - (Main.scale / 2), vertices.get(vertexName).getYpos() - (Main.scale / 2), Main.scale, Main.scale);
            this.add(vertices.get(vertexName).label);

            for (String connectionName : vertices.get(vertexName).connectionNames) {
                if (!completedVertices.contains(connectionName)) {
                    //draw a line for each connection, given it hasn't been done before (e.g if a has been drawn to b, b to a wont be drawn
                    g.drawLine(vertices.get(vertexName).getXpos(), vertices.get(vertexName).getYpos(), vertices.get(connectionName).getXpos(), vertices.get(connectionName).getYpos());

                    //write the weight in the middle
                    int midX = (vertices.get(vertexName).getXpos() +  vertices.get(connectionName).getXpos()) / 2;
                    int midY = (vertices.get(vertexName).getYpos() +  vertices.get(connectionName).getYpos()) / 2;
                    midY -= Main.scale;
                    if (g instanceof Graphics2D){
                        Graphics2D g2d = (Graphics2D)g;
                        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

                        g2d.drawString(String.valueOf(vertices.get(vertexName).connections.get(connectionName)), midX, midY);
                    }
                }
                completedVertices.add(vertexName);
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        invalidate();
        repaint();
    }

    public List<String> dijkstrasShortestPath(String startingVertex, String finalVertex){
        List<String> shortestPath = new ArrayList<>();
        int timeConfirmed = 1;
        for(int step = 1; step <= 7; step++)
        {
            if(step == 1){
                //Mark the first vertex as confirmed
                vertices.get(startingVertex).confirmed = true;
                vertices.get(startingVertex).timeConfirmed = timeConfirmed;
                vertices.get(startingVertex).minimumDistance = 0;
                timeConfirmed ++;
            }
            else if(step == 2){

            }
        }
        return shortestPath;
    }
}