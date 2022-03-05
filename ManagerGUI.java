import java.sql.*;
import java.awt.*;
import java.awt.BorderLayout;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.plaf.ActionMapUIResource;
import java.util.Vector;

public class ManagerGUI extends JFrame implements ActionListener {
    static JFrame f;
    static Connection conn;
    static JPanel pan;
    static JButton edit;
    static JComboBox edit_targ_name;
    static JComboBox edit_targ_col;
    static JTextField edit_in;
    static JButton rem;
    static JComboBox rem_targ_name;

    public static void main(String[] args){
      //Building the connection
      conn = null;
	  
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
      
      // initialize manage GUI
      ManagerGUI gui = new ManagerGUI();
      f = new JFrame("Manager Menu View");

      // separate panel for the add item button and its parts
      JPanel add_item_pan = new JPanel();
      add_item_pan.setLayout(new GridLayout(2,5));

      // initialize and set layout of the main panel
      pan = new JPanel();
      pan.setLayout(new GridLayout(0,4));

      // initialize the close button and add give it an action listener
      JButton b = new JButton("Close");
      b.addActionListener(gui);
      
      //initialize add item button components
      JButton add = new JButton("Add Item");
      JTextField add_id = new JTextField();
      JTextField add_name = new JTextField();
      JTextField add_desc = new JTextField();
      JTextField add_price = new JTextField();
      add_item_pan.add(new JLabel(""));
      add_item_pan.add(new JLabel("Item", SwingConstants.CENTER));
      add_item_pan.add(new JLabel("Name", SwingConstants.CENTER));
      add_item_pan.add(new JLabel("Description", SwingConstants.CENTER));
      add_item_pan.add(new JLabel("Price", SwingConstants.CENTER));
      add_item_pan.add(add);
      add_item_pan.add(add_id);
      add_item_pan.add(add_name);
      add_item_pan.add(add_desc);
      add_item_pan.add(add_price);

      //initialize edit item button components
      edit = new JButton("Edit Item");
      edit_targ_name = new JComboBox();
      Vector<String> col_names = new Vector<String>();
      col_names.add("item");
      col_names.add("name");
      col_names.add("description");
      col_names.add("price");
      edit_targ_col = new JComboBox(col_names);
      edit_in = new JTextField();

      //initialize remove item button components
      rem = new JButton("Remove Item");
      rem_targ_name = new JComboBox();
      
      //this calls refresh panel which adds all components and database entries for menu_key into the main panel
      try{
        refreshPanel();
      } catch (Exception e){
        JOptionPane.showMessageDialog(null,"Error accessing Database: " + e);
      }

      //set frame layout and add all components to frame
      f.setLayout(new BorderLayout(20,15));
      f.add(b,BorderLayout.SOUTH);
      f.add(add_item_pan,BorderLayout.WEST);
      f.add(pan,BorderLayout.CENTER);
      f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      f.setSize(600,600);
      f.pack();
      f.setVisible(true);

      // definition for edit item button that changes the value of a certain attribute of an item given by the selected name
      edit.addActionListener(new ActionListener(){
        public void actionPerformed(ActionEvent e){
          String sel = (String)edit_targ_name.getSelectedItem();
          String type = (String)edit_targ_col.getSelectedItem();
          String in = edit_in.getText();
          //make statement
          try{
            Statement stmt = conn.createStatement();
            String sqlStatement = "";
            if(type.equals("item")){
              sqlStatement = "UPDATE menu_key SET " + type + " = " + in + " WHERE name = \'" + sel + "\';";
            } else {
              sqlStatement = "UPDATE menu_key SET " + type + " = \'" + in + "\' WHERE name = \'" + sel + "\';";
            }
            stmt.executeUpdate(sqlStatement);
          } catch(Exception x){
            JOptionPane.showMessageDialog(null,"Error accessing Database." + x);
          }
          edit_in.setText("");
          if(type.equals("name")){
            edit_targ_name.removeItem(sel);
            edit_targ_name.addItem(in);
            rem_targ_name.removeItem(sel);
            rem_targ_name.addItem(in);
          }
          refreshPanel();
        }
      });

      // definition for add item button that adds a new row to menu_key given all attributes. does nothing on conflict
      add.addActionListener(new ActionListener(){
        public void actionPerformed(ActionEvent e){
          int item = Integer.parseInt(add_id.getText());
          String name = add_name.getText();
          String desc = add_desc.getText();
          double price = Double.parseDouble(add_price.getText());
          try{
            Statement stmt = conn.createStatement();
            String sqlStatement = String.format("INSERT INTO menu_key VALUES (%d, \'%s\', \'%s\', %.2f) ON CONFLICT DO NOTHING;",item,name,desc,price);
            stmt.executeUpdate(sqlStatement);
            sqlStatement = "ALTER TABLE sales_list ADD COLUMN i_" + item + " INT DEFAULT 0;";
            stmt.executeUpdate(sqlStatement);
          } catch(Exception x){
            JOptionPane.showMessageDialog(null,"Error accessing Database." + x);
          }
          add_id.setText("");
          add_name.setText("");
          add_desc.setText("");
          add_price.setText("");
          edit_targ_name.addItem(name);
          rem_targ_name.addItem(name);
          refreshPanel();
        }
      });

      // definition for remove item button that deletes a row from menu_key based on an input name
      rem.addActionListener(new ActionListener(){
        public void actionPerformed(ActionEvent e){
          String sel = (String)rem_targ_name.getSelectedItem();
          try{
            Statement stmt = conn.createStatement();
            String sqlStatement = "DELETE FROM menu_key WHERE name = \'" + sel + "\';";
            //System.out.println(sqlStatement);
            stmt.executeUpdate(sqlStatement);
          } catch (Exception x){
            JOptionPane.showMessageDialog(null,"Error accessing Database." + x);
          }       
          edit_targ_name.removeItem(sel);
          rem_targ_name.removeItem(sel);
          refreshPanel();
        }
      });
    }

