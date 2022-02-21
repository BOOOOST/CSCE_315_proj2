import java.io.*;  
import java.util.Scanner;
import java.sql.*;
import java.util.Calendar;
public class ReadOrderSales1{  
    public static String PickDay(int num){
        String date = "";
        switch(num){
            case 1: date = "02/13/2022";
            break;
            case 2: date = "02/14/2022";
            break;
            case 3: date = "02/15/2022";
            break;
            case 4: date = "02/16/2022";
            break;
            case 5: date = "02/17/2022";
            break;
            case 6: date = "02/18/2022";
            break;
            case 7: date = "02/19/2022";
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

       //Running a query
       //TODO: update the sql command here
      Scanner sc = new Scanner(new File("FirstWeekSales.csv")).useDelimiter(",");
      int Item1;
      int Item2;
      int Item3;
      int Item4;
      int Item5;
      int Item6;
      int Item7;
      int Item8;
      int Item9;
      int Item10;
      int Item11;
      int Item12;
      int Item13;
      int Item14;
      int Item15;
      int Item16;
      int Item17;
      int Item18;
      int Item19;
      int quan1, quan2, quan3, quan4, quan5, quan6, quan7, quan8, quan9, quan10, quan11, quan12, quan13, quan14, quan15, quan16, quan17, quan18, quan19;
      String currDate;
      String dayOfWeek;
      double Price = 0;
	  sc.nextLine();
      int dayCount = 1;
      while (sc.hasNext()){
            currDate = ReadOrderSales1.PickDay(dayCount);
			dayOfWeek = sc.next();
            sc.nextLine();
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
            if (!sc.hasNext()){
                Price = (quan1*6.5)+(quan2*5.5)+(quan3*4.5)+(quan4*2.5)+(quan5*5)+(quan6*32)+(quan7*0.75)+(quan8*4.75)+(quan9*5.75)+(quan10*3.75)+(quan11*4.5)+(quan12*3.5)+(quan13*0.1)+(quan14*1.5)+(quan15*0.5)+(quan16*1.5)+(quan17*1.75)+(quan18*1.25)+(quan19*2);
                System.out.println(currDate + " , " + dayOfWeek + " , " + Price + ", " + Item1 + ", " + quan1+ " , " + Item2 + ", " + quan2+ " , " + Item3 + ", " + quan3+ " , " + Item4 + ", " + quan4+ " , " + Item5 + ", " + quan5+ " , " + Item6 + ", " + quan6+ " , " + Item7 + ", " + quan7+ " , " + Item8 + ", " + quan8+ " , " + Item9 + ", " + quan9+ " , " + Item10 + ", " + quan10+ " , " + Item11 + ", " + quan11+ " , " + Item12 + ", " + quan12+ " , " + Item13 + ", " + quan13+ " , " + Item14 + ", " + quan14+ " , " + Item15 + ", " + quan15+ " , " + Item16 + ", " + quan16+ " , " + Item17 + ", " + quan17+ " , " + Item18 + ", " + quan18+ " , " + Item19 + ", " + quan19);
                dayCount++;
                String sqlStatement = "INSERT INTO sales_list VALUES ( \'" + currDate + "\' , \'" + dayOfWeek + "\' , " + (float)Price + " , " + quan1+ " , " + quan2+ " , " + quan3+ " , " + quan4+ " , " + quan5+ " , " + quan6+ " , " + quan7+ " , " + quan8+ " , " + quan9+ " , " + quan10+ " , " + quan11+ " , " + quan12+ " , " + quan13+ " , " + quan14+ " , " + quan15+ " , " + quan16+ " , " + quan17+ " , " + quan18+ " , " + quan19 +") ON CONFLICT DO NOTHING;";
                int result = stmt.executeUpdate(sqlStatement);
                System.out.println(result);
                break;
            }
            sc.nextLine();
            Price = (quan1*6.5)+(quan2*5.5)+(quan3*4.5)+(quan4*2.5)+(quan5*5)+(quan6*32)+(quan7*0.75)+(quan8*4.75)+(quan9*5.75)+(quan10*3.75)+(quan11*4.5)+(quan12*3.5)+(quan13*0.1)+(quan14*1.5)+(quan15*0.5)+(quan16*1.5)+(quan17*1.75)+(quan18*1.25)+(quan19*2);
            System.out.println(currDate + " , " + dayOfWeek + " , " + Price + ", " + Item1 + ", " + quan1+ " , " + Item2 + ", " + quan2+ " , " + Item3 + ", " + quan3+ " , " + Item4 + ", " + quan4+ " , " + Item5 + ", " + quan5+ " , " + Item6 + ", " + quan6+ " , " + Item7 + ", " + quan7+ " , " + Item8 + ", " + quan8+ " , " + Item9 + ", " + quan9+ " , " + Item10 + ", " + quan10+ " , " + Item11 + ", " + quan11+ " , " + Item12 + ", " + quan12+ " , " + Item13 + ", " + quan13+ " , " + Item14 + ", " + quan14+ " , " + Item15 + ", " + quan15+ " , " + Item16 + ", " + quan16+ " , " + Item17 + ", " + quan17+ " , " + Item18 + ", " + quan18+ " , " + Item19 + ", " + quan19);
            dayCount++;
			String sqlStatement = "INSERT INTO sales_list VALUES ( \'" + currDate + "\' , \'" + dayOfWeek + "\' , " + (float)Price + " , " + quan1+ " , " + quan2+ " , " + quan3+ " , " + quan4+ " , " + quan5+ " , " + quan6+ " , " + quan7+ " , " + quan8+ " , " + quan9+ " , " + quan10+ " , " + quan11+ " , " + quan12+ " , " + quan13+ " , " + quan14+ " , " + quan15+ " , " + quan16+ " , " + quan17+ " , " + quan18+ " , " + quan19 +") ON CONFLICT DO NOTHING;";
			int result = stmt.executeUpdate(sqlStatement);
			System.out.println(result);
      }   
      sc.close();  //closes the scanner  
       

       //send statement to DBMS
       //This executeQuery command is useful for data retrieval
       //ResultSet result = stmt.executeQuery(sqlStatement);
       //OR
       //This executeUpdate command is useful for updating data
       //int result = stmt.executeUpdate(sqlStatement);

       //OUTPUT
       //You will need to output the results differently depeninding on which function you use
       System.out.println("--------------------Query Results--------------------");
       //while (result.next()) {
       //System.out.println(result.getString("column_name"));
       //}
       //OR
      //System.out.println(result);
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