import java.io.*;  
import java.util.Scanner;
import java.sql.*;
public class UnitConversionInput{  
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


       int Item;
       String Name;
       String Description;
       String sqlStatement;
       int result;
       //Start with Item 501, 5 finger

       
       Item = 501;
       Name = "5 Finger Original";
       Description = "x5 Chicken Finger, Sauce, Toast, Fries, Potato Salad, Drink";
       sqlStatement = "INSERT INTO recipes VALUES ( " + Item + " , \'" + Name + "\' , \'" + Description + "\' ) ON CONFLICT DO NOTHING;";
	   result = stmt.executeUpdate(sqlStatement);
       

       //Item 502, 4 finger meal
       Item = 502;
       Name = "4 Finger Meal";
       Description = "x4 Chicken Finger, Sauce, Toast, Fries, Potato Salad, Drink";
       sqlStatement = "INSERT INTO recipes VALUES ( " + Item + " , \'" + Name + "\' , \'" + Description + "\' ) ON CONFLICT DO NOTHING;";
	   result = stmt.executeUpdate(sqlStatement);

       //Item 503, 3 finger meal
       Item = 503;
       Name = "3 Finger Meal";
       Description = "x3 Chicken Finger, Sauce, x1 Toast, Fries, Potato Salad, Drink";
       sqlStatement = "INSERT INTO recipes VALUES ( " + Item + " , \'" + Name + "\' , \'" + Description + "\' ) ON CONFLICT DO NOTHING;";
	   result = stmt.executeUpdate(sqlStatement);

       //Item 504, Kids meal
       Item = 504;
       Name = "Kids meal";
       Description = "x2 Chicken Finger, Sauce, Toast, Fries, Potato Salad, Drink";
       sqlStatement = "INSERT INTO recipes VALUES ( " + Item + " , \'" + Name + "\' , \'" + Description + "\' ) ON CONFLICT DO NOTHING;";
	   result = stmt.executeUpdate(sqlStatement);

       //Item 505, Tea
       Item = 505;
       Name = "Gallon of Tea";
       Description = "8 tea bags, 1 cup sugar for tea";
       sqlStatement = "INSERT INTO recipes VALUES ( " + Item + " , \'" + Name + "\' , \'" + Description + "\' ) ON CONFLICT DO NOTHING;";
	   result = stmt.executeUpdate(sqlStatement);
       System.out.println(result);

       //Item 506, Family Pack
       Item = 506;
       Name = "Family Pack";
       Description = "x20 Chicken, x8 Sauce, x4 Toast, x4 Fries";
       sqlStatement = "INSERT INTO recipes VALUES (" + Item + " , \'" + Name + "\' , \'" + Description + "\') ON CONFLICT DO NOTHING;";
	   result = stmt.executeUpdate(sqlStatement);

       //Item 507, Club Sandwich combo
       Item = 507;
       Name = "Club Sandwhich Meal Combo";
       Description = "x1 Club, Toast, Sauce, Potato Salad, Fries, Drink";
       sqlStatement = "INSERT INTO recipes VALUES ( " + Item + " , \'" + Name + "\' , \'" + Description + "\' ) ON CONFLICT DO NOTHING;";
	   result = stmt.executeUpdate(sqlStatement);

       //Item 508, Club Sandwich
       Item = 508;
       Name = "Club Sandwhich";
       Description = "2 slices bread, 1 slice cheese, 2 slices bacon, x2 Chicken, 1/2 cup oil";
       sqlStatement = "INSERT INTO recipes VALUES ( " + Item + " , \'" + Name + "\' , \'" + Description + "\' ) ON CONFLICT DO NOTHING;";
	   result = stmt.executeUpdate(sqlStatement);

        //Item 509, Sandwich Combo
        Item = 509;
        Name = "Sandwhich Combo";
        Description = "2 slices bread, 1 slice cheese, 2 slices bacon, 1/2 cup fryer oil";
        sqlStatement = "INSERT INTO recipes VALUES ( " + Item + " , \'" + Name + "\' , \'" + Description + "\' ) ON CONFLICT DO NOTHING;";
	   result = stmt.executeUpdate(sqlStatement);

