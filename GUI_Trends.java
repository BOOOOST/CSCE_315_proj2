import java.sql.*;
import java.awt.*;
import java.awt.BorderLayout;
import java.awt.event.*;

import javax.naming.ldap.BasicControl;
import javax.naming.spi.DirStateFactory.Result;
import javax.swing.*;
import javax.swing.border.Border;
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

        BorderLayout layout = new BorderLayout(20,15);
        JLabel init_date1 = new JLabel("Initial Date Set 1: ");
        JLabel final_date1 = new JLabel("Final Date Set 1: ");
        JTextField inDate1 = new JTextField("(mm/dd/yyyy)");
        JTextField finDate1 = new JTextField("(mm/dd/yyyy)");
        JLabel init_date2 = new JLabel("Initial Date Set 2: ");
        JLabel final_date2 = new JLabel("Final Date Set 2: ");
        JTextField inDate2 = new JTextField("(mm/dd/yyyy)");
        JTextField finDate2 = new JTextField("(mm/dd/yyyy)");

        //Make buttons for inventory usage/popularity
        JButton invUsageButton = new JButton("Inventory Usage (Use Date Set 1)");
        JButton invPopButton = new JButton("Inventory Popularity (Use Date Set 1)");

        //create inventory usage table when invUsageButton or invPopButton are pressed
        ActionListener invButton = new ActionListener(){
            public void actionPerformed(ActionEvent e){
                //gets date window from date set 1 and gets the number of each menu item ordered within the date window
                String start = inDate1.getText();
                String end = finDate1.getText();
                Vector<Integer> numItems = getNumItems(start, end);
                //Create a table that displays the inventory item name, and the quantity of that item that has been used
                Vector<String> colNames = new Vector<String>();
                colNames.add("Inventory Item");
                colNames.add("Qantity Used");
                //get every inventory item name
                Vector<String> invItemNames = new Vector<String>();
                int numDays = 0;
                try{
                    //get all food inventory item names
                    Statement stmt = conn.createStatement();
                    String sqlStatement = "SELECT * FROM inventory WHERE type = \'Food\' ORDER BY name asc;";
                    ResultSet res = stmt.executeQuery(sqlStatement);
                    while(res.next()){
                        invItemNames.add(res.getString("name"));
                    }
                    //get all non food inventory item names
                    sqlStatement = "SELECT * FROM inventory WHERE NOT type = \'Food\' ORDER BY name asc;";
                    res = stmt.executeQuery(sqlStatement);
                    while(res.next()){
                        invItemNames.add(res.getString("name"));
                    }
                    //get number of days long of the window
                    sqlStatement = "SELECT * FROM sales_list WHERE date BETWEEN \'" + start + "\' AND \'" + end + "\' ORDER BY date asc;";
                    res = stmt.executeQuery(sqlStatement);
                    while(res.next()){
                        numDays = res.getRow();
                    }
                    //System.out.println(numDays);
                } catch (Exception x){
                    JOptionPane.showMessageDialog(null,"Error accessing Database." + x);
                }
                // Calculate all amounts of inventory items used based on how many of each food item was ordered
                int totalTenders = 5*numItems.elementAt(0) + 4*numItems.elementAt(1) + 3*numItems.elementAt(2) + 2*numItems.elementAt(3)
                + 20*numItems.elementAt(5) + 3*(numItems.elementAt(6) + numItems.elementAt(7) + numItems.elementAt(8) + numItems.elementAt(9))
                + numItems.elementAt(13) + 5*numItems.elementAt(19);
                int totalFries = numItems.elementAt(0) + numItems.elementAt(1) + numItems.elementAt(2) + numItems.elementAt(3)
                + 4*numItems.elementAt(5) + numItems.elementAt(6) + numItems.elementAt(8) + numItems.elementAt(10) + numItems.elementAt(16) + numItems.elementAt(19);
                int totalBread = numItems.elementAt(0) + numItems.elementAt(1) + numItems.elementAt(2) + numItems.elementAt(3) + 4*numItems.elementAt(5)
                + 3*(numItems.elementAt(6) + numItems.elementAt(8) + numItems.elementAt(10)) + 2*(numItems.elementAt(7) + numItems.elementAt(9) + numItems.elementAt(11))
                + numItems.elementAt(14) + numItems.elementAt(19);
                //food item usage
                Vector<Float> itemUsages = new Vector<Float>();
                float baconUsed = (float)0.01 * (numItems.elementAt(6) + numItems.elementAt(7)); itemUsages.add(baconUsed);
                float pepperUsed = (float)0.000625*totalTenders; itemUsages.add(pepperUsed);
                float chickenUsed = (float)0.09375*totalTenders; itemUsages.add(chickenUsed);
                float flourUsed = (float)0.0025*totalTenders; itemUsages.add(flourUsed);
                float friesUsed = (float)0.0167*totalFries; itemUsages.add(friesUsed);
                float oilUsed = (float)0.01557*(totalFries + totalTenders); itemUsages.add(oilUsed);
                float garlicUsed = (float)0.00125*totalTenders; itemUsages.add(garlicUsed);
                float impChicken = (float)0.09375*numItems.elementAt(19); itemUsages.add(impChicken);
                float lgKetContainerUsed = (float).1*numDays; itemUsages.add(lgKetContainerUsed);
                float ketPacketsUsed = (float)0.25*numDays; itemUsages.add(ketPacketsUsed);
                float liquidMargUsed = (float)0.005*totalBread; itemUsages.add(liquidMargUsed);
                float mayoUsed = (float).0025*numItems.elementAt(4); itemUsages.add(mayoUsed);
                float potSaladUsed = (float)0.06875*(numItems.elementAt(0) + numItems.elementAt(1) + numItems.elementAt(2) + numItems.elementAt(3) + numItems.elementAt(6)
                + numItems.elementAt(8) + numItems.elementAt(10) + numItems.elementAt(15) + + numItems.elementAt(19)); itemUsages.add(potSaladUsed);
                float ranchUsed = (float).0025*numItems.elementAt(4); itemUsages.add(ranchUsed);
                float saltUsed = numDays; itemUsages.add(saltUsed);
                float cheeseUsed = (float)0.0021*(numItems.elementAt(6) + numItems.elementAt(7) + numItems.elementAt(10) + numItems.elementAt(11)); itemUsages.add(cheeseUsed);
                float teaSugarUsed = (float)0.021*numItems.elementAt(4); itemUsages.add(teaSugarUsed);
                float teaBagsUsed = (float)0.0417*numItems.elementAt(4); itemUsages.add(teaBagsUsed);
                float breadUsed = totalBread/170; itemUsages.add(breadUsed);
                float worceSauceUsed = (float).0025*numItems.elementAt(4); itemUsages.add(worceSauceUsed);
                //non food item usage
                float tenOzCup = (float)0.1*numDays; itemUsages.add(tenOzCup);
                float sixteenOzCup = (float)(numItems.elementAt(17) + numItems.elementAt(0) + numItems.elementAt(1) + numItems.elementAt(2) + numItems.elementAt(3)
                + numItems.elementAt(6) + numItems.elementAt(8) + numItems.elementAt(10) + numItems.elementAt(19))/1000;  itemUsages.add(sixteenOzCup);
                float twoOzCup = (float)(numItems.elementAt(12) + numItems.elementAt(0) + numItems.elementAt(1) + numItems.elementAt(2) + numItems.elementAt(3)
                + numItems.elementAt(6) + numItems.elementAt(8) + numItems.elementAt(10) + numItems.elementAt(19) + 8*numItems.elementAt(5))/2500; itemUsages.add(twoOzCup);
                float twoOzLid = (float)twoOzCup; itemUsages.add(twoOzLid);
                float fiveOzCup = (float)(numItems.elementAt(15) + numItems.elementAt(0) + numItems.elementAt(1) + numItems.elementAt(2) + numItems.elementAt(3)
                + numItems.elementAt(6) + numItems.elementAt(8) + numItems.elementAt(10) + numItems.elementAt(19))/2500; itemUsages.add(fiveOzCup);
                float fiveOzLid = fiveOzCup; itemUsages.add(fiveOzLid);
                float basketLiners = (float)(numItems.elementAt(0) + numItems.elementAt(1) + numItems.elementAt(2) + numItems.elementAt(3)
                + numItems.elementAt(6) + numItems.elementAt(8) + numItems.elementAt(10) + numItems.elementAt(19))/6000; itemUsages.add(basketLiners);
                float berryLemonade = (float)0.0417*(numItems.elementAt(18))/4; itemUsages.add(berryLemonade);
                float creamSoda = berryLemonade; itemUsages.add(creamSoda);
                float orangeNCream = berryLemonade; itemUsages.add(orangeNCream);
                float rootBeer = berryLemonade; itemUsages.add(rootBeer);
                float cutlery = (float)0.01*numDays; itemUsages.add(cutlery);
                float dishSoap = (float)numDays/24; itemUsages.add(dishSoap);
                float drinkLids = sixteenOzCup; itemUsages.add(drinkLids);
                float floorCleaner = (float)numDays/90; itemUsages.add(floorCleaner);
                float gloves = (float)0.1*numDays; itemUsages.add(gloves);
                float handSanitizer = (float)numDays/8; itemUsages.add(handSanitizer);
                float handSoap = (float)numDays/8; itemUsages.add(handSoap);
                float hingedLidBoxLg = (float)0.005*(basketLiners/3); itemUsages.add(hingedLidBoxLg);
                float hingedLidBoxSm = (float)0.0022*(basketLiners/3); itemUsages.add(hingedLidBoxSm);
                float cola = sixteenOzCup/6; itemUsages.add(cola);
                float drJones = sixteenOzCup/6; itemUsages.add(drJones);
                float lemonLime = sixteenOzCup/6; itemUsages.add(lemonLime);
                float orangeNCreamBIB = sixteenOzCup/6; itemUsages.add(orangeNCreamBIB);
                float rootBeerBIB = sixteenOzCup/6; itemUsages.add(rootBeerBIB);
                float sugarFreeCola = sixteenOzCup/6; itemUsages.add(sugarFreeCola);
                float napkin = (float)numDays/30; itemUsages.add(napkin);
                float paperTowels = (float)numDays/60; itemUsages.add(paperTowels);
                float straws = sixteenOzCup; itemUsages.add(straws);
                float surfaceCleaner = (float)numDays/24; itemUsages.add(surfaceCleaner);
                float toiletPaper = (float)numDays*2; itemUsages.add(toiletPaper);
                float trashCanLinerLg = (float)numDays/2; itemUsages.add(trashCanLinerLg);
                float trashCanLinerSm = (float)numDays*3; itemUsages.add(trashCanLinerSm);
                //System.out.println(invItemNames.size() + "item names and " + itemUsages.size() + " items");

                Object which = e.getSource();
                Vector<Vector<String>> data = new Vector<Vector<String>>();
                if(which == invPopButton){
                    //loop through the lists and add the info to the 2d Vector
                    int x = 0;
                    while (x < itemUsages.size()){
                        Vector<String> rowData = new Vector<String>();
                        int index = getMaxFloat(itemUsages);
                        rowData.add(invItemNames.elementAt(index));
                        rowData.add(String.valueOf(itemUsages.elementAt(index)));
                        invItemNames.remove(invItemNames.elementAt(index));
                        itemUsages.remove(itemUsages.elementAt(index));
                        data.add(rowData);
                    }
                } else {
                    for(int i = 0; i < itemUsages.size();i++){
                        Vector<String> rowData = new Vector<String>();
                        rowData.add(invItemNames.elementAt(i));
                        rowData.add(String.valueOf(itemUsages.elementAt(i)));
                        data.add(rowData);
                    }
                }
                //create a table to add to the frame
                JTable table = new JTable(data,colNames);
                JScrollPane scrollPane1 = new JScrollPane(table);
                //Set it so the table will sort if you click on the column name (clicking on the trend and % diff should be good for the demo)
                //table.setAutoCreateRowSorter(true);
                table.setFillsViewportHeight(true);
                //add table to frame, this should update the table everytime the button is pressed too
                if (layout.getLayoutComponent(BorderLayout.CENTER) != null){
                    f.remove(layout.getLayoutComponent(BorderLayout.CENTER));
                }
                f.add(scrollPane1,BorderLayout.CENTER);
                f.pack();
                f.setVisible(true);
            }
        };
        //give both inventory buttons this function when pressed
        invPopButton.addActionListener(invButton);
        invUsageButton.addActionListener(invButton);

        //Make button for ordering trends
        JButton orderTrendButton = new JButton("Order Trends (Use Both Date Sets)");
        JButton orderPopButton = new JButton("Order Popularity (Use Date Set 1)");
        //create popularity table of items when Ordering pop button is pressed
        orderPopButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                //get the starting and ending dates from the text fields for set 1
                String start_date1 = inDate1.getText();
                String end_date1 = finDate1.getText();
                Vector<Integer> numItems = getNumItems(start_date1, end_date1);
                //Create a table that displays the item number, % for set1, % for set2, %difference, column that displays Up/Down Trend
                Vector<String> columnNames = new Vector<String>();
                columnNames.add("Item");
                columnNames.add("Number of Orders");
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
                //loop through the lists and add the info to the 2d Vector
                int x = 0;
                while (x < numItems.size()){
                    Vector<String> rowData = new Vector<String>();
                    int index = getMaxInt(numItems);
                    rowData.add(itemName.elementAt(index));
                    rowData.add(String.valueOf(numItems.elementAt(index)));
                    itemName.remove(itemName.elementAt(index));
                    numItems.remove(numItems.elementAt(index));
                    data.add(rowData);
                }


                ///create a table to add to the frame
                JTable table = new JTable(data,columnNames);
                JScrollPane scrollPane1 = new JScrollPane(table);
                //Set it so the table will sort if you click on the column name (clicking on the trend and % diff should be good for the demo)
                //table.setAutoCreateRowSorter(true);
                table.setFillsViewportHeight(true);
                //add table to frame, this should update the table everytime the button is pressed too
                if (layout.getLayoutComponent(BorderLayout.CENTER) != null){
                    f.remove(layout.getLayoutComponent(BorderLayout.CENTER));
                }
                f.add(scrollPane1,BorderLayout.CENTER);
                f.pack();
                f.setVisible(true);
            }
            });
        //create trend table when Ordering Trends button is pressed
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
                String trend;
                Vector<Float> percentDiff = new Vector<Float>();
                for (int i = 0; i < percent1.size(); i++){
                    percentDiff.add(percent2.elementAt(i) - percent1.elementAt(i));
                }
                //loop through the lists and add the info to the 2d Vector
                while (0 < percentDiff.size()){
                    Vector<String> rowData = new Vector<String>();
                    int index = getMaxFloat(percentDiff);
                    if (percentDiff.elementAt(index) <= 0){
                        trend = "Down";
                    }
                    else{
                        trend = "Up";
                    }
                    rowData.add(itemName.elementAt(index));
                    rowData.add(String.valueOf(percent1.elementAt(index)));
                    rowData.add(String.valueOf(percent2.elementAt(index)));
                    rowData.add(String.valueOf(percentDiff.elementAt(index)));
                    rowData.add(trend);
                    data.add(rowData);
                    itemName.remove(itemName.elementAt(index));
                    percent1.remove(percent1.elementAt(index));
                    percent2.remove(percent2.elementAt(index));
                    percentDiff.remove(percentDiff.elementAt(index));
                }
                ///create a table to add to the frame
                JTable table = new JTable(data,columnNames);
                JScrollPane scrollPane = new JScrollPane(table);
                //Set it so the table will sort if you click on the column name (clicking on the trend and % diff should be good for the demo)
                //table.setAutoCreateRowSorter(true);
                table.setFillsViewportHeight(true);
                //add table to frame, this should update the table everytime the button is pressed too
                if (layout.getLayoutComponent(BorderLayout.CENTER) != null){
                    f.remove(layout.getLayoutComponent(BorderLayout.CENTER));
                }
                f.add(scrollPane,BorderLayout.CENTER);
                f.pack();
                f.setVisible(true);
            }
            });

        //adding all of the components needed for the trends
        f.setLayout(layout);
        f.add(b,BorderLayout.SOUTH);
        pan.add(orderTrendButton, BorderLayout.CENTER);
        pan.add(init_date1, BorderLayout.CENTER);
        pan.add(inDate1, BorderLayout.CENTER);
        pan.add(final_date1,BorderLayout.CENTER);
        pan.add(finDate1, BorderLayout.CENTER);
        pan.add(orderPopButton, BorderLayout.CENTER);
        pan.add(init_date2, BorderLayout.CENTER);
        pan.add(inDate2, BorderLayout.CENTER);
        pan.add(final_date2,BorderLayout.CENTER);
        pan.add(finDate2, BorderLayout.CENTER);
        pan.add(invPopButton, BorderLayout.CENTER);
        pan.add(invUsageButton, BorderLayout.CENTER);
        f.add(pan,BorderLayout.NORTH);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setSize(600,600);
        f.pack();
        f.setVisible(true);
    }
    //retursns the index of the max in the vector
    static int getMaxInt(Vector<Integer> intList){
        int max = intList.elementAt(0);
        int index = 0;
        for (int i = 0; i < intList.size(); i++){
            if (intList.elementAt(i) > max){
                max = intList.elementAt(i);
                index = i;
            }
        }
        return index;
    }
    //retursns the index of the max in the vector
    static int getMaxFloat(Vector<Float> intList){
        float max = intList.elementAt(0);
        int index = 0;
        for (int i = 0; i < intList.size(); i++){
            if (intList.elementAt(i) > max){
                max = intList.elementAt(i);
                index = i;
            }
        }
        return index;
    }
    //get the number of items for each menu item bewtween two dates
    static Vector<Integer> getNumItems(String initDate, String endDate){
        Vector<Integer> itemQuans = new Vector<Integer>();
        try{
            Statement stmt = conn.createStatement();
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
            return itemQuans;
        }
        catch (Exception y){
            JOptionPane.showMessageDialog(null,"Error accessing Database. (GetItemQuantities)");
        }
        return itemQuans;
    }
    //get the total price of the items from the dates
    static Float getTotalPrices(String initDate, String endDate){
        float totPrice = 0;
        try{
            Statement stmt = conn.createStatement();
            String sqlStatement = "SELECT * FROM sales_list WHERE date BETWEEN \'" + initDate + "\' AND \'" + endDate + "\' ORDER BY date asc;";
            ResultSet result = stmt.executeQuery(sqlStatement);
            //Just get the prices inbetween the two dates and add them up
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
        //list of the number of orders each menu item has
        Vector<Integer> itemQuans = new Vector<Integer>();
        //returning ans vector
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
            //Get menu prices to multiply with itemQuans list to get ans vecotr
            sqlStatement = "SELECT * FROM menu_key ORDER BY item asc;";
            Vector<Float> menuPrices = new Vector<Float>();
            result = stmt.executeQuery(sqlStatement);
            while(result.next()){
                menuPrices.add(Float.parseFloat(result.getString("price")));
            }
            //multiply the total number of orders each item menu has with its corresponding menu price to get the total revenue for that menu item
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