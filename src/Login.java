import javax.swing.*;
import java.awt.*;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class Login {
    private JTextField text_username;
    private JPasswordField text_password;
    private JButton btn_login;
    private JButton btn_signup;
    private JPanel panelForm;
    private JFrame frame;

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
    }
}
