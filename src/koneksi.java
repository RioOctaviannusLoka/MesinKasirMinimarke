import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;

public class koneksi {
    private String url = "jdbc:mysql://127.0.0.1:3306/kasir_minimarket_schema";
    private String username = "root";
    private String password = "123lkokta";
    private Connection con;

    public void connect(){
        try{
            con = DriverManager.getConnection(url, username, password);
            System.out.println("Koneksi Berhasil");
        } catch(Exception e){
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    public Connection getCon() {
        return con;
    }
}
