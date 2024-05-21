import javax.swing.*;
import java.awt.*;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class Transaksi {
    private JPanel panelTransaksi;
    private JTextField text_id_transaksi;
    private JTextField text_jumlah_beli;
    private JTextField text_jumlah_diskon;
    private JComboBox combo_id_produk;
    public JButton btn_bayar;
    private JTextField text_total_harga;
    private JTextField text_total_transaksi;
    public JButton btn_perbarui;
    public JButton btn_hapus;
    public JButton btn_tambah;
    private JTable table_transaksi;
    public JButton btn_logout;
    public JButton btn_produk;
    private JFrame frame;

    public Transaksi(){
        frame = new JFrame("Transaksi");
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(1000, 600));
        frame.setResizable(false);

        //now add the panel
        frame.add(panelTransaksi);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
