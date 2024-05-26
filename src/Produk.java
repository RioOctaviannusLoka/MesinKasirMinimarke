import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class Produk {
    private JTextField text_id_produk;
    private JTextField text_nama_produk;
    public JButton btn_tambah;
    public JButton btn_perbarui;
    public JButton btn_hapus;
    public JButton btn_transaksi;
    private JPanel panelProduk;
    private JTable table_produk;
    public JButton btn_logout;
    private JTextField text_harga_produk;
    private JTextField text_jumlah_diskon;
    private static JFrame frame;

    public Produk(){
        frame = new JFrame("Menu Produk");
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(1000, 600));
        frame.setResizable(false);

        //now add the panel
        frame.add(panelProduk);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        btn_logout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Login login = new Login();
                login.frame.setVisible(true);
                Produk.frame.setVisible(false);
            }
        });
    }
}
