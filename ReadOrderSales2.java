import java.io.*;  
import java.util.Scanner;
import java.sql.*;
import java.util.Calendar;
//Note that this code is for our second week of sales
public class ReadOrderSales2{  
    //This function, given an integer from 1 to 7, will select the date that the order took place on. As we are only reading one week of sales with this file,
    //the days are hard-coded into it
    private static String PickDay(int num){
        String date = "";
        switch(num){
            case 1: date = "02/20/2022";
            break;
            case 2: date = "02/21/2022";
            break;
            case 3: date = "02/22/2022";
            break;
            case 4: date = "02/23/2022";
            break;
            case 5: date = "02/24/2022";
            break;
            case 6: date = "02/25/2022";
            break;
            case 7: date = "02/26/2022";
            break;
        }
        return date;
    }
    public static void main(String[] args) throws Exception {  
      //parsing a CSV file into Scanner class constructor
      Connection conn = null;
     String teamNumber = "16";
     String sectionNumber = "903";
     String dbName = "csce315" + sectionNumber + "_" + teamNumber + "db";
     String dbConnectionString = "jdbc:postgresql://csce-315-db.engr.tamu.edu/" + dbName;
     String userName = "csce315" + sectionNumber + "_" + teamNumber + "user";
     String userPassword = "prjj1234";

    //Connecting to the database
    try {
        conn = DriverManager.getConnection(dbConnectionString,userName, userPassword);
     } catch (Exception e) {
        e.printStackTrace();
        System.err.println(e.getClass().getName()+": "+e.getMessage());
        System.exit(0);
     }

     System.out.println("Opened database successfully");
     try{
       //create a statement object
       Statement stmt = conn.createStatement();

      Scanner sc = new Scanner(new File("SecondWeekSales.csv")).useDelimiter(",");
      //Instantiate each of the 19 menu items and their crresponding quantities to read from the file
      int Item1, Item2, Item3, Item4, Item5, Item6, Item7, Item8, Item9, Item10, Item11, Item12, Item13, Item14, Item15, Item16, Item17, Item18, Item19;
      int quan1, quan2, quan3, quan4, quan5, quan6, quan7, quan8, quan9, quan10, quan11, quan12, quan13, quan14, quan15, quan16, quan17, quan18, quan19;
      //Set current date as a string for now, may change it to the Date sql data type if need be
      String currDate;
      //day of the week that the sales took place in (i.e. Monday, Tuesday)
      String dayOfWeek;
      //Total ammount of money in sales that happened in one day
      double Price = 0;
	  sc.nextLine();
      int dayCount = 1;
      while (sc.hasNext()){
          //use PickDay function to get the date
            currDate = ReadOrderSales2.PickDay(dayCount);
            //read in day of the week, then go to next line
			dayOfWeek = sc.next();
            sc.nextLine();
            //The pattern goes like this for each day: read the Item number and its corresponding quantity, then go to next line.
            //Repeat untill all 19 menu items are correctly read
            Item1 = sc.nextInt();
            quan1 = sc.nextInt();
            sc.nextLine();
            Item2 = sc.nextInt();
            quan2 = sc.nextInt();
            sc.nextLine();
            Item3 = sc.nextInt();
            quan3 = sc.nextInt();
            sc.nextLine();
            Item4 = sc.nextInt();
            quan4 = sc.nextInt();
            sc.nextLine();
            Item5 = sc.nextInt();
            quan5 = sc.nextInt();
            sc.nextLine();
            Item6 = sc.nextInt();
            quan6 = sc.nextInt();
            sc.nextLine();
            Item7 = sc.nextInt();
            quan7 = sc.nextInt();
            sc.nextLine();
            Item8 = sc.nextInt();
            quan8 = sc.nextInt();
            sc.nextLine();
            Item9 = sc.nextInt();
            quan9 = sc.nextInt();
            sc.nextLine();
            Item10 = sc.nextInt();
            quan10 = sc.nextInt();
            sc.nextLine();
            Item11 = sc.nextInt();
            quan11 = sc.nextInt();
            sc.nextLine();
            Item12 = sc.nextInt();
            quan12 = sc.nextInt();
            sc.nextLine();
            Item13 = sc.nextInt();
            quan13 = sc.nextInt();
            sc.nextLine();
            Item14 = sc.nextInt();
            quan14 = sc.nextInt();
            sc.nextLine();
            Item15 = sc.nextInt();
            quan15 = sc.nextInt();
            sc.nextLine();
            Item16 = sc.nextInt();
            quan16 = sc.nextInt();
            sc.nextLine();
            Item17 = sc.nextInt();
            quan17 = sc.nextInt();
            sc.nextLine();
            Item18 = sc.nextInt();
            quan18 = sc.nextInt();
            sc.nextLine();
            Item19 = sc.nextInt();
            quan19 = sc.nextInt();
            sc.nextLine();
            //If its the last set of data (i.e. Saturday), go into if statement as the nextLine statement will cause an error
            if (!sc.hasNext()){
                //Calculating the price based on menu_key prices
                Price = (quan1*6.5)+(quan2*5.5)+(quan3*4.5)+(quan4*2.5)+(quan5*5)+(quan6*32)+(quan7*0.75)+(quan8*4.75)+(quan9*5.75)+(quan10*3.75)+(quan11*4.5)+
                (quan12*3.5)+(quan13*0.1)+(quan14*1.5)+(quan15*0.5)+(quan16*1.5)+(quan17*1.75)+(quan18*1.25)+(quan19*2);
                System.out.println(currDate + " , " + dayOfWeek + " , " + Price + ", " + Item1 + ", " + quan1+ " , " + Item2 + ", " + quan2+ " , " + Item3 + ", " + 
                quan3+ " , " + Item4 + ", " + quan4+ " , " + Item5 + ", " + quan5+ " , " + Item6 + ", " + quan6+ " , " + Item7 + ", " + quan7+ " , " + Item8 + ", " + 
                quan8+ " , " + Item9 + ", " + quan9+ " , " + Item10 + ", " + quan10+ " , " + Item11 + ", " + quan11+ " , " + Item12 + ", " + quan12+ " , " + Item13 + 
                ", " + quan13+ " , " + Item14 + ", " + quan14+ " , " + Item15 + ", " + quan15+ " , " + Item16 + ", " + quan16+ " , " + Item17 + ", " + quan17+ " , " + 
                Item18 + ", " + quan18+ " , " + Item19 + ", " + quan19);
                //increment daycount to increase the day
                dayCount++;
                //sql statements that insert the values into the table
                String sqlStatement = "INSERT INTO sales_list VALUES ( \'" + currDate + "\' , \'" + dayOfWeek + "\' , " + (float)Price + " , " + quan1+ " , " + quan2+ " , " + quan3+ " , " + quan4+ " , " + quan5+ " , " + quan6+ " , " + quan7+ " , " + quan8+ " , " + quan9+ " , " + quan10+ " , " + quan11+ " , " + quan12+ " , " + quan13+ " , " + quan14+ " , " + quan15+ " , " + quan16+ " , " + quan17+ " , " + quan18+ " , " + quan19 +") ON CONFLICT DO NOTHING;";
                int result = stmt.executeUpdate(sqlStatement);
                System.out.println(result);
                break;
            }
            //This next line skips the empty line in the csv file between different days and the next iteration of the loop will then read the next day
            sc.nextLine();
            //These next lines are just copy paste of the sql statements and price calculation above
            Price = (quan1*6.5)+(quan2*5.5)+(quan3*4.5)+(quan4*2.5)+(quan5*5)+(quan6*32)+(quan7*0.75)+(quan8*4.75)+(quan9*5.75)+(quan10*3.75)+(quan11*4.5)+
            (quan12*3.5)+(quan13*0.1)+(quan14*1.5)+(quan15*0.5)+(quan16*1.5)+(quan17*1.75)+(quan18*1.25)+(quan19*2);
            System.out.println(currDate + " , " + dayOfWeek + " , " + Price + ", " + Item1 + ", " + quan1+ " , " + Item2 + ", " + quan2+ " , " + Item3 + ", " + quan3+ 
            " , " + Item4 + ", " + quan4+ " , " + Item5 + ", " + quan5+ " , " + Item6 + ", " + quan6+ " , " + Item7 + ", " + quan7+ " , " + Item8 + ", " + quan8+ " , " 
            + Item9 + ", " + quan9+ " , " + Item10 + ", " + quan10+ " , " + Item11 + ", " + quan11+ " , " + Item12 + ", " + quan12+ " , " + Item13 + ", " + quan13+ " , "
             + Item14 + ", " + quan14+ " , " + Item15 + ", " + quan15+ " , " + Item16 + ", " + quan16+ " , " + Item17 + ", " + quan17+ " , " + Item18 + ", " + quan18+ 
             " , " + Item19 + ", " + quan19);
            dayCount++;
			String sqlStatement = "INSERT INTO sales_list VALUES ( \'" + currDate + "\' , \'" + dayOfWeek + "\' , " + (float)Price + " , " + quan1+ " , " + quan2+ " , " + quan3+ " , " + quan4+ " , " + quan5+ " , " + quan6+ " , " + quan7+ " , " + quan8+ " , " + quan9+ " , " + quan10+ " , " + quan11+ " , " + quan12+ " , " + quan13+ " , " + quan14+ " , " + quan15+ " , " + quan16+ " , " + quan17+ " , " + quan18+ " , " + quan19 +") ON CONFLICT DO NOTHING;";
			int result = stmt.executeUpdate(sqlStatement);
			System.out.println(result);
      }   
      sc.close();  //closes the scanner  
       
       System.out.println("--------------------Query Results--------------------");
   } catch (Exception e){
       e.printStackTrace();
       System.err.println(e.getClass().getName()+": "+e.getMessage());
       System.exit(0);
   }

    //closing the connection
    try {
      conn.close();
      System.out.println("Connection Closed.");
    } catch(Exception e) {
      System.out.println("Connection NOT Closed.");
    }
    }  
}  