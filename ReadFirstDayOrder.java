import java.io.*;  
import java.util.Scanner;
import java.sql.*;
public class ReadFirstDayOrder{  
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
      Scanner sc = new Scanner(new File("First day order.csv")).useDelimiter(",");
      String Type;
	  String Name;
	  String SKU;
	  Float quantity;
	  String sold_by;
	  String delivered_by;
	  Float purchase_price_per;
	  int invoice_line;
	  String description;
	  String date_ordered = "";
	  String next;
	  int count = 0;
	  for(int i = 0; i < 6;i++){
		    date_ordered = sc.next();
	  }
	  sc.nextLine();
	  sc.nextLine();
	  Type = sc.next();
	  sc.nextLine();
      while (sc.hasNext()){
			if(count == 19 || count == 25 || count == 29 || count == 42){
				Type = sc.next();
				sc.nextLine();
			} else if(count == 52){
				break;
			}
			Name = sc.next();
			SKU = sc.next();
			quantity = Float.parseFloat(sc.next());
			sc.next();
			sold_by = sc.next();
			delivered_by = sc.next();
			sc.next();
			purchase_price_per = Float.parseFloat(sc.next().substring(1));
			next = sc.nextLine();
			if(count == 0){
				String[] split = next.split("\"",5);
				// for(int j = 0; j < split.length; j++){
					// System.out.println(split[j]);
				// }
				invoice_line = Integer.parseInt(split[2].substring(split[2].length()-2,split[2].length()-1));
				description = split[3];
			} else {
				String[] split = next.split("\"",3);
				// for(int j = 0; j < split.length; j++){
					// System.out.println(split[j]);
				// }
				invoice_line = Integer.parseInt(split[0].substring(split[0].length()-2,split[0].length()-1));
				description = split[1];
			}
			count++;
			System.out.println(count+" " + Type + " , " + Name + " , " + SKU + " , " + quantity + " , " + sold_by + " , " + delivered_by + " , " + purchase_price_per + " , " + invoice_line + " , " + description);
			String sqlStatement = "INSERT INTO inventory VALUES ( \'" + SKU + "\' , \'" + Type + "\' , \'" + Name + "\' , " + quantity + " , \'" + sold_by + "\' , \'" + delivered_by + "\' , " + purchase_price_per + " , " + invoice_line + " , \'" + date_ordered + "\' , \'" + description + "\') ON CONFLICT DO NOTHING;";
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