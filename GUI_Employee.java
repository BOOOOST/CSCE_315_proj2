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

      JTextField item_number = new JTextField("Item: ");
      JTextField item_price = new JTextField("Price: ");
      
      JComboBox t = new JComboBox(menu);
      t.setEditable(false);

      // add actionlistener to button
      b.addActionListener(s);
      b1.addActionListener(s);
      t.addActionListener(new ActionListener(){
        public void actionPerformed(ActionEvent e)
        {// a change in the drop down menu
            JComboBox cb = (JComboBox)e.getSource();
            String menu_item = (String)cb.getSelectedItem();
            switch (menu_item){
              case "5 finger original":
                  item_number.setText("501");
                  item_price.setText("$6.50");
                  break;
              case "4 finger meal":
                  item_number.setText("502");
                  item_price.setText("$5.50");
                  break;
              case "three finger meal":
                  item_number.setText("503");
                  item_price.setText("$4.50");
                  break;
              case "kids meal":
                  item_number.setText("504");
                  item_price.setText("$2.50");
                  break;
              case "gallon of tea":
                  item_number.setText("505");
                  item_price.setText("$5.00");
                  break;
              case "family pack":
                  item_number.setText("506");
                  item_price.setText("$32.00");
                  break;
              case "Club Sandwich meal":
                  item_number.setText("507");
                  item_price.setText("$7.50");
                  break;
              case "Club Sandwich only":
                  item_number.setText("508");
                  item_price.setText("$4.75");
                  break;
              case "Sandwich meal combo":
                  item_number.setText("509");
                  item_price.setText("$5.75");
                  break;
              case "sandwich only":
                  item_number.setText("510");
                  item_price.setText("$3.75");
                  break;
              case "Grill cheese meal combo":
                  item_number.setText("511");
                  item_price.setText("$4.50");
                  break;
              case "grill cheese sandwich only":
                  item_number.setText("512");
                  item_price.setText("$3.50");
                  break;
              case "Laynes sauce":
                  item_number.setText("513");
                  item_price.setText("$0.10");
                  break;
              case "Chicken finger":
                  item_number.setText("514");
                  item_price.setText("$1.50");
                  break;
              case "texas toast":
                  item_number.setText("515");
                  item_price.setText("$0.50");
                  break;
              case "potato Salad":
                  item_number.setText("516");
                  item_price.setText("$1.50");
                  break;
              case "Crinkle cut fries":
                  item_number.setText("517");
                  item_price.setText("$1.75");
                  break;
              case "Fountain Drink":
                  item_number.setText("518");
                  item_price.setText("$1.25");
                  break;
              case "Bottle drink":
                  item_number.setText("519");
                  item_price.setText("$2.00");
                  break;
              default:
                  item_number.setText("ERROR");
                  item_price.setText("$ERROR");
              }
          }
      });

      //TODO Step 4
      p.add(b1, BorderLayout.WEST);
      p.add(item_number, BorderLayout.WEST);
      p.add(t, BorderLayout.CENTER);
      p.add(item_price, BorderLayout.EAST);

      // add button to panel
      //p.add(b, BorderLayout.NORTH);

      // add panel to frame
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

    // if a button is pressed
    public void actionPerformed(ActionEvent e)
    {
        String s = e.getActionCommand();
        if (s.equals("Close")) {
            f.dispose();
        }
        else if (s.equals("Add Order")) {
            //add the order to database
            
        }
    }


}