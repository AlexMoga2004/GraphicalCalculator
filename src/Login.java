import org.sqlite.util.StringUtils;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.sql.*;

public class Login extends JFrame implements ActionListener{

    public static JTextField userNameField;
    public static JPasswordField passwordField;
    public static String username;
    public static String password;
    public static int numberOfUsers;

    public Login() throws ClassNotFoundException {
        //create all of the components and the panel

        JPanel loginPanel = new JPanel();
        loginPanel.requestFocus();
        loginPanel.setLayout(null);

        JLabel welcomeLabel = new JLabel("Graphical Calculator.", SwingConstants.CENTER);
        Font f = new Font("SansSerif", Font.BOLD, 25);
        welcomeLabel.setBounds(40,0, 260, 100);
        welcomeLabel.setFont(f);
        loginPanel.add(welcomeLabel);

        userNameField = new JTextField("Username");
        Font f2 = new Font("SansSerif", Font.BOLD, 15);
        userNameField.setFont(f2);
        userNameField.setBounds(20, 100, 310, 60);

        //make an empty field display "username"
        userNameField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if(userNameField.getText().equals("Username")) {
                    userNameField.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if(userNameField.getText().equals("")){
                    userNameField.setText("Username");
                }
            }
        });
        loginPanel.add(userNameField);

        passwordField = new JPasswordField("Password");
        passwordField.setEchoChar((char)0);

        passwordField.setFont(f2);
        passwordField.setBounds(20, 200, 310, 60);

        //make the empty field display password"
        passwordField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if(new String(passwordField.getPassword()).equals("Password")) {
                    passwordField.setText("");
                    passwordField.setEchoChar('*');
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if(new String(passwordField.getPassword()).equals("")){
                    passwordField.setEchoChar((char)0);
                    passwordField.setText("Password");
                }
            }
        });
        loginPanel.add(passwordField);

        JButton loginButton = new JButton("Login");
        Font f3 = new Font("SansSerif", Font.BOLD, 15);
        loginButton.setBounds(30,300, 130, 100);
        loginButton.setFont(f3);
        loginButton.setBackground(Color.WHITE);
        loginButton.addActionListener((ActionListener) this);
        loginButton.setActionCommand("login");
        loginPanel.add(loginButton);

        JButton signupButton = new JButton("Sign up");
        signupButton.setBounds(180,300, 130, 100);
        signupButton.setFont(f3);
        signupButton.setBackground(Color.WHITE);
        signupButton.addActionListener((ActionListener) this);
        signupButton.setActionCommand("sign up");
        loginPanel.add(signupButton);



        //create the login window
        setTitle("Graphical calculator");
        Dimension dimension = new Dimension(360 ,500 );
        setPreferredSize(dimension);
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
        setLocationRelativeTo(null);
        add(loginPanel);
        pack();

    }

    public void login(String username, String password){
        String jdbcUrl = "jdbc:sqlite:C:\\Users\\Niturzion\\Documents\\SQLite\\sqlite-tools-win32-x86-3360000\\users.db";

        //Input Validation : The String must not be more than 20 characters long and cannot contain SQL injection.
        if(isAlphanumeric(username) && isAlphanumeric(password) && username.length() <= 20 && password.length() <= 20){
            try {
                //load and register JDBC driver for mysql
                Connection connection = DriverManager.getConnection(jdbcUrl);
                String sql = "SELECT * FROM User WHERE Username = \"";
                sql = sql + username + "\"";
                Statement statement = connection.createStatement();
                ResultSet result = statement.executeQuery(sql);
                if(userNameField.getText().equals("Username") || new String(passwordField.getPassword()).equals("Password")){
                    JOptionPane.showMessageDialog(this, "Please enter some data");
                }

                if(result.next()){
                    String realPassword = result.getString("Password");
                    if(password.equals(realPassword)){
                        Main.startProgram();
                        dispose();
                    }
                    else{
                        JOptionPane.showMessageDialog(this, "Login credentials are not valid!");
                    }
                }
                else{
                    JOptionPane.showMessageDialog(this, "Login credentials are not valid!");
                }

            }catch(SQLException e){
                e.printStackTrace();
            }
        }
        else{
            JOptionPane.showMessageDialog(this, "Username and password MUST be less than 20 characters long and must be alphanumeric");
        }
    }

    public void signUp(String username, String password){
        String jdbcUrl = "jdbc:sqlite:C:\\Users\\Niturzion\\Documents\\SQLite\\sqlite-tools-win32-x86-3360000\\users.db";

        //Input Validation : The String must not be more than 20 characters long and cannot contain SQL injection.
        if(isAlphanumeric(username) && isAlphanumeric(password) && username.length() <= 20 && password.length() <= 20){
            try {
                //load and register JDBC driver for mysql
                Connection connection = DriverManager.getConnection(jdbcUrl);
                String sql = "SELECT * FROM User WHERE Username = \"";
                sql = sql + username + "\"";
                Statement statement = connection.createStatement();
                ResultSet result = statement.executeQuery(sql);
                if(userNameField.getText().equals("Username") || new String(passwordField.getPassword()).equals("Password")){
                    JOptionPane.showMessageDialog(this, "Please enter some data");
                }

                //if user already exists
                if(result.next()){
                   JOptionPane.showMessageDialog(this, "This Username is already taken");
                }
                else{
                    //Add the data to the database
                    result = statement.executeQuery("SELECT COUNT(*) FROM User");
                    numberOfUsers = result.getInt(1);
                    System.out.println(numberOfUsers);
                    sql = "INSERT INTO User VALUES (";
                    sql += (numberOfUsers + 1) + ", \"";
                    sql += username + "\", \"";
                    sql += password + "\");";
                    System.out.println(sql);
                    statement.execute(sql);
                }

            }catch(SQLException e){
                e.printStackTrace();
            }
        }
        else{
            JOptionPane.showMessageDialog(this, "Username and password MUST be less than 20 characters long and must be alphanumeric");
        }
    }

    //check if a string contains banned characters
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

    //wait for a button press.
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("login")){
            login(userNameField.getText(), new String(passwordField.getPassword()));
        }
        else if(e.getActionCommand().equals("sign up")){
            signUp(userNameField.getText(), new String(passwordField.getPassword()));
        }
    }
}
