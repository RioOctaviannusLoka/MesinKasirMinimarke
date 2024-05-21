import javax.swing.*;
import java.awt.*;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class SignUp {
    private JTextField text_username;
    private JPasswordField text_password;
    private JPasswordField text_confirm_password;
    private JButton btn_signup;
    private JButton btn_login;
    private JPanel panelSignUp;
    private JFrame frame;

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
    }
}
