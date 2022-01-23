import org.sqlite.util.StringUtils;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class NameWindow extends JFrame implements ActionListener{

    //Declare variables
    public static JTextField nameField;
    public JTextField nameButton;
    public int xpos;
    public int ypos;

    //Initialise the window
    public NameWindow(int xpos, int ypos) throws ClassNotFoundException {
        this.xpos = xpos;
        this.ypos = ypos;
        JPanel namePanel = new JPanel();
        namePanel.requestFocus();
        namePanel.setLayout(null);

        //create a label
        JLabel nameLabel = new JLabel("Enter name: ", SwingConstants.LEFT);
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
            //Check if the Text is alphanumeric
            if(isAlphanumeric(name)){
                //Check if the name has already been taken
                Boolean passed = true;
                for(String vertexName : Window.vertexNames){
                    if(vertexName.equals(name)){
                        passed = false;
                    }
                }
                if (passed){
                    //All checks have passed, therefore add a vertex with the name.
                    Main.window.createVertex(xpos, ypos, name);
                    dispose();
                }
                else{
                    JOptionPane.showMessageDialog(null, "Name is already taken");
                }
            }
            else{
                JOptionPane.showMessageDialog(null, "Input must be alphanumeric");
            }
        }
    }

    //function to check if the input contains no special characters.
    public static boolean isAlphanumeric(String str)
    {
        char[] charArray = str.toCharArray();
        for(char c:charArray)
        {
            if (!Character.isLetterOrDigit(c))
                return false;
        }
        return true;
    }
}
