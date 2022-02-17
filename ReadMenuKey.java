import java.io.*;  
import java.util.Scanner;
import java.sql.*;
public class ReadMenuKey{  
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
       //Statement stmt = conn.createStatement();

       //Running a query
       //TODO: update the sql command here
      Scanner sc = new Scanner(new File("MenuKey.csv"));  
      sc.useDelimiter(",");
      int count = 0;
      int Item;
      String Name;
      String Description;
      float Price;
      while (sc.hasNext()){
          if (count < 5){
            String tempData = sc.next(); //Unneeded values from the beginning. These were column names4
            count++;
            continue;
          }
          else{
              Item = Integer.parseInt(sc.next());
              Name = sc.next();
              Description = sc.next();
              Price = Float.parseFloat(sc.next());
          }
          Statement stmt = conn.createStatement();
          String sqlStatement = "INSERT INTO menu_key VALUES (" +Item +",'" + Name + "' ,'" + Description + "',2.5);";
          int result = stmt.executeUpdate(sqlStatement);
          System.out.println(result);
      }   
      sc.close();  //closes the scanner  
       //String sqlStatement = "INSERT INTO menu_key VALUES ();";

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