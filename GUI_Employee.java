import java.sql.*;
import java.awt.event.*;

import javax.lang.model.util.ElementScanner14;
import javax.swing.*;
import java.awt.*;
import java.util.Vector;
/*
  TODO:
  1) Change credentials for your own team's database
  2) Change SQL command to a relevant query that retrieves a small amount of data
  3) Create a JTextArea object using the queried data
  4) Add the new object to the JPanel p
*/

public class GUI_Employee extends JFrame implements ActionListener {
    static JFrame f;
    public static void main(String[] args)
    {
      //Building the connection
      Connection conn = null;
      //TODO STEP 1
      try {
        Class.forName("org.postgresql.Driver");
        conn = DriverManager.getConnection("jdbc:postgresql://csce-315-db.engr.tamu.edu/csce315903_16db",
           "csce315903_16user", "prjj1234");
      } catch (Exception e) {
        e.printStackTrace();
        System.err.println(e.getClass().getName()+": "+e.getMessage());
        System.exit(0);
      }
      JOptionPane.showMessageDialog(null,"Opened database successfully");

      Vector<String> menu = new Vector<String>();
      Vector<Integer> numList = new Vector<Integer>();
      Vector<Float> MenupriceList = new Vector<Float>();
      Vector<Float> priceList = new Vector<Float>();
      try{
        //create a statement object
        Statement stmt = conn.createStatement();
        //create an SQL statement
        //TODO Step 2
        String sqlStatement = "SELECT * FROM menu_key;";
        //send statement to DBMS
        ResultSet result = stmt.executeQuery(sqlStatement);
        while (result.next()) {
          menu.add(result.getString("name"));
          numList.add(Integer.parseInt(result.getString("item")));
          MenupriceList.add(Float.parseFloat(result.getString("price")));
        }

        sqlStatement = "SELECT * FROM sales_list;";
        //send statement to DBMS
        result = stmt.executeQuery(sqlStatement);
        while (result.next()) {
          priceList.add(Float.parseFloat(result.getString("total_sales")));
        }
        
      } catch (Exception e){
        JOptionPane.showMessageDialog(null,"Error accessing Database.");
      }
      // create a new frame
      f = new JFrame("DB GUI");
      f.setLayout(new BorderLayout(20,15));

      // create a object
      GUI_Employee s = new GUI_Employee();

      // create a panel
      JPanel p = new JPanel();

      JButton b = new JButton("Close");

      JButton b1 = new JButton("Add Order");

      JLabel item_number = new JLabel("Item: ");
      JLabel item_price = new JLabel("Price: ");
      JLabel dateLabel = new JLabel("Date: ");

      JTextField currentDate = new JTextField("(mm/dd/yyyy)");
      
      JComboBox t = new JComboBox(menu);
      t.setEditable(false);

      // add actionlistener to "Close" button
      b.addActionListener(s);
      // add actionlistener to "Add Order" button
      b1.addActionListener(new ActionListener(){
        public void actionPerformed(ActionEvent e){
          Connection conn = null;
          try {
            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection("jdbc:postgresql://csce-315-db.engr.tamu.edu/csce315903_16db",
               "csce315903_16user", "prjj1234");
          } catch (Exception t) {
            t.printStackTrace();
            System.err.println(e.getClass().getName()+": "+t.getMessage());
            System.exit(0);
          }
          
        }}); 

      //add actionlistener for drop down menu change
      t.addActionListener(new ActionListener(){
        public void actionPerformed(ActionEvent e)
        {// a change in the drop down menu
            JComboBox cb = (JComboBox)e.getSource();
            String menu_item = (String)cb.getSelectedItem();
            //set the item number and the item price to its corresponding menu item
            int num;
            float price;
            for (int i = 0; i < menu.size(); i++){
                if (menu.get(i) == menu_item){
                  num = numList.get(i);
                  price = MenupriceList.get(i);
                  item_number.setText("Item: " + String.valueOf(num));
                  item_price.setText("Price: $" + String.valueOf(price));
                }
            }
          }
      });

      //add all menu components to panel
      p.add(b1, BorderLayout.WEST);
      p.add(dateLabel, BorderLayout.WEST);
      p.add(currentDate, BorderLayout.WEST);
      p.add(item_number, BorderLayout.WEST);
      p.add(t, BorderLayout.CENTER);
      p.add(item_price, BorderLayout.EAST);


      // add close button and panel to frame
      f.add(b, BorderLayout.NORTH);
      f.add(p, BorderLayout.CENTER);


      // set the size of frame
      f.setSize(600, 600);

      f.show();

      //closing the connection
      try {
        conn.close();
        JOptionPane.showMessageDialog(null,"Connection Closed.");
      } catch(Exception e) {
        JOptionPane.showMessageDialog(null,"Connection NOT Closed.");
      }
    }

    public void actionPerformed(ActionEvent e)
    {
        String s = e.getActionCommand();
        if (s.equals("Close")) {
            f.dispose();
        }
    }
}