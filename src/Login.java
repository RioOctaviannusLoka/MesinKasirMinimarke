import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.security.MessageDigest;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class Login {
    // GUI Component
    private JTextField text_username;
    private JPasswordField text_password;
    private JButton btn_login;
    private JButton btn_signup;
    private JPanel panelForm;
    public static JFrame frame;

    // Database Connection
    // PreparedStatement prevents SQL Injection because PreparedStatement ensure that user input is treated as data
    // and not executable code, thereby preventing SQL injection attacks.
    private PreparedStatement ps;
    private ResultSet rs;
    koneksi conn = new koneksi();

    private static int idUser;

    public Login() {
        frame = new JFrame("Login");
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(600, 600));
        frame.setResizable(false);

        //now add the panel
        frame.add(panelForm);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        conn.connect();
        btn_login.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                user u = new user();
                try {
                    Login.this.ps = conn.getCon().prepareStatement("SELECT id_user, password FROM users WHERE " +
                            "username = ?;");
                    Login.this.ps.setString(1, u.username);
                    Login.this.rs = Login.this.ps.executeQuery();
                    if (rs.next()) {
                        String password =  rs.getString("password");
                        String hashedPassword = PasswordHash(u.password);

                        if (password.equals(hashedPassword)){
                            // Save id_user for usage in other forms
                            int id_user = rs.getInt("id_user");
                            SetIdUser(id_user);

                            Transaksi transaction = new Transaksi();
                            transaction.frame.setVisible(true);
                            Login.frame.setVisible(false);
                            transaction.btn_logout.setEnabled(true);
                            transaction.btn_produk.setEnabled(true);
                            transaction.btn_tambah.setEnabled(true);
                            transaction.btn_bayar.setEnabled(true);

                            // Close Database Connection
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
                        } else {
                            JOptionPane.showMessageDialog(null, "Password Salah. Silahkan masukkan password yang benar.");
                        }
                    } else{
                        JOptionPane.showMessageDialog(null, "Username salah. Akun tidak ditemukan.");
                    }
                } catch (Exception e){
                    JOptionPane.showMessageDialog(null, e.getMessage());
                }
            }
        });
        btn_signup.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                SignUp signup = new SignUp();
                signup.frame.setVisible(true);
                Login.frame.setVisible(false);
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

    private void SetIdUser(int id_user){
        idUser = id_user;
    }

    public static int GetIdUser(){
        return idUser;
    }

    class user {
        int id_user;
        String username, password;

        public user(){
            this.id_user = 0;
            this.username = text_username.getText();
            this.password = text_password.getText();
        }
    }
}
