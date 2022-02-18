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
       Statement stmt = conn.createStatement();

       //Running a query
       //TODO: update the sql command here
      Scanner sc = new Scanner(new File("MenuKey.csv")).useDelimiter(",");
      int count = 0;
      int Item;
      String Name;
      String Description;
      Float Price;
	  String line;
	  sc.nextLine();
	  sc.nextLine();
      while (sc.hasNext()){
			line = sc.nextLine();
			String[] split1 = line.split("\"",3);
			if(split1.length > 1){
				Description = split1[1]; //Description
				String[] split2 = split1[0].split(",",3);
				Item = Integer.parseInt(split2[1]);//Primary Key
				Name = split2[2].substring(0,split2[2].length()-1).replaceFirst("'","");//Name
				Price = Float.parseFloat(split1[2].substring(2,6));//Price
			} else{
				String[] split2 = split1[0].split(",",4);
				Description = "";
				Item = Integer.parseInt(split2[1]);//Primary Key
				Name = split2[2].replaceFirst("'","");//Name
				Price = Float.parseFloat(split2[3].substring(2,6));//Price
			}
            System.out.println(Item + " , " + Name + " , " + Description + " , " + Price);
			String sqlStatement = "INSERT INTO menu_key VALUES ( " + Item + " , \'" + Name + "\' , \'" + Description + "\' , " + Price +") ON CONFLICT DO NOTHING;";
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