import javax.swing.*;
import java.awt.*;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class Pembayaran {
    private JPanel panelPembayaran;
    private JTextField text_total_transaksi;
    private JTextField text_jumlah_bayar;
    private JTextField text_total_kembalian;
    private JButton btn_hitung;
    private JButton btn_batal;
    private JFrame frame;

    public Pembayaran(){
        frame = new JFrame("Pembayaran");
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(600, 600));
        frame.setResizable(false);

        //now add the panel
        frame.add(panelPembayaran);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
