import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class Pembayaran {
    private JPanel panelPembayaran;
    private JTextField text_jumlah_bayar;
    private JButton btn_selesai;
    private JButton btn_batal;
    private JLabel text_total_transaksi;
    private JLabel text_total_kembalian;
    private JButton btn_bayar;
    public static JFrame frame;

    // Database connection
    private PreparedStatement ps;
    private ResultSet rs;
    koneksi conn = new koneksi();
    long prices = 0, total_transaksi = 0, total_bayar = 0, kembalian = 0;

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

        conn.connect();
        try {
            Pembayaran.this.ps = conn.getCon().prepareStatement("SELECT total_price FROM transaction WHERE id_user = ?");
            ps.setInt(1, Login.GetIdUser());
            Pembayaran.this.rs = ps.executeQuery();
            while (rs.next()) {
                prices = prices + rs.getLong("total_price");
            }
            text_total_transaksi.setText("Rp " + String.valueOf(prices));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }

        btn_batal.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                Transaksi.frame.setVisible(true);
                Pembayaran.frame.setVisible(false);
            }
        });
        btn_bayar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                try {
                    total_transaksi = Long.parseLong(text_total_transaksi.getText().split("Rp ")[1]);
                    total_bayar = Long.parseLong(text_jumlah_bayar.getText());
                    kembalian = total_bayar - total_transaksi;

                    if (kembalian >= 0) {
                        text_total_kembalian.setText("Rp " + kembalian);
                        btn_selesai.setEnabled(true);
                    } else {
                        JOptionPane.showMessageDialog(null, "Maaf, jumlah uang yang dibayar kurang.");
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Jumlah Bayar harus berupa angka");
                }
            }
        });
        btn_selesai.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                try{
                    LocalDateTime tanggal = LocalDateTime.now();
                    DateTimeFormatter format = DateTimeFormatter.ofPattern("yyy-MM-dd HH:mm:ss");
                    String formattedDate = tanggal.format(format);
                    Pembayaran.this.ps = conn.getCon().prepareStatement("INSERT INTO payment VALUES " +
                            "(?, ?, ?, ?, ?);");
                    ps.setInt(1, 0);
                    ps.setLong(2, total_transaksi);
                    ps.setLong(3, total_bayar);
                    ps.setLong(4, kembalian);
                    ps.setString(5, formattedDate);
                    ps.executeUpdate();

                    ps = conn.getCon().prepareStatement("DELETE FROM transaction WHERE id_user = ?;");
                    ps.setInt(1, Login.GetIdUser());
                    ps.executeUpdate();

                    Transaksi transaksi = new Transaksi();
                    transaksi.frame.setVisible(true);
                    Pembayaran.frame.setVisible(false);
                    transaksi.btn_logout.setEnabled(true);
                    transaksi.btn_produk.setEnabled(true);
                    transaksi.btn_tambah.setEnabled(true);
                    transaksi.btn_bayar.setEnabled(true);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, e.getMessage());
                }
            }
        });
    }
}
