import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class Transaksi {
    private JPanel panelTransaksi;
    private JLabel text_id_transaksi;
    private JTextField text_jumlah_beli;
    private JComboBox combo_id_produk;
    public JButton btn_bayar;
    public JButton btn_perbarui;
    public JButton btn_hapus;
    public JButton btn_tambah;
    private JTable table_transaksi;
    public JButton btn_logout;
    public JButton btn_produk;
    private JLabel text_jumlah_diskon;
    private JLabel text_total_harga;
    private JScrollPane scrollPane;
    public static JFrame frame;

    private DefaultTableModel model = null;
    // Database connection
    private PreparedStatement ps;
    private ResultSet rs;
    koneksi conn = new koneksi();

    public Transaksi(){
        frame = new JFrame("Transaksi");
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(1200, 600));
        frame.setResizable(false);

        //now add the panel
        frame.add(panelTransaksi);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        // Initialize table model and JTable
        initializeTable();

        // Adding RefreshTable, RefreshCombo, and DB Connection to GUI Component
        conn.connect();
        refreshTable();
        refreshCombo();

        btn_logout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Login login = new Login();
                login.frame.setVisible(true);
                Transaksi.frame.setVisible(false);
            }
        });
        btn_produk.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Produk produk = new Produk();
                produk.frame.setVisible(true);
                Transaksi.frame.setVisible(false);
                produk.btn_transaksi.setEnabled(true);
                produk.btn_logout.setEnabled(true);
                produk.btn_tambah.setEnabled(true);
            }
        });
        btn_bayar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Pembayaran pembayaran = new Pembayaran();
                pembayaran.frame.setVisible(true);
                Transaksi.frame.setVisible(false);
            }
        });
        btn_tambah.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                try {
                    Transaction transaction = new Transaction();
                    Transaksi.this.ps = conn.getCon().prepareStatement("SELECT discount FROM products WHERE id_product = ?;");
                    ps.setInt(1, transaction.id_product);
                    Transaksi.this.rs = ps.executeQuery();
                    long diskon = 0;
                    long total_harga = 0;
                    while (rs.next()) {
                        diskon = rs.getLong("discount");
                        total_harga = transaction.total_price - (diskon * transaction.number_of_items);
                    }

                    text_jumlah_diskon.setText("" + diskon);
                    text_total_harga.setText("" + total_harga);
                    Transaksi.this.ps = conn.getCon().prepareStatement("INSERT INTO transaction VALUES " +
                            "(?, ?, ?, ?, ?);");
                    ps.setInt(1, 0);
                    ps.setInt(2, transaction.id_product);
                    ps.setInt(3, transaction.number_of_items);
                    ps.setLong(4, total_harga);
                    ps.setInt(5, Login.GetIdUser());
                    int pilihan = JOptionPane.showConfirmDialog(null,
                            "Nama Produk: " + transaction.nama_produk +
                                    "\nHarga Produk: " + transaction.harga_produk +
                                    "\nJumlah Beli: " + transaction.number_of_items +
                                    "\nTotal Diskon: " + (diskon * transaction.number_of_items) +
                                    "\nTotal Harga: " + total_harga + "\n",
                            "Tambahkan Transaksi?",
                            JOptionPane.YES_NO_OPTION);
                    if (pilihan == JOptionPane.YES_OPTION) {
                        ps.executeUpdate();
                        refreshTable();
                    } else if (pilihan == JOptionPane.NO_OPTION) {
                        refreshTable();
                    }
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "Jumlah Beli harus berupa angka.");
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, e.getMessage());
                }
            }
        });
        table_transaksi.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                text_id_transaksi.setText(model.getValueAt(table_transaksi.getSelectedRow(),0).toString());
                combo_id_produk.setSelectedItem(model.getValueAt(table_transaksi.getSelectedRow(), 1) + ":" +
                        model.getValueAt(table_transaksi.getSelectedRow(), 2) + ":" +
                        model.getValueAt(table_transaksi.getSelectedRow(), 3));
                text_jumlah_beli.setText(model.getValueAt(table_transaksi.getSelectedRow(), 4).toString());
                text_jumlah_diskon.setText(model.getValueAt(table_transaksi.getSelectedRow(), 5).toString());
                text_total_harga.setText(model.getValueAt(table_transaksi.getSelectedRow(), 6).toString());
                btn_perbarui.setEnabled(true);
                btn_hapus.setEnabled(true);
            }
        });
        btn_perbarui.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                try {
                    Transaction transaction = new Transaction();
                    Transaksi.this.ps = conn.getCon().prepareStatement("SELECT discount FROM products WHERE id_product = ?;");
                    ps.setInt(1, transaction.id_product);
                    Transaksi.this.rs = ps.executeQuery();
                    long diskon = 0;
                    long total_harga = 0;
                    while (rs.next()) {
                        diskon = rs.getLong("discount");
                        total_harga = transaction.total_price - (diskon * transaction.number_of_items);
                    }

                    text_jumlah_diskon.setText("" + diskon);
                    text_total_harga.setText("" + total_harga);

                    transaction.id_transaksi = Integer.parseInt(text_id_transaksi.getText());
                    Transaksi.this.ps = conn.getCon().prepareStatement("UPDATE transaction SET id_product=?," +
                            " number_of_items=?, total_price=?, id_user=? WHERE id_transaction=?;");
                    ps.setInt(1, transaction.id_product);
                    ps.setInt(2, transaction.number_of_items);
                    ps.setLong(3, total_harga);
                    ps.setInt(4, Login.GetIdUser());
                    ps.setInt(5, transaction.id_transaksi);
                    int pilihan = JOptionPane.showConfirmDialog(null,
                            "Nama Produk: " + transaction.nama_produk +
                                    "\nHarga Produk: " + transaction.harga_produk +
                                    "\nJumlah Beli: " + transaction.number_of_items +
                                    "\nTotal Diskon: " + (diskon * transaction.number_of_items) +
                                    "\nTotal Harga: " + total_harga + "\n",
                            "Tambahkan Transaksi?",
                            JOptionPane.YES_NO_OPTION);
                    if (pilihan == JOptionPane.YES_OPTION) {
                        ps.executeUpdate();
                        refreshTable();
                    } else if (pilihan == JOptionPane.NO_OPTION) {
                        refreshTable();
                    }
                    btn_perbarui.setEnabled(false);
                    btn_hapus.setEnabled(false);
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "Jumlah Beli harus berupa angka.");
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, e.getMessage());
                }
            }
        });
        btn_hapus.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                try{
                    Transaksi.this.ps = conn.getCon().prepareStatement("DELETE FROM transaction WHERE id_transaction = ?;");
                    ps.setInt(1, Integer.parseInt(text_id_transaksi.getText()));
                    ps.executeUpdate();
                    refreshTable();
                    btn_perbarui.setEnabled(false);
                    btn_hapus.setEnabled(false);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, e.getMessage());
                }
            }
        });
    }

    class Transaction {
        int id_transaksi, id_product, number_of_items;
        long total_price, harga_produk;
        String nama_produk;

        public Transaction(){
            String combo = combo_id_produk.getSelectedItem().toString();
            String[] arr = combo.split(":");
            this.id_product = Integer.parseInt(arr[0]);
            this.number_of_items = Integer.parseInt(text_jumlah_beli.getText());
            this.nama_produk = arr[1];
            this.harga_produk = Long.parseLong(arr[2]);
            this.total_price = this.harga_produk * this.number_of_items;
        }
    }

    private void initializeTable() {
        model = new DefaultTableModel();
        model.addColumn("ID Transaksi");
        model.addColumn("Id Produk");
        model.addColumn("Nama Produk");
        model.addColumn("Harga Produk");
        model.addColumn("Jumlah Beli");
        model.addColumn("Jumlah Diskon per pcs");
        model.addColumn("Total Harga");
        table_transaksi.setModel(model);
    }

    public void refreshTable(){
        // Clear table before refreshing
        model.setRowCount(0);
        try {
            this.ps = conn.getCon().prepareStatement("SELECT transaction.id_transaction, transaction.id_product, " +
                    "products.product_name, products.product_price, transaction.number_of_items, products.discount, " +
                    "transaction.total_price FROM transaction LEFT JOIN products ON transaction.id_product = " +
                    "products.id_product WHERE transaction.id_user = ?;");
            ps.setInt(1, Login.GetIdUser());
            this.rs = this.ps.executeQuery();
            while (rs.next()) {
                Object[] data = {
                        rs.getInt("id_transaction"),
                        rs.getInt("id_product"),
                        rs.getString("product_name"),
                        rs.getLong("product_price"),
                        rs.getInt("number_of_items"),
                        rs.getLong("discount"),
                        rs.getLong("total_price")
                };
                model.addRow(data);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        text_id_transaksi.setText("");
        text_jumlah_beli.setText("");
        text_jumlah_diskon.setText("0");
        text_total_harga.setText("0");
    }

    public void refreshCombo(){
        try{
            this.ps = conn.getCon().prepareStatement("SELECT id_product, product_name, product_price FROM products WHERE id_user = ?;");
            ps.setInt(1, Login.GetIdUser());
            this.rs = this.ps.executeQuery();
            while (rs.next()) {
                combo_id_produk.addItem(rs.getString("id_product")+":"+
                        rs.getString("product_name")+":"+rs.getString("product_price"));
            }
        } catch (Exception e) {
          JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
}