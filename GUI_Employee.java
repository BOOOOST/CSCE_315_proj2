import java.sql.*;
import java.awt.event.*;
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

      // create a object
      GUI_Employee s = new GUI_Employee();

      // create a panel
      JPanel p = new JPanel();

      JButton b = new JButton("Close");

      // add actionlistener to button
      b.addActionListener(s);

      //TODO Step 3 
      JComboBox t = new JComboBox(menu);
      t.setEditable(false);

      //TODO Step 4
      p.add(t, BorderLayout.CENTER);

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

    // if button is pressed
    public void actionPerformed(ActionEvent e)
    {
        String s = e.getActionCommand();
        if (s.equals("Close")) {
            f.dispose();
        }
    }
}