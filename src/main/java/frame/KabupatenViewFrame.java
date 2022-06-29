package frame;

import helpers.Koneksi;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.*;

public class KabupatenViewFrame  extends JFrame {
    private JPanel mainPanel;
    private JPanel cariPanel;
    private JScrollPane viewScrollPanel;
    private JPanel buttonPanel;
    private JTextField textField1;
    private JButton cariButton;
    private JTable viewTable;
    private JButton tambahButton;
    private JButton ubahButton;
    private JButton hapusButton;
    private JButton batalButton;
    private JButton cetakButton;
    private JButton tutupButton;

    public KabupatenViewFrame() {
        hapusButton.addActionListener(e ->{
          int barisTerpilih = viewTable.getSelectedRow();
          if(barisTerpilih <0){
              JOptionPane.showMessageDialog( null, "pilih data dulu");
              return;
          }
          int pilihan = JOptionPane.showConfirmDialog( null, "Yakin mau Hapus?"
          , "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION);
          if (pilihan == 0){
              TableModel tm= viewTable.getModel();
              int id = Integer.parseInt(tm.getValueAt(barisTerpilih,0).toString());
              Connection c =Koneksi.getConnection();
              String deleteSQL = "DELETE FROM kabupaten WHERE id = ?";
              try{
                  PreparedStatement ps = c.prepareStatement(deleteSQL);
                  ps.setInt(1,id);
                  ps.executeUpdate();
              }catch (SQLException ex) {
                  throw new RuntimeException(ex);
              }
          }
        });


        cariButton.addActionListener(e -> {
            Connection c = Koneksi.getConnection();
            String keyword = "%" + textField1.getText() + "%";
            String searchSQL = " SELECT * FROM kabupaten WHERE nama like ?";
            PreparedStatement ps = null;
            try {
                ps = c.prepareStatement(searchSQL);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            try {
                ps.setString(1, keyword);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            ResultSet rs = null;
            try {
                rs = ps.executeQuery();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            DefaultTableModel dtm = (DefaultTableModel) viewTable.getModel();
            dtm.setRowCount(0);
            Object[] row = new Object[2];
            while (true) {
                try {
                    if (!rs.next()) break;
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
                try {
                    row[0] = rs.getInt("id");
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
                try {
                    row[2] = rs.getString("nama");
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
                dtm.addRow(row);

            }
        });
        tutupButton.addActionListener(e -> {
            dispose();
        });
        batalButton.addActionListener(e -> {
            addWindowListener(new WindowAdapter() {
                @Override
                public void windowActivated(WindowEvent e) {
                    isiTabel();
                }
            });
        });
        isiTabel();
        init();
    }

    public void init() {
        setContentPane(mainPanel);
        setTitle("Data Kabupaten");
        pack();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    public void isiTabel() {
        Connection c = Koneksi.getConnection();
        String selectSQL = "SELECT * FROM kabupaten";
        try {
            Statement s = c.createStatement();
            ResultSet rs = s.executeQuery(selectSQL);
            String header[] = {"id", "Nama Kabupaten"};
            DefaultTableModel dtm = new DefaultTableModel(header, 0);
            viewTable.setModel(dtm);
            Object[] row = new Object[2];
            while (rs.next()) {
                row[0] = rs.getInt("id");
                row[1] = rs.getString("nama");
                dtm.addRow(row);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

