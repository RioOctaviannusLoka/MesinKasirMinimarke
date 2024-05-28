import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class SignUp {
    private JTextField text_username;
    private JPasswordField text_password;
    private JPasswordField text_confirm_password;
    private JButton btn_signup;
    private JButton btn_login;
    private JPanel panelSignUp;
    public static JFrame frame;

    // Database connection
    // PreparedStatement prevents SQL Injection because PreparedStatement ensure that user input is treated as data
    // and not executable code, thereby preventing SQL injection attacks.
    private PreparedStatement ps;
    private ResultSet rs;
    koneksi conn = new koneksi();

    public SignUp(){
        frame = new JFrame("SignUp");
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(600, 600));
        frame.setResizable(false);

        //now add the panel
        frame.add(panelSignUp);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        conn.connect();
        btn_signup.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                user u = new user();
                try {
                    // Check if the username has already been used in database
                    SignUp.this.ps = conn.getCon().prepareStatement("SELECT username FROM users WHERE " +
                            "username = ?");
                    SignUp.this.ps.setString(1, u.username);
                    SignUp.this.rs = SignUp.this.ps.executeQuery();

                    if (rs.next()) {
                        JOptionPane.showMessageDialog(null, "Username telah digunakan!!!");
                    } else {
                        // Check if confirm password is the same as password
                        if (!u.password.equals(u.confirmPassword)) {
                            JOptionPane.showMessageDialog(null, "Confirm Password yang" +
                                    " Anda masukkan berbeda dengan password yang Anda masukkan");
                        } else {
                            // Insert user data into database using Password Hashing
                            String hashedPassword =  SignUp.PasswordHash(u.password);
                            SignUp.this.ps = conn.getCon().prepareStatement("INSERT INTO users (username, password)" +
                                    " VALUES (?, ?)");
                            SignUp.this.ps.setString(1, u.username);
                            SignUp.this.ps.setString(2, hashedPassword);
                            SignUp.this.ps.execute();

                            JOptionPane.showMessageDialog(null, "Akun berhasil dibuat!!!");

                            Login login = new Login();
                            Login.frame.setVisible(true);
                            SignUp.frame.setVisible(false);

                            // Close database connection
                            if (rs != null) {
                                try {
                                    rs.close();
                                } catch (SQLException e) { /* Ignored */}
                            }
                            if (ps != null) {
                                try {
                                    ps.close();
                                } catch (SQLException e) { /* Ignored */}
                            }
                            conn.close();
                            // TODO: Watch Out for SQL INJECTION
                        }
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, e.getMessage());
                }
            }
        });
        btn_login.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Login login = new Login();
                SignUp.frame.setVisible(false);
                login.frame.setVisible(true);
            }
        });
    }

    // Password Hashing
    public static String PasswordHash(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA");
            md.update(password.getBytes());
            byte[] rbt = md.digest();
            StringBuilder sb = new StringBuilder();

            for(byte b: rbt) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (Exception e) {
        }
        return null;
    }

    class user {
        int id_user;
        String username, password, confirmPassword;

        public user(){
            this.id_user = 0;
            this.username = text_username.getText();
            this.password = text_password.getText();
            this.confirmPassword = text_confirm_password.getText();
        }
    }
}
