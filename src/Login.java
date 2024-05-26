import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

    // Database connection
    private PreparedStatement ps;
    private ResultSet rs;
    koneksi conn = new koneksi();

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
                    Login.this.ps = conn.getCon().prepareStatement("SELECT * FROM users WHERE " +
                            "username = ? AND password = ?;");
                    Login.this.ps.setString(1, u.username);
                    Login.this.ps.setString(2, u.password);
                    Login.this.rs = Login.this.ps.executeQuery();
                    if (rs.next()) {
                        Transaksi transaction = new Transaksi();
                        transaction.frame.setVisible(true);
                        Login.frame.setVisible(false);
                        transaction.btn_logout.setEnabled(true);
                        transaction.btn_produk.setEnabled(true);
                    } else{
                        JOptionPane.showMessageDialog(null, "Username atau Password salah. Akun tidak ditemukan.");
                    }
                } catch (Exception e){
                    JOptionPane.showMessageDialog(null, e.getMessage());
                } finally {
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
                }
            }
        });
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