       //Item 500, Sandwich
       Item = 510;
       Name = "Sandwhich";
       Description = "2 slices thick bread, 1 slice cheese, 2 slices bacon, 1/2 cup fryer oil";
       sqlStatement = "INSERT INTO recipes VALUES ( " + Item + " , \'" + Name + "\' , \'" + Description + "\' ) ON CONFLICT DO NOTHING;";
	   result = stmt.executeUpdate(sqlStatement);

       //Item 511, grilled cheese meal
       Item = 511;
       Name = "Grilled Cheese Meal Combo";
       Description = "x1 Grilled Cheese, Toast, Sauce, Potato Salad, Fries, Drink";
       sqlStatement = "INSERT INTO recipes VALUES ( " + Item + " , \'" + Name + "\' , \'" + Description + "\' ) ON CONFLICT DO NOTHING;";
	   result = stmt.executeUpdate(sqlStatement);

       //Item 512, Grill Cheese Sandwhich
       Item = 512;
       Name = "Grilled Cheese Sandwhich";
       Description = "2 slices thick bread, 1.5 Tsp liquid margarine, 1 slice cheese";
       sqlStatement = "INSERT INTO recipes VALUES ( " + Item + " , \'" + Name + "\' , \'" + Description + "\' ) ON CONFLICT DO NOTHING;";
	   result = stmt.executeUpdate(sqlStatement);

       //Item 513, Layne's sauce
       Item = 513;
       Name = "Laynes Sauce";
       Description = "3/4 cup mayo, 1/4 cup ketchup, 5 tsp worce, 1/2 tsp pepper, 1/2 tsp garlic";
       sqlStatement = "INSERT INTO recipes VALUES ( " + Item + " , \'" + Name + "\' , \'" + Description + "\' ) ON CONFLICT DO NOTHING;";
	   result = stmt.executeUpdate(sqlStatement);

       //Item 514, 1 Chicken Finger
       Item = 514;
       Name = "Chicken Finger";
       Description = "1.5 oz raw chicken breast, 1 oz flour, 1/8 cup fryer oil";
       sqlStatement = "INSERT INTO recipes VALUES ( " + Item + " , \'" + Name + "\' , \'" + Description + "\' ) ON CONFLICT DO NOTHING;";
	   result = stmt.executeUpdate(sqlStatement);

       //Item 515, 1 Toast
       Item = 516;
       Name = "Toast";
       Description = "1 slice Thick Bread, 0.5 oz liquid margarine, 1 pinch salt";
       sqlStatement = "INSERT INTO recipes VALUES ( " + Item + " , \'" + Name + "\' , \'" + Description + "\' ) ON CONFLICT DO NOTHING;";
	   result = stmt.executeUpdate(sqlStatement);

       //Item 516, 1 Potato Salad
       Item = 516;
       Name = "Potato Salad";
       Description = "5.5 oz potato salad, 5.5 oz clear cup and lid";
       sqlStatement = "INSERT INTO recipes VALUES ( " + Item + " , \'" + Name + "\' , \'" + Description + "\' ) ON CONFLICT DO NOTHING;";
	   result = stmt.executeUpdate(sqlStatement);

       //Item 517, 1 order of fries
       Item = 517;
       Name = "Crinkle Cut Fries";
       Description = "7 oz fries, 2 tsp salt, 1/2 cup fryer oil";
       sqlStatement = "INSERT INTO recipes VALUES ( " + Item + " , \'" + Name + "\' , \'" + Description + "\' ) ON CONFLICT DO NOTHING;";
	   result = stmt.executeUpdate(sqlStatement);

       //Item 518, soft drink
       Item = 518;
       Name = "Fountain Drink";
       Description = "16 oz cup, 1 staw, 1 drink lid";
       sqlStatement = "INSERT INTO recipes VALUES ( " + Item + " , \'" + Name + "\' , \'" + Description + "\' ) ON CONFLICT DO NOTHING;";
	   result = stmt.executeUpdate(sqlStatement);

       //Item 519, bottled drink
       Item = 519;
       Name = "Bottle Drink";
       Description = "12 oz bottled drink";
       sqlStatement = "INSERT INTO recipes VALUES ( " + Item + " , \'" + Name + "\' , \'" + Description + "\' ) ON CONFLICT DO NOTHING;";
	   result = stmt.executeUpdate(sqlStatement);



       System.out.println(result);
        
       
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