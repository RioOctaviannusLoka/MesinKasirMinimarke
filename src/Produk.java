import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class Produk {
    private JLabel text_id_produk;
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
    private JScrollPane scrollPane;
    public static JFrame frame;

    private DefaultTableModel model = null;
    // Database connection
    private PreparedStatement ps;
    private ResultSet rs;
    koneksi conn = new koneksi();

    public Produk(){
        frame = new JFrame("Menu Produk");
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(1000, 600));
        frame.setResizable(false);

        frame.add(panelProduk);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        // Initialize table model and JTable
        initializeTable();

        // Adding RefreshTable dan DB Connection to GUI Component
        conn.connect();
        refreshTable();

        btn_logout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                Login login = new Login();
                login.frame.setVisible(true);
                Produk.frame.setVisible(false);
            }
        });
        btn_transaksi.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                Transaksi transaksi = new Transaksi();
                transaksi.frame.setVisible(true);
                Produk.frame.setVisible(false);
                transaksi.btn_logout.setEnabled(true);
                transaksi.btn_produk.setEnabled(true);
            }
        });
        btn_tambah.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                Products products = new Products();
                if (products.product_price > products.discount) {
                    if(products.discount <= (products.product_price * 0.5)) {
                        try {
                            Produk.this.ps = conn.getCon().prepareStatement("INSERT INTO products VALUES " +
                                    "(?, ?, ?, ?, ?);");
                            ps.setInt(1, 0);
                            ps.setString(2, products.product_name);
                            ps.setLong(3, products.product_price);
                            ps.setLong(4, products.discount);
                            ps.setInt(5, Login.GetIdUser());
                            ps.executeUpdate();
                            refreshTable();
                        } catch (NumberFormatException e) {
                            JOptionPane.showMessageDialog(null, "Harga dan Jumlah Diskon harus berupa angka.");
                        } catch (Exception e) {
                            JOptionPane.showMessageDialog(null, e.getMessage());
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Diskon tidak boleh lebih besar dari 50% harga produk");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Harga harus lebih besar dari diskon");
                }
            }
        });
        table_produk.addComponentListener(new ComponentAdapter() {
        });
        table_produk.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                text_id_produk.setText(model.getValueAt(table_produk.getSelectedRow(), 0).toString());
                text_nama_produk.setText(model.getValueAt(table_produk.getSelectedRow(), 1).toString());
                text_harga_produk.setText(model.getValueAt(table_produk.getSelectedRow(), 2).toString());
                text_jumlah_diskon.setText(model.getValueAt(table_produk.getSelectedRow(), 3).toString());
                btn_perbarui.setEnabled(true);
                btn_hapus.setEnabled(true);
            }
        });
        btn_perbarui.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                Products products = new Products();
                if (products.product_price > products.discount) {
                    if(products.discount <= (products.product_price * 0.5)) {
                        try {
                            Login.frame.setVisible(false);
                            Produk.this.ps = conn.getCon().prepareStatement("UPDATE products SET product_name = ?, "
                            + "product_price = ?, discount = ?, id_user = ? WHERE id_product = ?;");
                            ps.setString(1, products.product_name);
                            ps.setLong(2, products.product_price);
                            ps.setLong(3, products.discount);
                            ps.setInt(4, Login.GetIdUser());
                            ps.setInt(5, Integer.parseInt(text_id_produk.getText()));
                            ps.executeUpdate();
                            refreshTable();
                            btn_perbarui.setEnabled(false);
                            btn_hapus.setEnabled(false);
                        } catch (NumberFormatException e) {
                            JOptionPane.showMessageDialog(null, "Harga dan jumlah diskon harus berupa angka.");
                        } catch (Exception e) {
                            JOptionPane.showMessageDialog(null, e.getMessage());
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Diskon tidak boleh lebih besar dari 50% harga produk");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Harga harus lebih besar dari diskon");
                }
            }
        });
        btn_hapus.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                try {
                    Produk.this.ps = conn.getCon().prepareStatement("DELETE FROM products WHERE id_product = ?;");
                    ps.setInt(1, Integer.parseInt(text_id_produk.getText()));
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

    class Products {
        int id_product;
        long product_price, discount;
        String product_name;

        public Products() {
            this.product_name = text_nama_produk.getText();
            this.product_price = Long.parseLong(text_harga_produk.getText());
            this.discount = Long.parseLong(text_jumlah_diskon.getText());
        }
    }

    private void initializeTable() {
        model = new DefaultTableModel();
        model.addColumn("ID Produk");
        model.addColumn("Nama Produk");
        model.addColumn("Harga");
        model.addColumn("Jumlah Diskon");
        table_produk.setModel(model);
    }

    public void refreshTable() {
        Login.frame.setVisible(false);
        // Clear table before refreshing
        model.setRowCount(0);
        try {
            this.ps = conn.getCon().prepareStatement("SELECT * FROM products WHERE id_user = ?;");
            ps.setInt(1, Login.GetIdUser());
            this.rs = this.ps.executeQuery();
            while (rs.next()) {
                Object[] columns = {
                        rs.getInt("id_product"),
                        rs.getString("product_name"),
                        rs.getLong("product_price"),
                        rs.getLong("discount")
                };
                model.addRow(columns);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        text_id_produk.setText("");
        text_nama_produk.setText("");
        text_harga_produk.setText("");
        text_jumlah_diskon.setText("");
    }
}
