import java.sql.*;
import javax.swing.*;
import java.awt.event.*;

public class LoginPage extends JFrame{
    private JTextField usernamField;
    private JPasswordField passwordField;
    private JButton loginButton,createAccountButton;

    public LoginPage(){
        setTitle("Login");
        setSize(300,150);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        JLabel userlabel = new JLabel("Username :");
        add(userlabel);
        userlabel.setBounds(10,20,80,25);

        usernamField = new JTextField();
        add(usernamField);
        usernamField.setBounds(100,20,165,25);

        JLabel passLabel = new JLabel("Password :");
        add(passLabel);
        passLabel.setBounds(10,50,80,25);

        passwordField = new JPasswordField();
        add(passwordField);
        passwordField.setBounds(100,50,165,25);

        loginButton = new JButton("Login");
        add(loginButton);
        loginButton.setBounds(10,80,80,25);

        createAccountButton = new JButton("Register User ?");
        add(createAccountButton);
        createAccountButton.setBounds(100,80,140,25);


        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                String username = usernamField.getText();
                String password = new String(passwordField.getPassword());
                if(validateLogin(username,password)){
                    JOptionPane.showMessageDialog(null, "Login successfull!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    new MainMenu(username);
                    dispose();
                }else{
                    JOptionPane.showMessageDialog(
                        LoginPage.this, 
                        "Invalid username or password.", 
                        "Login Failed", 
                        JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        });

        createAccountButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                new CreateAccount();
                dispose();
            }
        });
        setVisible(true);
    }
    private boolean validateLogin(String username,String password){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/OnlineBanking";
            String dbUsername = "root";
            String dbPassword = "Chandu@96";
            Connection conn = DriverManager.getConnection(url, dbUsername, dbPassword);
            String sql = "SELECT * FROM Users WHERE Username = ? AND PasswordHash = ?";
            PreparedStatement stmt  = conn.prepareStatement(sql);
            stmt.setString(1,username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            boolean isvalid = rs.next();
            rs.close();
            stmt.close();
            conn.close();
            return isvalid;
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public static void main(String[] args) {
        new LoginPage();
    }
}