    //every time an item is added removed or edited this function gets called which clears the main panel and adds new data and re-adds preexisting components
    static public void refreshPanel(){
      edit_targ_name.removeAllItems();
      rem_targ_name.removeAllItems();
      pan.removeAll();
      pan.add(edit);
      pan.add(edit_targ_name);
      pan.add(edit_targ_col);
      pan.add(edit_in);
      pan.add(rem);
      pan.add(rem_targ_name);
      pan.add(new JLabel(""));
      pan.add(new JLabel(""));
      pan.add(new JLabel("Item", SwingConstants.CENTER));
      pan.add(new JLabel("Name", SwingConstants.CENTER));
      pan.add(new JLabel("Description", SwingConstants.CENTER));
      pan.add(new JLabel("Price", SwingConstants.CENTER));
      try{
        //create a statement object
        Statement stmt = conn.createStatement();
        //get all rows and cols
        String sqlStatement = "SELECT * FROM menu_key ORDER BY item asc;";
        ResultSet result = stmt.executeQuery(sqlStatement);
        while(result.next()){

          //this adds the item id number to the panel grid
          JLabel cell = new JLabel(String.valueOf(result.getInt("item")), SwingConstants.CENTER);
          cell.setBorder(BorderFactory.createLineBorder(Color.BLACK));
          pan.add(cell);

          //this adds the item name to the panel grid
          String obj = result.getString("name");
          edit_targ_name.addItem(obj);
          rem_targ_name.addItem(obj);
          cell = new JLabel(obj, SwingConstants.CENTER);
          cell.setBorder(BorderFactory.createLineBorder(Color.BLACK));
          pan.add(cell);

          //this adds the item description to the panel grid
          cell = new JLabel(result.getString("description"), SwingConstants.CENTER);
          cell.setBorder(BorderFactory.createLineBorder(Color.BLACK));
          pan.add(cell);

          //this adds the item price to the panel grid
          cell = new JLabel(String.format("$%.2f",result.getDouble("price")), SwingConstants.CENTER);
          cell.setBorder(BorderFactory.createLineBorder(Color.BLACK));
          pan.add(cell);
        }
      } catch (Exception e){
        JOptionPane.showMessageDialog(null,"Error accessing Database: " + e);
      }

    }

    //this is called when button 'b' is pressed and it closes the connection to the database and disposes of the frame
    public void actionPerformed(ActionEvent e){
        String s = e.getActionCommand();
        if (s.equals("Close")) {
            try {
              conn.close();
            } catch(Exception ex) {
              JOptionPane.showMessageDialog(null,"Connection NOT Closed.");
            }
            f.dispose();
        }
		}

	}