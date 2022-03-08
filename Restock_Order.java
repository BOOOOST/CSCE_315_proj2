import java.sql.*;
import java.awt.*;
import java.awt.BorderLayout;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.plaf.ActionMapUIResource;
import java.util.Vector;

public class Restock_Order extends JFrame implements ActionListener {
    static JFrame bigFrame;
    static Connection conn;
    static JPanel pan;
    static JPanel restockPanel;
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
        Restock_Order gui = new Restock_Order();
        bigFrame = new JFrame("Restock Order View");

        // initialize and set layout of the main panel
        pan = new JPanel();
        pan.setLayout(new GridLayout(0, 4));

        // initialize the close button and add give it an action listener
        JButton b = new JButton("Close");
        b.addActionListener(gui);

        BorderLayout layout = new BorderLayout(20, 15);
        Vector<String> itemNames = new Vector<String>();

        try {
            Statement stm = conn.createStatement();
            String sqlStm = "SELECT * FROM inventory ORDER BY name asc;";
            ResultSet r = stm.executeQuery(sqlStm);

            // gets sku, name, type, quantity, sold_by, and description from inventory table
            // and put them in grid panel
            while (r.next()) {
                String i = r.getString("name");
                JLabel cell = new JLabel(i);
                itemNames.add(i);
                cell.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                pan.add(cell);

                cell = new JLabel(String.valueOf(r.getInt("quantity")));
                cell.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                pan.add(cell);

                String s = r.getString("sold_by");
                cell = new JLabel(s);
                cell.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                pan.add(cell);

                cell = new JLabel(String.valueOf(r.getInt("fill")));
                cell.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                pan.add(cell);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error accessing database.");
        }

        JPanel totalPan = new JPanel();

        JTextField editIn = new JTextField();
        JPanel editPan = new JPanel();
        editPan.setLayout(new GridLayout(1, 3));
        JComboBox itemSell = new JComboBox(itemNames);
        JButton itemB = new JButton("Edit Item Quantity");
        itemB.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                pan = new JPanel();
                pan.setLayout(new GridLayout(0, 4));
                String quant = editIn.getText();
                String sel = (String) itemSell.getSelectedItem();
                try {
                    Statement st = conn.createStatement();
                    String sqlSt = "UPDATE inventory SET quantity = " + quant + " WHERE name = \'" + sel + "\';";
                    st.executeUpdate(sqlSt);
                    String sqlUpdate = "SELECT * FROM inventory ORDER BY name asc;";
                    ResultSet newR = st.executeQuery(sqlUpdate);

                    // gets sku, name, type, quantity, sold_by, and description from inventory table
                    // and put them in grid panel
                    // this part of the code ensures that no reloading needs to be done to update
                    // the gui
                    if (layout.getLayoutComponent(BorderLayout.CENTER) != null) {
                        bigFrame.remove(layout.getLayoutComponent(BorderLayout.CENTER)); // seamlessly transition
                                                                                         // between the restock report
                                                                                         // and list
                    }
                    while (newR.next()) {
                        String i = newR.getString("name");
                        JLabel cell = new JLabel(i);
                        // itemNames.add(i);
                        cell.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                        pan.add(cell);

                        cell = new JLabel(String.valueOf(newR.getInt("quantity")));
                        cell.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                        pan.add(cell);

                        String s = newR.getString("sold_by");
                        cell = new JLabel(s);
                        // itemNames.add(i);
                        cell.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                        pan.add(cell);

                        cell = new JLabel(String.valueOf(newR.getInt("fill")));
                        cell.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                        pan.add(cell);
                    }
                    bigFrame.add(pan, BorderLayout.CENTER);
                    bigFrame.pack();
                    bigFrame.setVisible(true);

                    // JOptionPane.showMessageDialog(null, "Reload to show changes.");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Error accessing database.");
                }
                editIn.setText("");
            }
        });

        editPan.add(itemB);
        editPan.add(itemSell);
        editPan.add(editIn);

        // this part works with editing the fill level of items. Identical to other
        // parts, and allows for seamless transition between list
        // and restock report
        JTextField editInB = new JTextField();
        JPanel editPanB = new JPanel();
        editPanB.setLayout(new GridLayout(1, 4));
        JComboBox fillLevel = new JComboBox(itemNames);
        JButton itemC = new JButton("Edit Fill Level");
        itemC.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                pan = new JPanel();
                pan.setLayout(new GridLayout(0, 4));
                String fill = editInB.getText();
                String selected = (String) fillLevel.getSelectedItem();
                try {
                    Statement st = conn.createStatement();
                    String sqlStatement = "UPDATE inventory SET fill = " + fill + " WHERE name = \'" + selected + "\';";
                    st.executeUpdate(sqlStatement);
                    String sqlUpdate = "SELECT * FROM inventory ORDER BY name asc;";
                    ResultSet newR = st.executeQuery(sqlUpdate);

                    // gets sku, name, type, quantity, sold_by, and description from inventory table
                    // and put them in grid panel
                    if (layout.getLayoutComponent(BorderLayout.CENTER) != null) {
                        bigFrame.remove(layout.getLayoutComponent(BorderLayout.CENTER));
                    }
                    while (newR.next()) {
                        String i = newR.getString("name");
                        JLabel cell = new JLabel(i);
                        // itemNames.add(i);
                        cell.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                        pan.add(cell);

                        cell = new JLabel(String.valueOf(newR.getInt("quantity")));
                        cell.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                        pan.add(cell);

                        String s = newR.getString("sold_by");
                        cell = new JLabel(s);
                        // itemNames.add(i);
                        cell.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                        pan.add(cell);

                        cell = new JLabel(String.valueOf(newR.getInt("fill")));
                        cell.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                        pan.add(cell);

                    }
                    bigFrame.add(pan, BorderLayout.CENTER);
                    bigFrame.pack();
                    bigFrame.setVisible(true);
                    // JOptionPane.showMessageDialog(null, "Reload to show changes.");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Error accessing database.");
                }
                editInB.setText("");
            }
        });

        editPanB.add(itemC);
        editPanB.add(fillLevel);
        editPanB.add(editInB);

        JTextField ItemName = new JTextField();
        JPanel addPanel = new JPanel();
        addPanel.setLayout(new GridLayout(1, 4));
        JTextField ItemQuantity = new JTextField();
        JTextField ItemSKU = new JTextField();
        JTextField ItemType = new JTextField();
        JTextField ItemSold_by = new JTextField();
        JTextField ItemDesc = new JTextField();
        JButton AddButton = new JButton("Add an Item");
        AddButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String SKU = ItemSKU.getText();
                String Name = ItemName.getText();
                String Type = ItemType.getText();
                String quant = ItemQuantity.getText();
                String Sold_by = ItemSold_by.getText();
                String Desc = ItemDesc.getText();
                try {
                    Statement st = conn.createStatement();
                    String sqlSt = "INSERT INTO inventory VALUES ('" + SKU + "', '" + Name + "', '" + Type + "', "
                            + quant + ", '" + Sold_by + "', '" + Desc + "');";
                    System.err.println(sqlSt);
                    st.executeUpdate(sqlSt);

                    JOptionPane.showMessageDialog(null, "Reload to show changes.");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Error accessing database.");
                }
                ItemName.setText("Name");
                ItemQuantity.setText("Quantity");
                ItemSKU.setText("SKU");
                ItemType.setText("Type");
                ItemSold_by.setText("Sold_by");
                ItemDesc.setText("");
            }
        });

        addPanel.add(AddButton);
        addPanel.add(ItemSKU);
        addPanel.add(ItemName);
        addPanel.add(ItemType);
        addPanel.add(ItemQuantity);
        addPanel.add(ItemSold_by);
        addPanel.add(ItemDesc);

        // this is the restock report that shows up with all items that are under fill
        // level
        restockPanel = new JPanel();

        restockPanel.setLayout(new GridLayout(0, 4));
        JButton gen = new JButton("Generate Restock Report");
        gen.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                restockPanel = new JPanel();
                restockPanel.setLayout(new GridLayout(0, 4));
                try {
                    Statement stm = conn.createStatement();
                    String sqlStatement = "SELECT * FROM inventory WHERE fill > quantity ORDER BY name asc;";
                    ResultSet theResult = stm.executeQuery(sqlStatement);

                    // Vector<String> tempNames = new Vector<String>();

                    // puts in the grid table
                    while (theResult.next()) {
                        String i = theResult.getString("name");
                        JLabel cell = new JLabel(i);
                        // tempNames.add(i);
                        cell.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                        restockPanel.add(cell);

                        cell = new JLabel(String.valueOf(theResult.getInt("quantity")));
                        cell.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                        restockPanel.add(cell);

                        String s = theResult.getString("sold_by");
                        cell = new JLabel(s);
                        // tempNames.add(i);
                        cell.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                        restockPanel.add(cell);

                        cell = new JLabel(String.valueOf(theResult.getInt("fill")));
                        cell.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                        restockPanel.add(cell);

                    }

                    if (layout.getLayoutComponent(BorderLayout.CENTER) != null) { // this part allows for the restock
                                                                                  // report to show up at the center
                                                                                  // when button clicked
                        bigFrame.remove(layout.getLayoutComponent(BorderLayout.CENTER));
                        bigFrame.add(restockPanel, BorderLayout.CENTER);
                        bigFrame.pack();
                        bigFrame.setVisible(true);
                    }
                    String theUpdate = "UPDATE inventory SET quantity = fill WHERE fill > quantity;"; // updates the
                                                                                                      // quantity to
                                                                                                      // fill level
                    stm.executeQuery(theUpdate);
                    // JOptionPane.showMessageDialog(null, "Reload to show changes.");
                } catch (Exception ex) {
                    // JOptionPane.showMessageDialog(null, "Error accessing database.");
                }
            }
        });
        // adding all buttons and panels to the frame happens down here
        totalPan.setLayout(new GridLayout(2, 4));
        totalPan.add(addPanel);
        totalPan.add(editPan);
        totalPan.add(editPanB);
        totalPan.add(gen);

        bigFrame.setLayout(layout);
        bigFrame.add(b, BorderLayout.SOUTH);
        bigFrame.add(pan, BorderLayout.CENTER);
        // bigFrame.add(restockPanel, BorderLayout.CENTER);
        bigFrame.add(totalPan, BorderLayout.NORTH);

        bigFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        bigFrame.setSize(600, 600);
        bigFrame.pack();
        bigFrame.setVisible(true);

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
            bigFrame.dispose();
        }
    }

}
