import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class Login {
    // GUI Component
    private JTextField text_username;
    private JPasswordField text_password;
    private JButton btn_login;
    private JButton btn_signup;
    private JPanel panelForm;
    private static JFrame frame;

    // Logic
    private PreparedStatement stat;
    private ResultSet rs;
    koneksi k = new koneksi();

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

        k.connect();
        btn_login.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                user u = new user();
                try{
                    Login.this.stat = k.getCon().prepareStatement("SELECT * FROM users WHERE " +
                            "username = ? AND password = ?;");
                    Login.this.stat.setString(1, u.username);
                    Login.this.stat.setString(2, u.password);
                    Login.this.rs = Login.this.stat.executeQuery();
                    if (rs.next()) {
                        Transaksi transaction = new Transaksi();
                        transaction.frame.setVisible(true);
                        Login.frame.setVisible(false);
                    }
                } catch (Exception e){
                    JOptionPane.showMessageDialog(null, e.getMessage());
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
