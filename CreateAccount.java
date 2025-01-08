import javax.swing.*;
import java.sql.*;

public class CreateAccount extends JFrame {
    private JTextField usernameField, balanceField, fullNameField, emailField, phoneField;
    private JPasswordField passwordField;
    private JButton createButton, backButton;

    public CreateAccount() {
        setTitle("Registration of User");
        setSize(300, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        JLabel label1 = new JLabel("Username: ");
        label1.setBounds(10, 20, 80, 25);
        add(label1);

        usernameField = new JTextField();
        usernameField.setBounds(100, 20, 165, 25);
        add(usernameField);

        JLabel label2 = new JLabel("Full Name: ");
        label2.setBounds(10, 60, 80, 25);
        add(label2);

        fullNameField = new JTextField();
        fullNameField.setBounds(100, 60, 165, 25);
        add(fullNameField);

        JLabel label3 = new JLabel("Email ID: ");
        label3.setBounds(10, 100, 80, 25);
        add(label3);

        emailField = new JTextField();
        emailField.setBounds(100, 100, 165, 25);
        add(emailField);

        JLabel label4 = new JLabel("Phone Number: ");
        label4.setBounds(10, 140, 80, 25);
        add(label4);

        phoneField = new JTextField();
        phoneField.setBounds(100, 140, 165, 25);
        add(phoneField);

        JLabel label5 = new JLabel("Balance: ");
        label5.setBounds(10, 180, 80, 25);
        add(label5);

        balanceField = new JTextField();
        balanceField.setBounds(100, 180, 165, 25);
        add(balanceField);

        JLabel label6 = new JLabel("Password: ");
        label6.setBounds(10, 220, 80, 25);
        add(label6);

        passwordField = new JPasswordField();
        passwordField.setBounds(100, 220, 165, 25);
        add(passwordField);

        createButton = new JButton("Create Account");
        createButton.setBounds(10, 260, 130, 25);
        add(createButton);

        backButton = new JButton("Back to login!");
        backButton.setBounds(150, 260, 130, 25);
        add(backButton);

        createButton.addActionListener(e -> createAccount());
        backButton.addActionListener(e -> {
            setVisible(false);
            new LoginPage().setVisible(true);
        });

        setVisible(true);
    }

    private void createAccount() {
        String username = usernameField.getText();
        String fullName = fullNameField.getText();
        String email = emailField.getText();
        String phone = phoneField.getText();
        String balanceText = balanceField.getText();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || fullName.isEmpty() || email.isEmpty() || phone.isEmpty() || balanceText.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        double balance;
        try {
            balance = Double.parseDouble(balanceText);
            if (balance < 0) {
                JOptionPane.showMessageDialog(this, "Balance cannot be negative.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid balance value.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/OnlineBanking";
            String dbUsername = "root";
            String dbPassword = "Chandu@96";
            Connection conn = DriverManager.getConnection(url, dbUsername, dbPassword);

            String sql = "INSERT INTO Users (Username, PasswordHash, FullName, Email, Phone, Balance) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, fullName);
            stmt.setString(4, email);
            stmt.setString(5, phone);
            stmt.setDouble(6, balance);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    int userID = rs.getInt(1);
                    String accountNumber = generateAccountNumber(userID);

                    String accSql = "INSERT INTO Accounts (UserID, AccountNumber, AccountType, Balance) VALUES (?, ?, ?, ?)";
                    PreparedStatement accStmt = conn.prepareStatement(accSql);
                    accStmt.setInt(1, userID);
                    accStmt.setString(2, accountNumber);
                    accStmt.setString(3, "Savings");
                    accStmt.setDouble(4, balance);
                    accStmt.executeUpdate();

                    JOptionPane.showMessageDialog(this, "Account created successfully!");
                    accStmt.close();
                }
                rs.close();
            }

            stmt.close();
            conn.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private String generateAccountNumber(int userID) {
        return "EBSACC" + String.format("%04d", userID);
    }
}

// import javax.swing.*;
// import java.awt.event.*;
// import java.sql.Connection;
// import java.sql.DriverManager;
// import java.sql.PreparedStatement;
// import java.sql.ResultSet;
// public class CreateAccount extends JFrame{
//     private JTextField usernameField,balanceField,fullNameField,emailField,phoneField;
//     private JPasswordField passwordField;
//     private JButton createButton,backButton;

//     public CreateAccount(){
//         setTitle("Registration of User");
//         setSize(300,350);
//         setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//         setLayout(null);

//         JLabel label1 = new JLabel("Username: ");
//         label1.setBounds(10,20,80,25);
//         add(label1);
        
//         usernameField = new JTextField();
//         usernameField.setBounds(100,20,165,25);
//         add(usernameField);
        

//         JLabel label2 = new JLabel("Full Name: ");
//         add(label2);
//         label2.setBounds(10,60,80,25);
//         fullNameField = new JTextField();
//         add(fullNameField);
//         fullNameField.setBounds(100,60,165,25);

//         JLabel label3 = new JLabel("Email ID: ");
//         add(label3);
//         label3.setBounds(10,100,80,25);
//         emailField = new JTextField();
//         add(emailField);
//         emailField.setBounds(100,100,165,25);

//         JLabel label4 = new JLabel("Phone Number: ");
//         add(label4);
//         label4.setBounds(10,140,80,25);
//         phoneField = new JTextField();
//         add(phoneField);
//         phoneField.setBounds(100,140,165,25);

//         JLabel label5 = new JLabel("Balance : ");
//         add(label5);
//         label5.setBounds(10,180,80,25);
//         balanceField = new JTextField();
//         add(balanceField);
//         balanceField.setBounds(100,180,165,25);

//         JLabel label6 = new JLabel("Password: ");
//         add(label6);
//         label6.setBounds(10,220,80,25);
//         passwordField = new JPasswordField();
//         add(passwordField);
//         passwordField.setBounds(100,220,165,25);

//         createButton = new JButton("Create Account");
//         add(createButton);
//         createButton.setBounds(10,260,130,25);

//         backButton = new JButton("Back to login!");
//         add(backButton);
//         backButton.setBounds(150,260,130,25);

//         createButton.addActionListener(new ActionListener(){
//             @Override
//             public void actionPerformed(ActionEvent e){
//                 createAccount();
//             }
//         });

//         backButton.addActionListener(new ActionListener(){
//             @Override
//             public void actionPerformed(ActionEvent e){
//                 setVisible(false);
//                 new LoginPage().setVisible(true);
//             }
//         });

//         setVisible(true);
//     }
//     private void createAccount(){
//         String username = usernameField.getText();
//         String fullName = fullNameField.getText();
//         String email = emailField.getText();
//         String phone = phoneField.getText();
//         double balance = Double.parseDouble(balanceField.getText());
//         String password = new String(passwordField.getPassword());

//         if(username.isEmpty() || fullName.isEmpty() || email.isEmpty() || phone.isEmpty() || (balanceField.getText()).isEmpty() || password.isEmpty()){
//             JOptionPane.showMessageDialog(this,"All Fields are required!","Error",JOptionPane.ERROR_MESSAGE);
//         }else if(balance<0){
//             JOptionPane.showMessageDialog(this,"Balance Cannot be Negative","Error",JOptionPane.ERROR_MESSAGE);
//         }else{
//             try{
//                 Class.forName("com.mysql.cj.jdbc.Driver");
//                 String url = "jdbc:mysql://localhost:3306/OnlineBanking";
//                 String dbUsername = "root";
//                 String dbPassword = "Chandu@96";
//                 Connection conn = DriverManager.getConnection(url, dbUsername, dbPassword);
//                 String sql = "INSERT INTO Users (Username,PasswordHash,FullName,Email,Phone,Balance) VALUES (?,?,?,?,?,?)";
//                 PreparedStatement stmt  = conn.prepareStatement(sql);
//                 stmt.setString(1,username);
//                 stmt.setString(2, password);
//                 stmt.setString(3,fullName);
//                 stmt.setString(4,email);
//                 stmt.setString(5,phone);
//                 stmt.setDouble(6,balance);
//                 int rowsaffected = stmt.executeUpdate();

//                 if(rowsaffected>0){
//                     //JOptionPane.showMessageDialog(this,"Account created Successfully!");
//                     ResultSet rs=stmt.getGeneratedKeys();
//                     if(rs.next()){
//                         int userID = rs.getInt(1);
//                         String accountno = generateAccountno(userID);
//                         String accsql = "INSERT INTO Accounts(UserID,AccountNumber,AccountType,Balance) VALUES(?,?,?,?)";
//                         PreparedStatement accstmt = conn.prepareStatement(accsql);
//                         accstmt.setInt(1,userID);
//                         accstmt.setString(2,accountno);
//                         accstmt.setString(3,"Savings");
//                         accstmt.setDouble(4,balance);
//                         accstmt.executeUpdate();

//                         JOptionPane.showMessageDialog(this,"Account created Successfully!");
//                         accstmt.close();
//                     }
//                     rs.close();
//                 }

//                 stmt.close();
//                 conn.close();
//             }catch(Exception e){
//                 JOptionPane.showMessageDialog(this,"Error : "+e.getMessage(),"Database Error",JOptionPane.ERROR_MESSAGE);
//                 e.printStackTrace();
//             }
//         }
//     }
//     private String generateAccountno(int userid){
//         String p = "EBSACC";
//         return p+String.format("%04d",userid);
//     }
// }
