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
      
      ManagerGUI gui = new ManagerGUI();
      f = new JFrame("Manager Menu View");


      pan = new JPanel();
      pan.setLayout(new GridLayout(0,4));

      JButton b = new JButton("Close");
      b.addActionListener(gui);
      
      //initialize add item button components
      JButton add = new JButton("Add Item");
      JTextField add_id = new JTextField();
      JTextField add_name = new JTextField();
      JTextField add_desc = new JTextField();
      JTextField add_price = new JTextField();
      add.addActionListener(new ActionListener(){
        public void actionPerformed(ActionEvent e){

        }
      });

      //initialize edit item button components
      JButton edit = new JButton("Edit Item");
      JComboBox edit_targ_name = new JComboBox();
      Vector<String> col_names = new Vector<String>();
      col_names.add("item");
      col_names.add("name");
      col_names.add("description");
      col_names.add("price");
      JComboBox edit_targ_col = new JComboBox(col_names);
      JTextField edit_in = new JTextField();

      //initialize remove item button components
      JButton rem = new JButton("Remove Item");
      rem.addActionListener(new ActionListener(){
        public void actionPerformed(ActionEvent e){

        }
      });
      pan.add(edit);
      pan.add(edit_targ_name);
      pan.add(edit_targ_col);
      pan.add(edit_in);
      pan.add(rem);
      pan.add(add);
      pan.add(new JLabel(""));
      pan.add(new JLabel(""));
      pan.add(new JLabel("Item", SwingConstants.CENTER));
      pan.add(new JLabel("Name", SwingConstants.CENTER));
      pan.add(new JLabel("Description", SwingConstants.CENTER));
      pan.add(new JLabel("Price", SwingConstants.CENTER));
      
      Vector<String> item_names = new Vector<String>();
      try{
        //create a statement object
        Statement stmt = conn.createStatement();
        //get all item names
        String sqlStatement = "SELECT name FROM menu_key;";
        ResultSet result = stmt.executeQuery(sqlStatement);
        while(result.next()){
          item_names.add(result.getString("name"));
        }
        //get all rows and cols
        sqlStatement = "SELECT * FROM menu_key;";
        result = stmt.executeQuery(sqlStatement);
        while(result.next()){
          JLabel cell = new JLabel(String.valueOf(result.getInt("item")), SwingConstants.CENTER);
          cell.setBorder(BorderFactory.createLineBorder(Color.BLACK));
          pan.add(cell);

          String obj = result.getString("name");
          item_names.add(obj);
          edit_targ_name.addItem(obj);
          cell = new JLabel(obj, SwingConstants.CENTER);
          cell.setBorder(BorderFactory.createLineBorder(Color.BLACK));
          pan.add(cell);

          cell = new JLabel(result.getString("description"), SwingConstants.CENTER);
          cell.setBorder(BorderFactory.createLineBorder(Color.BLACK));
          pan.add(cell);

          cell = new JLabel(String.format("$%.2f",result.getDouble("price")), SwingConstants.CENTER);
          cell.setBorder(BorderFactory.createLineBorder(Color.BLACK));
          pan.add(cell);
        }
        
      } catch (Exception e){
        JOptionPane.showMessageDialog(null,"Error accessing Database: " + e);
      }

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
        }
      });

      f.setLayout(new BorderLayout(20,15));
      f.add(b);
      f.add(pan,BorderLayout.CENTER);
      f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      f.setSize(600,600);
      f.pack();
      f.setVisible(true);
    }

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