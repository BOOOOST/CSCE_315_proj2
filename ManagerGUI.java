import java.sql.*;
import java.awt.*;
import java.awt.BorderLayout;
import java.awt.event.*;
import javax.swing.*;

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
      
      pan = new JPanel();
      pan.setLayout(new GridLayout(0,4));
      pan.add(new JLabel("Item", SwingConstants.CENTER));
      pan.add(new JLabel("Name", SwingConstants.CENTER));
      pan.add(new JLabel("Description", SwingConstants.CENTER));
      pan.add(new JLabel("Price", SwingConstants.CENTER));
      try{
        //create a statement object
        Statement stmt = conn.createStatement();
        //get number of columns
        String sqlStatement = "SELECT * FROM menu_key;";
        ResultSet result = stmt.executeQuery(sqlStatement);
        while(result.next()){
          JLabel cell = new JLabel(String.valueOf(result.getInt("item")), SwingConstants.CENTER);
          cell.setBorder(BorderFactory.createLineBorder(Color.BLACK));
          pan.add(cell);
          cell = new JLabel(result.getString("name"), SwingConstants.CENTER);
          cell.setBorder(BorderFactory.createLineBorder(Color.BLACK));
          pan.add(cell);
          cell = new JLabel(result.getString("description"), SwingConstants.CENTER);
          cell.setBorder(BorderFactory.createLineBorder(Color.BLACK));
          pan.add(cell);
          cell = new JLabel(String.format("%.2f$",result.getDouble("price")), SwingConstants.CENTER);
          cell.setBorder(BorderFactory.createLineBorder(Color.BLACK));
          pan.add(cell);
        }
        
      } catch (Exception e){
        JOptionPane.showMessageDialog(null,"Error accessing Database: " + e);
      }
      ManagerGUI gui = new ManagerGUI();

      f = new JFrame("Manager Menu View");

      JButton b = new JButton("Close");
      b.addActionListener(gui);

      
      pan.add(b);
      
      f.add(pan,BorderLayout.CENTER);
      f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      f.setSize(400,400);
      f.pack();
      f.setVisible(true);
    }

    public void actionPerformed(ActionEvent e){
        String s = e.getActionCommand();
        if (s.equals("Close")) {
            f.dispose();
            try {
              conn.close();
              JOptionPane.showMessageDialog(null,"Connection Closed.");
            } catch(Exception ex) {
              JOptionPane.showMessageDialog(null,"Connection NOT Closed.");
            }
        }
		}

	}