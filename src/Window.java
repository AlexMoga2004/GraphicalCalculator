import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public class Window extends JPanel implements ActionListener, MouseListener , KeyListener {
    //declare variables
    public static Boolean placeMode = true;
    public static Map<String, Vertex> vertices = new HashMap<String, Vertex>();
    public static List<String> vertexNames = new ArrayList<>();
    public static Timer timer;
    public static String mode = "create";
    public static String highlightedVertex = "none";

    //create a vertex
    public void createVertex(int xpos, int ypos, String name) {
        Vertex vertex = new Vertex(xpos, ypos, name);
        if (!vertices.containsKey(name)) {
            vertices.put(name, vertex);
            vertexNames.add(name);
            timer.start();
        }
    }

    //Function to delete a vertex
    public void deleteVertex(String name){
        //verify that the vertex exists
        if (vertices.containsKey(name)){
            for(String vertexName: vertexNames){
                deleteConnection(name, vertexName);
            }
            vertices.get(name).label.setText("");
            vertices.remove(name);
            vertexNames.remove(name);
        }
    }

    //Function to remove a connection between 2 vertices
    public void deleteConnection(String name1, String name2){
        //verify that the 2 vertices even exist
        if (vertices.containsKey(name1) && vertices.containsKey(name2)){
            //Delete all associations between the 2 nodes, in both of their individual lists
            vertices.get(name1).connections.remove(name2);
            vertices.get(name2).connections.remove(name1);
            vertices.get(name1).connectionNames.remove(name2);
            vertices.get(name2).connectionNames.remove(name1);
        }
    }
    //create a window panel
    public Window() {
        requestFocusInWindow();
        setLayout(new BorderLayout());
        addMouseListener(this);
        addKeyListener(this);
        setCursor(new Cursor(Cursor.TEXT_CURSOR));
        timer = new Timer(100, this);
    }

    //join vertices
    public void joinVertices(String vertex1, String vertex2, float weight) {
        vertices.get(vertex1).addConnection(vertex2, weight);
        vertices.get(vertex2).addConnection(vertex1, weight);
    }

    //draw the vertices and arcs
    @Override
    protected void paintComponent(Graphics g) {
        this.setBackground(Color.decode(Main.background));
        super.paintComponent(g);
        List<String> completedVertices = new ArrayList<>();
        g.setColor(Color.decode(Main.vertexColor));
        for (String vertexName : vertexNames) {
            //make sure highlighted vertices show up as gold
            if(vertexName == highlightedVertex){
                g.setColor(Color.decode("#FFFF00"));
            }
            else{
                g.setColor(Color.decode(Main.vertexColor));
            }
            //draw a circle on each vertex
            g.fillOval(vertices.get(vertexName).getXpos() - (Main.scale / 2), vertices.get(vertexName).getYpos() - (Main.scale / 2), Main.scale, Main.scale);
            this.add(vertices.get(vertexName).label);

            g.setColor(Color.decode(Main.vertexColor));
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

    //function to perform dijkstra's

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

    //Function to handle when the user clicks on the screen.
    @Override
    public void mouseClicked(MouseEvent e) {
        //get the location of the mouse, and respond depending on which mode the user is in
        int xpos = e.getX();
        int ypos = e.getY();
        if(mode.equals("create")){
            try {
                NameWindow nw = new NameWindow(xpos, ypos);
            } catch (ClassNotFoundException classNotFoundException) {
                classNotFoundException.printStackTrace();
            }
        }
        else if(mode.equals("delete")){
            //for each vertex, calculate the distance between the click and the vertex using pythagoras. If distance < scale, count it and delete it.
            String vertexToDelete = "";
            boolean delete = false;
            for(String vertexName : vertexNames){
                int targetXpos = vertices.get(vertexName).getXpos();
                int targetYpos = vertices.get(vertexName).getYpos();
                //this checks whether the user has clicked the vertex or not
                if(Math.pow((xpos - targetXpos), 2) + Math.pow((ypos-targetYpos), 2) <= Math.pow(Main.scale, 2)){
                    vertexToDelete = vertexName;
                    delete = true;
                }
            }
            if(delete){
                deleteVertex(vertexToDelete);
            }
            //if no vertices have been deleted, check if a connection should be deleted instead
            else{
                String vertex1 = "";
                String vertex2 = "";
                boolean toDelete = false;
                //Check if the user has clicked a weight
                for(String vertexName : vertexNames){
                    for(String connectionName : vertices.get(vertexName).connectionNames) {
                        int midX = (vertices.get(vertexName).getXpos() + vertices.get(connectionName).getXpos()) / 2;
                        int midY = (vertices.get(vertexName).getYpos() + vertices.get(connectionName).getYpos()) / 2;
                        midY -= Main.scale;
                        if(Math.pow((xpos - midX), 2) + Math.pow((ypos - midY), 2) <= Math.pow(Main.scale + 5, 2)){
                            vertex1 = vertexName;
                            vertex2 = connectionName;
                            toDelete = true;
                        }
                    }
                }
                //Delete connection
                if(toDelete){
                    deleteConnection(vertex1, vertex2);
                }
            }
        }
        else if(mode.equals("connect")){
            //Check if the user has clicked on a vertex
            for(String vertexName : vertexNames){
                int targetXpos = vertices.get(vertexName).getXpos();
                int targetYpos = vertices.get(vertexName).getYpos();
                //this checks whether the user has clicked the vertex or not
                if(Math.pow((xpos - targetXpos), 2) + Math.pow((ypos-targetYpos), 2) <= Math.pow(Main.scale, 2)){
                    if(vertexName.equals(highlightedVertex)){
                        highlightedVertex = "none";
                    }
                    else if(highlightedVertex.equals("none")){
                        highlightedVertex = vertexName;
                    }
                    else{
                        try {
                            WeightWindow ww = new WeightWindow(vertexName, highlightedVertex);
                        } catch (ClassNotFoundException classNotFoundException) {
                            classNotFoundException.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    //wait for key presses from the user
    @Override
    public void keyPressed(KeyEvent e) {
        //if the user presses z
        if(e.getKeyCode() == 90){
            highlightedVertex = "none";
            mode = "create";
            setCursor(new Cursor(Cursor.TEXT_CURSOR));
        }
        //if the user presses x
        else if(e.getKeyCode() == 88){
            highlightedVertex = "none";
            mode = "delete";
            setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
        }
        //if the user presses c
        else if(e.getKeyCode() ==67){
            mode = "connect";
            setCursor(new Cursor(Cursor.MOVE_CURSOR));
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}