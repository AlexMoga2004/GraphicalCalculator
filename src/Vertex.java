import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Vertex {
    public int xpos, ypos, timeConfirmed, minimumDistance;
    public int[] potentialDistances;
    public boolean confirmed;
    public Map<String, Integer> connections = new HashMap<String, Integer>();
    public List<String> connectionNames = new ArrayList<>();
    public String name;
    public JLabel label;

    public Vertex(int xpos, int ypos, String name){
        label = new JLabel();
        label.setForeground(Color.decode(Main.textColor));
        label.setBounds(xpos - (Main.scale), ypos - (Main.scale), Main.scale, Main.scale);
        label.setText("<html><div style='text-align: center;'>" + name + "</div></html>");
        label.setFont(new Font("Serif", Font.PLAIN, Main.scale));
        this.xpos = xpos;
        this.ypos = ypos;
        this.name = name;
    }

    public int getXpos(){ return xpos; }
    public int getYpos(){ return ypos; }
    public void addConnection(String vertex, int weight){
        if(connections.containsKey(vertex)) {
            if(connections.get(vertex) > weight){
                connections.replace(vertex, weight);
            }
        }
        else{
            connections.put(vertex, weight);
            connectionNames.add(vertex);
        }
    }
}
