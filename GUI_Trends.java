import java.sql.*;
import java.awt.*;
import java.awt.BorderLayout;
import java.awt.event.*;
import javax.swing.*;

public class GUI_Trends extends JFrame implements ActionListener {
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
        GUI_Trends gui = new GUI_Trends();
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
        
        f.setLayout(new BorderLayout(20,15));
        f.add(b,BorderLayout.SOUTH);
        f.add(pan,BorderLayout.CENTER);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setSize(600,600);
        f.pack();
        f.setVisible(true);
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