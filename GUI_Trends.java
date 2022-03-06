import java.sql.*;
import java.awt.*;
import java.awt.BorderLayout;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
// import java.util.Date;
// import java.time.format.*;
// import java.text.DateFormat;
// import java.text.SimpleDateFormat;

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
        pan.setLayout(new GridLayout(0,5));

        // initialize the close button and add give it an action listener
        JButton b = new JButton("Close");
        b.addActionListener(gui);

        JLabel init_date1 = new JLabel("Initial Date Set 1: ");
        JLabel final_date1 = new JLabel("Final Date Set 1: ");
        JTextField inDate1 = new JTextField("(mm/dd/yyyy)");
        JTextField finDate1 = new JTextField("(mm/dd/yyyy)");
        JLabel init_date2 = new JLabel("Initial Date Set 2: ");
        JLabel final_date2 = new JLabel("Final Date Set 2: ");
        JTextField inDate2 = new JTextField("(mm/dd/yyyy)");
        JTextField finDate2 = new JTextField("(mm/dd/yyyy)");

        //Make button for ordering trends
        JButton orderTrendButton = new JButton("Ordering Trends");
        //create trend grpah when Ordering Trends button is pressed
        orderTrendButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                //get the starting and ending dates from the text fields
                String start_date1 = inDate1.getText();
                String end_date1 = finDate1.getText();
                Vector<Integer> set1 = getItemQuantities(start_date1,end_date1);
                int test = 501;
                for (int i = 0; i < set1.size(); i++){
                    System.out.println("Item " + test + ": " + set1.elementAt(i));
                    test++;
                }
            }
            });

        
        f.setLayout(new BorderLayout(20,15));
        f.add(b,BorderLayout.SOUTH);
        pan.add(orderTrendButton, BorderLayout.CENTER);
        pan.add(init_date1, BorderLayout.CENTER);
        pan.add(inDate1, BorderLayout.CENTER);
        pan.add(final_date1,BorderLayout.CENTER);
        pan.add(finDate1, BorderLayout.CENTER);
        pan.add(init_date2, BorderLayout.CENTER);
        pan.add(inDate2, BorderLayout.CENTER);
        pan.add(final_date2,BorderLayout.CENTER);
        pan.add(finDate2, BorderLayout.CENTER);
        f.add(pan,BorderLayout.CENTER);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setSize(600,600);
        f.pack();
        f.setVisible(true);
    }
    //returns a list of the item quantity totals within a set of dates
    static Vector<Integer> getItemQuantities(String initDate, String endDate){
        Vector<Integer> itemQuans = new Vector<Integer>();
        try{
            Statement stmt = conn.createStatement();
            //make sql select statement for rows inbetween the two dates
            String sqlStatement = "SELECT * FROM sales_list WHERE date BETWEEN \'" + initDate + "\' AND \'" + endDate + "\';";
            System.out.println(sqlStatement);
            ResultSet result = stmt.executeQuery(sqlStatement);
            //Retrieving the ResultSetMetaData object
            ResultSetMetaData rsmd = result.getMetaData();
            //getting the column type
            int column_count = rsmd.getColumnCount();
            int quantityToAdd = 0;
            int itemNum = 501;
            int maxNum = 501 + column_count - 3;
            Vector<Vector<Integer>> itemList = new Vector<Vector<Integer>>();
            for (int i = itemNum; i < maxNum; i++){
                itemList.add(new Vector<Integer>());
            }
            
            /*
            while (result.next()) {
                for (int i = itemNum; i < maxNum; i++){
                    itemQuans.add(Integer.parseInt(result.getString("i_" + itemNum)));
                }
                itemList.add(itemQuans);
                itemQuans.clear();
                itemNum++;
            }
            for (int i = 0; i < itemList.size(); i++)
            {
                int sum = 0;
                for (int j = 0; j < itemList.elementAt(i).size(); j++){
                    sum += itemList.elementAt(i).elementAt(j);
                }
                itemQuans.add(sum);
            }
            */
            return itemQuans;
        }
        catch (Exception y){
            JOptionPane.showMessageDialog(null,"Error accessing Database. (GetItemQuantities)");
        }
        return itemQuans;
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