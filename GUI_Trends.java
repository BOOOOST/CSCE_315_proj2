import java.sql.*;
import java.awt.*;
import java.awt.BorderLayout;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.plaf.basic.BasicTreeUI.TreeIncrementAction;

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
                //get the starting and ending dates from the text fields for set 1
                String start_date1 = inDate1.getText();
                String end_date1 = finDate1.getText();
                Vector<Float> set1 = getItemQuantities(start_date1,end_date1);
                float price1 = getTotalPrices(start_date1,end_date1);
                //Set 2
                String start_date2 = inDate2.getText();
                String end_date2 = finDate2.getText();
                Vector<Float> set2 = getItemQuantities(start_date2,end_date2);
                float price2 = getTotalPrices(start_date2,end_date2);
                //calculating the percents of each set
                Vector<Float> percent1 = new Vector<Float>();
                Vector<Float> percent2 = new Vector<Float>();
                //set1 and set2 should be equal in size
                for (int i = 0; i < set1.size(); i++){
                    //add the percent to set 1
                    percent1.add((set1.elementAt(i) / price1) * 100);
                    //add the percent to set 2
                    percent2.add((set2.elementAt(i) / price2) * 100);
                }
                //Create a table that displays the item number, % for set1, % for set2, %difference, column that displays Up/Down Trend
                Vector<String> columnNames = new Vector<String>();
                columnNames.add("Item");
                columnNames.add("Set 1 %");
                columnNames.add("Set 2 %");
                columnNames.add("% Difference");
                columnNames.add("Trend");
                //Get the list of item names from menu
                Vector<String> itemName = new Vector<String>();
                try{
                    Statement stmt = conn.createStatement();
                    String sqlStatement = "SELECT * FROM menu_key ORDER BY item asc;";
                    ResultSet result = stmt.executeQuery(sqlStatement);
                    while(result.next()){
                        itemName.add(result.getString("name"));
                    }
                }
                catch (Exception y){
                    JOptionPane.showMessageDialog(null,"Error accessing Database.");
                }
                Vector<Vector<String>> data = new Vector<Vector<String>>();
                //adding the info to a data 2d vector
                float percentDiff = 0;
                String trend;
                for (int i = 0; i < set1.size(); i++){
                    Vector<String> rowData = new Vector<String>();
                    percentDiff = percent2.elementAt(i) - percent1.elementAt(i);
                    if (percentDiff <= 0){
                        trend = "Down";
                    }
                    else{
                        trend = "Up";
                    }
                    rowData.add(itemName.elementAt(i));
                    rowData.add(String.valueOf(percent1.elementAt(i)));
                    rowData.add(String.valueOf(percent2.elementAt(i)));
                    rowData.add(String.valueOf(percentDiff));
                    rowData.add(trend);
                    data.add(rowData);
                }
                JTable table = new JTable(data,columnNames);
                JScrollPane scrollPane = new JScrollPane(table);
                table.setFillsViewportHeight(true);
                f.add(scrollPane,BorderLayout.CENTER);
                f.pack();
                f.setVisible(true);
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
        f.add(pan,BorderLayout.NORTH);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setSize(600,600);
        f.pack();
        f.setVisible(true);
    }
    //get the total price of the items from the dates
    static Float getTotalPrices(String initDate, String endDate){
        float totPrice = 0;
        try{
            Statement stmt = conn.createStatement();
            //make sql select statement for rows inbetween the two dates
            //SELECT * FROM sales_list WHERE date BETWEEN '02/18/2022' AND '02/20/2022' ORDER BY date asc;
            String sqlStatement = "SELECT * FROM sales_list WHERE date BETWEEN \'" + initDate + "\' AND \'" + endDate + "\' ORDER BY date asc;";
            //System.out.println(sqlStatement);
            ResultSet result = stmt.executeQuery(sqlStatement);
            while(result.next()){
                totPrice += Float.parseFloat(result.getString("total_sales"));
            }
            
            return totPrice;
        }
        catch (Exception y){
            JOptionPane.showMessageDialog(null,"Error accessing Database. (GetItemQuantities)");
        }
        return totPrice;
    }
    //returns a list of the item quantity totals multiplied with their menu prices within a set of dates
    static Vector<Float> getItemQuantities(String initDate, String endDate){
        Vector<Integer> itemQuans = new Vector<Integer>();
        Vector<Float> ans = new Vector<Float>();
        try{
            Statement stmt = conn.createStatement();
            //make sql select statement for rows inbetween the two dates
            //SELECT * FROM sales_list WHERE date BETWEEN '02/18/2022' AND '02/20/2022' ORDER BY date asc;
            String sqlStatement = "SELECT * FROM sales_list WHERE date BETWEEN \'" + initDate + "\' AND \'" + endDate + "\' ORDER BY date asc;";
            //System.out.println(sqlStatement);
            ResultSet result = stmt.executeQuery(sqlStatement);
            //Retrieving the ResultSetMetaData object
            ResultSetMetaData rsmd = result.getMetaData();
            //getting the column type
            int column_count = rsmd.getColumnCount();
            int quantityToAdd = 0;
            int itemNum = 501;
            int maxNum = 501 + column_count - 3;
            //loop through the dates and get the total of each item to put in a list
            for (int i = itemNum; i < maxNum; i++){
                result = stmt.executeQuery(sqlStatement);
                while(result.next()){
                    quantityToAdd += Integer.parseInt(result.getString("i_" + i));
                }
                itemQuans.add(quantityToAdd);
                quantityToAdd = 0;
            }
            sqlStatement = "SELECT * FROM menu_key ORDER BY item asc;";
            Vector<Float> menuPrices = new Vector<Float>();
            result = stmt.executeQuery(sqlStatement);
            while(result.next()){
                menuPrices.add(Float.parseFloat(result.getString("price")));
            }
            ans = new Vector<Float>();
            for (int i = 0; i < menuPrices.size(); i++){
                ans.add(menuPrices.elementAt(i) * itemQuans.elementAt(i));
            }
            return ans;
        }
        catch (Exception y){
            JOptionPane.showMessageDialog(null,"Error accessing Database. (GetItemQuantities)");
        }
        return ans;
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