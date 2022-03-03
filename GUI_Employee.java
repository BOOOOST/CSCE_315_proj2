import java.sql.*;
import java.awt.event.*;

import javax.lang.model.util.ElementScanner14;
import javax.swing.*;
import java.awt.*;
import java.util.Vector;
import java.util.Calendar;
import java.util.Date;

public class GUI_Employee extends JFrame implements ActionListener {
    static JFrame f;
    public static void main(String[] args)
    {
      //Building the connection
      Connection conn = null;
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
      try{
        //create a statement object
        Statement stmt = conn.createStatement();
        //create an SQL statement
        String sqlStatement = "SELECT * FROM menu_key ORDER BY item asc;";
        //send statement to DBMS
        ResultSet result = stmt.executeQuery(sqlStatement);
        while (result.next()) {
          menu.add(result.getString("name"));
          numList.add(Integer.parseInt(result.getString("item")));
          MenupriceList.add(Float.parseFloat(result.getString("price")));
        }
      } catch (Exception e){
        JOptionPane.showMessageDialog(null,"Error accessing Database.");
      }
      // create a new frame
      f = new JFrame("DB GUI");
      //set borders for better view on GUI
      f.setLayout(new BorderLayout(20,15));

      // create a object
      GUI_Employee s = new GUI_Employee();

      // create a panel
      JPanel p = new JPanel();

      JButton b = new JButton("Close");

      JButton b1 = new JButton("Add Order");
      //create labels for the things in the GUI
      JLabel item_number = new JLabel("Item: ");
      JLabel item_price = new JLabel("Price: ");
      JLabel dateLabel = new JLabel("Date: ");
      //create a text field that you can type in to change the date
      JTextField currentDate = new JTextField("(mm/dd/yyyy)");
      //create a drop down menu of the items in the menu
      JComboBox t = new JComboBox(menu);
      t.setEditable(false);

      // add actionlistener to "Close" button
      b.addActionListener(s);
      // add actionlistener to "Add Order" button
      b1.addActionListener(new ActionListener(){
        //create variables to use in update statement
        public void actionPerformed(ActionEvent e){
          //These are the values that are currently shown on the GUI, adding the order to the sales_list table
          String dateVar = currentDate.getText();
          int numToUpdate = Integer.parseInt(item_number.getText().substring(6));
          float priceToAdd = Float.parseFloat(item_price.getText().substring(8));
          int itemQuan = 0;
          float newPrice = 0;
          //System.out.print("num: "+ numToUpdate + ", price: " + priceToAdd + " date: " + dateVar + "\n");
          //Create new connection
          Connection conn = null;
          try {
            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection("jdbc:postgresql://csce-315-db.engr.tamu.edu/csce315903_16db",
               "csce315903_16user", "prjj1234");
          } catch (Exception t) {
            t.printStackTrace();
            System.err.println(t.getClass().getName()+": "+t.getMessage());
            System.exit(0);
          }
          //JOptionPane.showMessageDialog(null,"Opened database successfully");

          //Create new Statement and update the list
          try{
            Statement stmt = conn.createStatement();
            //create vectors to add all of the values we need from sales_list as we don't know what's in there currently
            Vector<Integer> quanList = new Vector<Integer>();
            Vector<Float> priceList = new Vector<Float>();
            Vector<String> dateList = new Vector<String>();
            String sqlStatement = "SELECT * FROM sales_list;";
            //send statement to DBMS
            ResultSet result = stmt.executeQuery(sqlStatement);
            while (result.next()) {
              priceList.add(Float.parseFloat(result.getString("total_sales")));
              quanList.add(Integer.parseInt(result.getString("i_" + numToUpdate)));
              dateList.add(result.getString("date"));
            }
            //System.out.print("Got Here");
            //create a statement object
            //loop through values from sales_list to find the columns we want to add the order to
            for (int i = 0; i < dateList.size(); i++){
                if (dateList.get(i).equals(dateVar)){
                    itemQuan = quanList.get(i) + 1;
                    newPrice = priceList.get(i) + priceToAdd;
                }
            }
            //System.out.print("newQuan: " + itemQuan + ", newPrice: " + newPrice + "\n");
            //EX of statement: UPDATE sales_list SET i_501 = 25 WHERE date = '02/13/2022';
            sqlStatement = "UPDATE sales_list SET i_" + numToUpdate + " = " + itemQuan + ", total_sales = " + newPrice + " WHERE date = \'" + dateVar + "\';";
            //send statement to DBMS
            int res = stmt.executeUpdate(sqlStatement);
          
            //TODO 
            if (res == 0){
              Calendar c = Calendar.getInstance();
              //c.setTime(Date(dateVar));
              sqlStatement = "INSERT INTO sales_list VALUES(\'" + dateVar + "\', ;";
              res = stmt.executeUpdate(sqlStatement);
            }
            
            
            //System.out.print(res);
            //JOptionPane.showMessageDialog(null,"Added Order!");
          } catch (Exception y){
            JOptionPane.showMessageDialog(null,"Error accessing Database.");
          }
          //closing the connection
          try {
            conn.close();
            //JOptionPane.showMessageDialog(null,"Connection Closed.");
          } catch(Exception z) {
            JOptionPane.showMessageDialog(null,"Connection NOT Closed.");
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
            //loop through the menu list to find the one that was selected, then make the item number and current price of that menu item show up on the GUI
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
        //JOptionPane.showMessageDialog(null,"Connection Closed.");
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