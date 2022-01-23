import org.sqlite.util.StringUtils;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class WeightWindow extends JFrame implements ActionListener{

    //Declare variables
    public static JTextField nameField;
    public JTextField nameButton;
    public String vertex1;
    public String vertex2;

    //Initialise the window
    public WeightWindow(String vertex1, String vertex2) throws ClassNotFoundException {
        this.vertex1 = vertex1;
        this.vertex2 = vertex2;
        JPanel namePanel = new JPanel();
        namePanel.requestFocus();
        namePanel.setLayout(null);

        //create a label
        JLabel nameLabel = new JLabel("Enter weight: ", SwingConstants.LEFT);
        Font f = new Font("SansSerif", Font.BOLD, 15);
        nameLabel.setBounds(0,20, 90, 20);
        nameLabel.setFont(f);
        namePanel.add(nameLabel);


        //create a button
        nameButton = new JTextField("");
        Font f3 = new Font("SansSerif", Font.BOLD, 15);
        nameButton.setBounds(110,20, 120, 20);
        nameButton.setFont(f3);
        nameButton.setBackground(Color.WHITE);
        nameButton.addActionListener((ActionListener) this);
        nameButton.setActionCommand("enter");
        namePanel.add(nameButton);

        //set the window
        setTitle("Name");
        Dimension dimension = new Dimension(250 ,100 );
        setPreferredSize(dimension);
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
        setLocationRelativeTo(null);
        add(namePanel);
        pack();
        namePanel.requestFocus();
    }

    //Detect when a button is pressed
    @Override
    public void actionPerformed(ActionEvent e) {
        String name = nameButton.getText();
        //Check if the enter key is pressed
        if(e.getActionCommand() == "enter"){
            String weightString = nameButton.getText();
            try{
                float weight = Float.parseFloat(weightString);
                if(weight > 0){
                    Main.window.joinVertices(vertex1, vertex2, weight);
                    dispose();
                    Window.highlightedVertex = "none";
                }
                else{
                    JOptionPane.showMessageDialog(null, "Value must be positive");
                }
            }
            catch(Exception x){
                JOptionPane.showMessageDialog(null, "Input must be a number");
            }
        }
    }
}
