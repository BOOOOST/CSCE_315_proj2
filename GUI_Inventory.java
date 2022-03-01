import java.sql.*;
import java.awt.*;
import java.awt.BorderLayout;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.plaf.ActionMapUIResource;
import java.util.Vector;

public class GUI_Inventory extends JFrame implements ActionListener {
    static JFrame f;
    static Connection conn;
    static JPanel pan;
    static JButton edit;
    static JTextField edit_in;

    public static void main(String[] args) {
        // Building the connection
        conn = null;

        try {
            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection("jdbc:postgresql://csce-315-db.engr.tamu.edu/csce315903_16db",
                    "csce315903_16user", "prjj1234");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        JOptionPane.showMessageDialog(null, "Opened database successfully");

        // initialize manage GUI
        GUI_Inventory gui = new GUI_Inventory();
        f = new JFrame("Inventory Menu View");

        // initialize and set layout of the main panel
        pan = new JPanel();
        pan.setLayout(new GridLayout(0, 6));

        // initialize the close button and add give it an action listener
        JButton b = new JButton("Close");
        b.addActionListener(gui);

        Vector<String> itemNames = new Vector<String>();

        try {
            Statement stm = conn.createStatement();
            String sqlStm = "SELECT * FROM inventory;";
            ResultSet r = stm.executeQuery(sqlStm);

            while (r.next()) {
                JLabel cell = new JLabel(r.getString("sku"));
                cell.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                pan.add(cell);

                String i = r.getString("name");
                cell = new JLabel(i);
                itemNames.add(i);
                cell.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                pan.add(cell);

                cell = new JLabel(r.getString("type"));
                cell.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                pan.add(cell);

                cell = new JLabel(String.valueOf(r.getInt("quantity")));
                cell.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                pan.add(cell);

                cell = new JLabel(r.getString("sold_by"));
                cell.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                pan.add(cell);

                cell = new JLabel(r.getString("description"));
                cell.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                pan.add(cell);

            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error accessing database.");
        }

        JTextField editIn = new JTextField();
        JPanel editPan = new JPanel();
        editPan.setLayout(new GridLayout(1, 3));
        JComboBox itemSell = new JComboBox(itemNames);
        JButton itemB = new JButton("Edit Item Quantity");
        itemB.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String quant = editIn.getText();
                String sel = (String) itemSell.getSelectedItem();
                try {
                    Statement st = conn.createStatement();
                    String sqlSt = "UPDATE inventory SET quantity = " + quant + " WHERE name = \'" + sel + "\';";
                    st.executeUpdate(sqlSt);

                    JOptionPane.showMessageDialog(null, "Reload to show changes.");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Error accessing database.");
                }
                editIn.setText("");
            }
        });

        editPan.add(itemB);
        editPan.add(itemSell);
        editPan.add(editIn);

        f.setLayout(new BorderLayout(20, 15));
        f.add(b, BorderLayout.SOUTH);
        f.add(pan, BorderLayout.CENTER);
        f.add(editPan, BorderLayout.NORTH);

        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setSize(600, 600);
        f.pack();
        f.setVisible(true);

    }

    // this is called when button 'b' is pressed and it closes the connection to the
    // database and disposes of the frame
    public void actionPerformed(ActionEvent e) {
        String s = e.getActionCommand();
        if (s.equals("Close")) {
            try {
                conn.close();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Connection NOT Closed.");
            }
            f.dispose();
        }
    }

}
