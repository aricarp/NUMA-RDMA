import java.sql.*;
import java.util.*;
import java.io.*;

public class Capacity {
    static final String DB_URL = "jdbc:oracle:thin:@edgar1.cse.lehigh.edu:1521:cse241";

    public static void main(String[] args) {

        Connection conn = null;
        Scanner in = new Scanner(System.in);
        Console cnsl = System.console();

        do {
            try {
                System.out.print("enter username: ");
                String user = in.nextLine();
                System.out.print("enter password (input hidden): ");
                char[] pw = cnsl.readPassword();
                String passwd = new String(pw);

                conn = DriverManager.getConnection(DB_URL, user, passwd);
                System.out.println("\n\n ***** Connected to Oracle Database *****\n");
                Statement stmt = conn.createStatement();

                int operation = 0;
                int apptNumber = 0;
                String query = "";
                do {
                    try {
                        operation = getOperation(in);
                        switch(operation) {
                        case 1: // PROPERTY MANAGER
                            System.out.println("\nWelcome to the Property Manager Interface\n");
                            // Using the first property as the property that the property manager logs into

                            PropManager p = new PropManager();
                            System.out.println("\nPlease enter your property manager ID (Number from 1-20)");
                            
                            int property_id = 0;
                            try{
                                property_id = in.nextInt();
                                if (property_id>20 || property_id<0){
                                    System.out.println("Must enter an integer from 1-20");
                                    break;
                                }       
                            }
                            catch (Exception e){
                                System.out.println("Must enter an integer from 1-20");
                                break;
                            }
                            
                            query = "select property_name from property where property_id = " + property_id;
                            try{
                                ResultSet rs = stmt.executeQuery(query);
                                while (rs.next()){
                                    String property_name = rs.getString("property_name"); 
                                    System.out.println("You are the property manager of the " + property_name + " property");
                                }
                            }
                            catch(SQLException err){
                                System.out.println("Could not find property associated with that property ID. Returning to main menu");
                            }

                            p.optionMenu();  
                            int option = in.nextInt();
                            if (option == 1){ // record a visit
                                System.out.println("\n\nYou have slected to log a visit.\n");
                                System.out.println("You can log a visit for any of the appartment numbers you have (listed below)");
                                query = "select appartment_number from appartments where property_id = " + property_id;
                                ResultSet rs = stmt.executeQuery(query);
                                while (rs.next()){
                                    String appartment_number = rs.getString("appartment_number"); 
                                    System.out.println("Appartment #: " + appartment_number);
                                }

                                System.out.println("\nEnter the appartment number that was visited");
                                apptNumber = in.nextInt();
                                System.out.println("Enter the date of the visit (Use ****/**/** -- year/month/day format. Enter 01 if January, 01 if first day, etc.)");
                                String date = in.nextLine(); date = in.nextLine();
                                System.out.println("Enter the first name of the visitor");
                                String firstName = in.nextLine();
                                System.out.println("Enter the last name of the visitor");
                                String lastName = in.nextLine();

                                String visitQuery = p.enterVisit(property_id, apptNumber, date, firstName, lastName);
                                try{
                                    stmt.executeQuery(visitQuery);
                                    System.out.println("\nVisit added succesfully\n");
                                }
                                catch (SQLException err){
                                    System.out.println("Could not add visit. Remember to use correct date format and enter an appropriate appartment number. \nExiting back to main menu...\n");
                                }

                            }
                            else if (option == 2){ // record lease data
                                System.out.println("You have selected to record lease data\n");
                                System.out.println("You have access to: \n");
                                query = "select appartment_number from appartments where property_id = " + property_id;
                                try{
                                    ResultSet rs = stmt.executeQuery(query);
                                    while (rs.next()){
                                        String appartment_number = rs.getString("APPARTMENT_NUMBER"); 
                                        System.out.println("Appartment #: " + appartment_number);
                                    }
                                }
                                catch(SQLException err){
                                    System.out.println("Query was not able to execute. Exiting back to main menu...\n");
                                    break;
                                }

                                System.out.println("\nWhich lease would you like to update? (enter appartment number)");
                                int appartment_number = in.nextInt();
                                query = "select  lease.tenant_id, lease.lease_id from lease, appartments where appartments.lease_id = lease.lease_id and appartment_number = " + appartment_number;
                                int tenant_id = -1;
                                int lease_id = -1;
                                try{
                                    ResultSet rs = stmt.executeQuery(query);
                                    while (rs.next()){
                                        String tenID = rs.getString(1); 
                                        tenant_id = Integer.parseInt(tenID);
                                        lease_id = rs.getInt(2);
                                    }
                                }
                                catch(SQLException err){
                                    System.out.println("Query was not able to find a lease ID with that appartment number. Exiting back to main menu...\n");
                                    break;
                                }

                                System.out.println("How many months are left on the lease?");
                                int numMonths =  in.nextInt();
                                System.out.println("How much is the security deposit?");
                                int securityDeposit = in.nextInt();
                                System.out.println("How many weeks is the notice period?");
                                int noticePeriod = in.nextInt();

                                query = "Update lease SET LEASE_ID = " + lease_id + ", TENANT_ID = " + tenant_id + ", NUM_MONTHS = " + numMonths + ", SECURITY_DEPOSIT = " + securityDeposit + ", NOTICE_PERIOD = " + noticePeriod + " where tenant_id = " + tenant_id;                                try{
                                    stmt.executeQuery(query);
                                    System.out.println("Lease data updated. Updates can be in option 5 of the PM interface");
                                }
                                catch (SQLException err){
                                    System.out.println("Error: could not update lease data. Make sure to enter integers when entering data for updated lease. \nReturning to main menu...");
                                    break;
                                }
                            }
                            else if (option == 3){ // View visits for certain appartment number
                                System.out.println("You have selected to view past visits for a certain appartment\n");
                                
                                System.out.println("You have access to the following appartments");
                                query = "select appartment_number from appartments where property_id = " + property_id;
                                ResultSet rss = stmt.executeQuery(query);
                                while (rss.next()){
                                    String appartment_number = rss.getString("APPARTMENT_NUMBER"); 
                                    System.out.println("Appartment #: " + appartment_number);
                                }
                                
                                System.out.println("\nWhat appartment number would you like to search for?");
                                apptNumber = in.nextInt();
                                String visitCheckQuery = p.checkVisit(property_id, apptNumber);
                                try{
                                    ResultSet rs = stmt.executeQuery(visitCheckQuery);
                                    if (!rs.isBeforeFirst()){
                                        System.out.println("\nThere are no visits in the database for that particular appartment.\nReturning to main menu...\n");
                                        break;
                                    }
                                    while (rs.next()){
                                        String pID = rs.getString("PROPERTY_ID");
                                        int aNum = rs.getInt("APPARTMENT_NUMBER");
                                        String dateVis = rs.getString("DATE_OF_VISIT");
                                        String fN = rs.getString("FIRST_NAME_VISITOR");
                                        String lN = rs.getString("LAST_NAME_VISITOR");
                                        System.out.println("Appartment Number: " + aNum + "\nDate Visited: " + dateVis.substring(0,10) + "\nName: " + fN + " " + lN +"\n");
                                    }
                                }
                                catch (SQLException err){
                                    System.out.println("Query was not able to execute. Exiting back to main menu...\n");
                                }
                                break;
                            } 
                            else if (option == 4){ // record move out date
                                System.out.println("You have selected to record a move out date");
                                System.out.println("You have access to the following appartments (appartments that are under your property and are not for sale)");
                                query = "select appartment_number from appartments where property_id = " + property_id + " and for_sale = 0";
                                try{
                                    ResultSet rs = stmt.executeQuery(query);
                                    while (rs.next()){
                                        String appartment_number = rs.getString("APPARTMENT_NUMBER"); 
                                        System.out.println("Appartment #: " + appartment_number);
                                    }
                                }
                                catch(SQLException err){
                                    System.out.println("Query was not able to execute. Exiting back to main menu...\n");
                                    break;
                                }
                                System.out.println("Which appartment would you like to record a move out date for?");
                                int appartment_number = in.nextInt();

                                System.out.println("Enter year (4 numbers)");
                                int year = in.nextInt();
                                System.out.println("Enter month (2 numbers)");
                                int month = in.nextInt();
                                System.out.println("Enter day (2 numbers)");
                                int day = in.nextInt();

                                query = "update appartments set moveout_date = TO_DATE('" + year + "/" + month + "/" + day + "', 'yyyy/mm/dd') where appartment_number = " + appartment_number;
                                try{
                                    stmt.executeQuery(query);
                                    System.out.println("\nMove out date recorded succesfully. Update be viwed in the business manager interface.\n");
                                }
                                catch (SQLException err){
                                    System.out.println("Query error: could not execute. Returning to main menu...");
                                }
                            }
                            else if (option == 5){ // View lease information on particular appartment
                                System.out.println("You selected to view lease information on a particular appartment");
                                System.out.println("You have access to the following appartments");
                                query = "select appartment_number from appartments where property_id = " + property_id;
                                ResultSet rss = stmt.executeQuery(query);
                                while (rss.next()){
                                    String appartment_number = rss.getString("APPARTMENT_NUMBER"); 
                                    System.out.println("Appartment #: " + appartment_number);
                                }
                                
                                System.out.println("\nWhat appartment number would you like to search for?");
                                int appartment_number = in.nextInt();
                                query = "select lease.notice_period, lease.num_months, lease.security_deposit from lease, appartments where lease.lease_id = appartments.lease_id and appartments.appartment_number = " + appartment_number + " and appartments.property_id = " + property_id;
                                //System.out.println(query);
                                ResultSet rs = stmt.executeQuery(query);
                                if (!rs.isBeforeFirst()){
                                    System.out.println("There is no lease information tied to that appartment.\nReturning to main menu...\n");
                                    break;
                                }
                                while (rs.next()){
                                    int notice_period = rs.getInt("notice_period");
                                    int num_months = rs.getInt("num_months");
                                    int security_deposit = rs.getInt("security_deposit");
                                    System.out.println("Notice period: " + notice_period);
                                    System.out.println("Number of months remaining: " + num_months);
                                    System.out.println("Security deposit: " + security_deposit);
                                }
                            }
                            else if (option == 6){
                                System.out.println("Exisiting PM interface...\n");
                            }    
                            else{
                                System.out.println("Must enter an integer from 1-6");
                            }
                            
                            break;
                        case 2:
                            System.out.println("Welecome to the tenant interface");                            
                            Tenant t = new Tenant();
                            String sp_first_name = "";
                            System.out.println("Enter your tenant ID");
                            long tenant_id = in.nextInt();
                            query = "select tenant_first_name, tenant_last_name from tenant where tenant_id = " + tenant_id;
                            try{ 
                                ResultSet rs = stmt.executeQuery(query);
                                if (!rs.isBeforeFirst()){
                                    System.out.println("No tenant with that tenant ID.\nReturning to main menu...");
                                    break;
                                }
                                while(rs.next()){
                                    String first_name = rs.getString("tenant_first_name"); 
                                    String last_name = rs.getString("tenant_last_name");
                                    sp_first_name = rs.getString("tenant_first_name");
                                    System.out.println("Welecome tenant " + first_name + " " + last_name + "!");
                                    break;
                                }
                            }
                            catch(SQLException err){
                                System.out.println("Could not find tenant with that tenant_id. Returning to main menu...");
                            }
                            
                            t.optionMenu();
                            int option2 = in.nextInt();
                            if (option2 == 1){ // Check payment status
                                System.out.println("You selected to check payment status");
                                query = "select rent_due, utilities_due from tenant where tenant_id = " + tenant_id;
                                int rent_due = 0;
                                int utilities_due = 0;
                                try{
                                    ResultSet rs = stmt.executeQuery(query);
                                    while (rs.next()){
                                        rent_due = rs.getInt(1); 
                                        utilities_due = rs.getInt(2);
                                    }
                                }
                                catch (SQLException err){
                                    System.out.println("Could not find amount due. Returning back to main menu...");
                                    break;
                                }
                                if (rent_due == 0 && utilities_due == 0)
                                    System.out.println("You have no payment due");
                                else if (rent_due == 0 && utilities_due > 0)
                                    System.out.println("Utilities due: " + utilities_due);
                                else if (rent_due > 0 && utilities_due == 0)
                                    System.out.println("Rent due: " + rent_due);
                                else{
                                    System.out.println("Rent due: " + rent_due);
                                    System.out.println("Utilities due: " + utilities_due);
                                }

                            }
                            else if(option2 == 2){  // Make payment
                                System.out.println("You selected to make a payment");
                                System.out.println("Chose Utilities or Rent ('U' or 'R')");
                                String option3 = in.next();
                                if (option3.equals("U")){
                                    System.out.println("What form of payment would you like to choose ('CC' (Credit Card) or 'VENMO')"); 
                                    System.out.println("Enter either 'CC' or 'VENMO'");
                                    
                                    Random r = new Random();
                                    int low = 1;
                                    int high = 10000;
                                    int randNum = r.nextInt(high-low) + low;
                                    int amount = 0;
                                    query = "select UTILITIES_DUE from tenant where tenant_id = " + tenant_id;
                                    ResultSet rs = stmt.executeQuery(query);
                                    while (rs.next())
                                        amount = rs.getInt("UTILITIES_DUE");
                                    
                                    String choice = in.next();
                                    if (choice.equals("CC")){
                                        System.out.println("Enter credit card number (16 digits, no spaces):");
                                        String cc_num = in.next();
                                        System.out.println("Enter expliration date (MM/YY)");
                                        String cc_date = in.next();
                                        System.out.println("Enter security code");
                                        String security_code = in.next();

                                        query = "insert into payment (Payment_id, tenant_id, amount) values (" + randNum + ", " + tenant_id + ", " + amount + ")";
                                        try{
                                            stmt.executeQuery(query);
                                        }
                                        catch(SQLException err){
                                            System.out.println("Error when trying to add payment");
                                        }
                                        query = "insert into credit_card (Payment_id, cc_number, cc_exp_date, security_code) values (" + randNum + ", " + cc_num + ", '" + cc_date + "', " + security_code + ")";
                                        try {
                                            stmt.executeQuery(query);
                                            System.out.println("Payment completed succesfully");
                                        }
                                        catch (SQLException err){
                                            System.out.println("Error when trying to add payment history");
                                        }

                                        query = "update tenant set UTILITIES_DUE = 0 where tenant_id = " + tenant_id;
                                        stmt.executeQuery(query);

                                    }
                                    else if (choice.equals("VENMO")){
                                        System.out.println("Enter senders first name");
                                        String send_first_name = in.next();
                                        System.out.println("Enter senders last name");
                                        String send_last_name = in.next();
                                        System.out.println("Enter recievers first name");
                                        String recieve_first_name = in.next();
                                        System.out.println("Enter recievers last name");
                                        String recieve_last_name = in.next();

                                        query = "insert into payment (Payment_id, tenant_id, amount) values (" + randNum + ", " + tenant_id + ", " + amount + ")";
                                        try{
                                            stmt.executeQuery(query);
                                        }
                                        catch(SQLException err){
                                            System.out.println("Error when trying to add payment");
                                            break;
                                        }
                                        query = "insert into venmo (Payment_id, sender_first_name, sender_last_name, reciever_first_name, reciever_last_name) values (" + randNum + ", '" + send_first_name + "', '" + send_last_name + "', '" + recieve_first_name + "', '" + recieve_last_name +  "')";          
                                        try {
                                            stmt.executeQuery(query);
                                            System.out.println("Payment completed succesfully");
                                        }
                                        catch (SQLException err){
                                            System.out.println("Error when trying to add payment history");
                                            break;
                                        }
                                        query = "update tenant set UTILITIES_DUE = 0 where tenant_id = " + tenant_id;
                                        stmt.executeQuery(query);
                                    }
                                    else{
                                        System.out.println("Must enter either CC or VENMO. Returning to main menu...");
                                    }

                                }
                                else if (option3.equals("R")){
                                    System.out.println("What form of payment would you like to choose ('CC' (credit card) or 'VENMO')"); 
                                    System.out.println("Enter either 'CC' or 'VENMO'");
                                    
                                    Random r = new Random();
                                    int low = 1;
                                    int high = 10000;
                                    int randNum = r.nextInt(high-low) + low;
                                    int amount = 0;
                                    query = "select rent_due from tenant where tenant_id = " + tenant_id;
                                    ResultSet rs = stmt.executeQuery(query);
                                    while (rs.next())
                                        amount = rs.getInt("rent_due");
                                    
                                    String choice = in.next();
                                    if (choice.equals("CC")){
                                        System.out.println("Enter credit card number (16 digits, no spaces):");
                                        String cc_num = in.next();
                                        System.out.println("Enter expliration date (MM/YY)");
                                        String cc_date = in.next();
                                        System.out.println("Enter security code");
                                        String security_code = in.next();

                                        query = "insert into payment (Payment_id, tenant_id, amount) values (" + randNum + ", " + tenant_id + ", " + amount + ")";
                                        try{
                                            stmt.executeQuery(query);
                                        }
                                        catch(SQLException err){
                                            System.out.println("Error when trying to add payment");
                                        }
                                        query = "insert into credit_card (Payment_id, cc_number, cc_exp_date, security_code) values (" + randNum + ", " + cc_num + ", '" + cc_date + "', " + security_code + ")";
                                        try {
                                            stmt.executeQuery(query);
                                            System.out.println("Payment completed succesfully");
                                        }
                                        catch (SQLException err){
                                            System.out.println("Error when trying to add payment history");
                                        }

                                        query = "update tenant set rent_due = 0 where tenant_id = " + tenant_id;
                                        stmt.executeQuery(query);

                                    }
                                    else if (choice.equals("VENMO")){
                                        System.out.println("Enter senders first name");
                                        String send_first_name = in.next();
                                        System.out.println("Enter senders last name");
                                        String send_last_name = in.next();
                                        System.out.println("Enter recievers first name");
                                        String recieve_first_name = in.next();
                                        System.out.println("Enter recievers last name");
                                        String recieve_last_name = in.next();

                                        query = "insert into payment (Payment_id, tenant_id, amount) values (" + randNum + ", " + tenant_id + ", " + amount + ")";
                                        try{
                                            stmt.executeQuery(query);
                                        }
                                        catch(SQLException err){
                                            System.out.println("Error when trying to add payment");
                                            break;
                                        }
                                        query = "insert into venmo (Payment_id, sender_first_name, sender_last_name, reciever_first_name, reciever_last_name) values (" + randNum + ", '" + send_first_name + "', '" + send_last_name + "', '" + recieve_first_name + "', '" + recieve_last_name +  "')";          
                                        try {
                                            stmt.executeQuery(query);
                                            System.out.println("Payment completed succesfully");
                                        }
                                        catch (SQLException err){
                                            System.out.println("Error when trying to add payment history");
                                            break;
                                        }
                                        query = "update tenant set rent_due = 0 where tenant_id = " + tenant_id;
                                        stmt.executeQuery(query);
                                    }
                                    else{
                                        System.out.println("Must enter either CC or VENMO. Returning to main menu...");
                                    }
                                }
                                else{
                                    System.out.println("Must enter 'U' or 'R' to indicate rent or utility payment");
                                    break;
                                }
                            }
                            else if (option2 == 3){ // Add tenant to a lease
                                System.out.println("You selected to add a tenat to your lease (the tenant you add will have the same ID as you)");
                                System.out.println("Enter new tenants first name");
                                String first_name = in.next();
                                System.out.println("Enter new tenants last name");
                                String last_name = in.next();

                                query = "insert into tenant (tenant_id, tenant_first_name, tenant_last_name, rent_due, utilities_due) values (" + tenant_id + ", '" + first_name + "', '" + last_name + "', 0, 0)";
                                try{
                                    stmt.executeQuery(query);
                                    System.out.println("New tenant added succesfully");
                                }
                                catch(SQLException err){
                                    System.out.println("Could not add new tenant. Returning to main menu...");
                                }
                                
                            }
                            else if (option2 == 4){ // View tenants on your lease
                                System.out.println("You selected to view tenants on your lease");
                                query = "select * from tenant where tenant_id = " + tenant_id;
                                ResultSet rs = stmt.executeQuery(query);
                                while (rs.next()){
                                    String first_name = rs.getString("tenant_first_name");
                                    String last_name = rs.getString("tenant_last_name");
                                    System.out.println("Tenant: " + first_name + " " + last_name);
                                }
                            }
                            else if (option2 == 5){ // Edit personal data
                                System.out.println("You selected to edit your personal tenant data");
                                System.out.println("Enter your updated first name");
                                String first_name = in.next();
                                System.out.println("Enter your updated last name");
                                String last_name = in.next();
                                query = "update tenant set tenant_first_name = '" + first_name + "', tenant_last_name = '" + last_name + "' where tenant_id = " + tenant_id + " and tenant_first_name = '" + sp_first_name + "'";
                                stmt.executeQuery(query);
                                System.out.println("Personal data updated succesfully");
                                
                            }
                            else if (option2 == 6){
                                System.out.println("Exiting Tenant Interface and returning to main menu...");
                                break;
                            }
                            else {
                                System.out.println("Must enter an integer from 1-6.\nReturning to main menu...");
                            }

                            break;
                        case 3:
                            System.out.println("Welecome to the business manager interface! (VIEW ONLY)\nPlease select an action\n");
                            BM b = new BM();
                            b.optionMenu();
                            int optionBM = 0;
                            try{
                                optionBM = in.nextInt();
                            }
                            catch (Exception e){
                                System.out.println("Must enter an integer from 1-6\nReturning to main menu...");
                                break;
                            }
                            int x = -1;
                            if (optionBM == 1){ // View all properties
                                System.out.println("You selected to view a property.");
                                System.out.println("Enter property ID of property you would like to view (number 1-20)");
                                int choice = in.nextInt();
                                query = "select * from property where property_id = " + choice;
                                ResultSet rs = stmt.executeQuery(query);
                                String prop_name = "";  String street_name = ""; String street_num = ""; String zip_code = ""; 
                                String pool = ""; String spa = ""; String gym = "";
                                while (rs.next()){
                                    prop_name = rs.getString("property_name");
                                    street_name = rs.getString("street_name");
                                    street_num = rs.getString("street_number");
                                    zip_code = rs.getString("zip_code");
                                    pool = rs.getString("pool"); spa = rs.getString("spa"); gym = rs.getString("gym");
                                }
                                
                                System.out.println("Property Name: " + prop_name);
                                System.out.println("Location: " + street_num + " " + street_name + " St.");
                                System.out.println("Zip Code: " + zip_code);
                                x = Integer.parseInt(pool);
                                if (x == 1)
                                    System.out.println("Pool present");
                                else
                                    System.out.println("No pool present");
                                x = Integer.parseInt(spa);
                                if (x == 1)
                                    System.out.println("Spa present");
                                else
                                    System.out.println("No spa present");
                                x = Integer.parseInt(gym);
                                if (x == 1)
                                    System.out.println("Gym present");
                                else
                                    System.out.println("No gym present");

                            }
                            else if (optionBM == 2){ // View appartments at a certain property
                                System.out.println("You selected to view appartments for a certain property");
                                System.out.println("Which property would you like to see the appartments for? (integer 1-20)");
                                int property_id2 = in.nextInt();
                                query = "select * from appartments where property_id = " + property_id2;
                                ResultSet rs = stmt.executeQuery(query);
                                while(rs.next()){
                                    String appartment_number = rs.getString("Appartment_number");
                                    String lease_id = rs.getString("lease_id");
                                    int num_bathrooms = rs.getInt("Number_bathrooms");
                                    int num_bedrooms = rs.getInt("Number_bedrooms");
                                    int for_sale = rs.getInt("for_sale");
                                    String move_out = rs.getString("moveout_date");
                                    System.out.println("Appartment number: " + appartment_number);
                                    System.out.println("\tLease ID: " + lease_id);
                                    System.out.println("\t# Bed: " + num_bathrooms + "; # Bath: " + num_bathrooms);
                                    if (for_sale == 1)
                                        System.out.println("\tFor sale");
                                    else   
                                        System.out.println("\tNot for sale");
                                    if (move_out != null)
                                        System.out.println("\tMove out date: " + move_out);
                                    else
                                        System.out.println("\tNo move out date set");
                                }

                            }
                            else if (optionBM == 3){ // View visits for a certain appartment
                                System.out.println("You selected to view visits for a certain appartment");
                                System.out.println("What property is the appartment located (1-20)");
                                int prop_ID = in.nextInt();
                                System.out.println("What appartment at this property would you like to search for?");
                                int appt_Number = in.nextInt();
                                query = "select * from visits where property_id = " + prop_ID + " and appartment_number = " + appt_Number;
                                ResultSet rs = stmt.executeQuery(query);
                                if (!rs.isBeforeFirst()){
                                    System.out.println("There are no visits in the database for this appartment");
                                }
                                while (rs.next()){
                                    String date_visit = rs.getString("Date_of_visit");
                                    String firstN = rs.getString("first_name_visitor");
                                    String lastN = rs.getString("last_name_visitor");
                                    System.out.println("Date: " + date_visit);
                                    System.out.println("\tVisitor: " + firstN + " " + lastN);
                                }
                            }
                            else if (optionBM == 4){ // Check move out date for certain appartment
                                System.out.println("You selected to check if there is a move out date for a certain appartments");
                                System.out.println("What property is the appartment located (1-20)");
                                int prop_ID = in.nextInt();
                                System.out.println("What appartment at this property would you like to check the move out date for?");
                                int appt_Number = in.nextInt();

                                query = "select moveout_date from appartments where property_id = " + prop_ID + " and appartment_number = " + appt_Number;
                                ResultSet rs = stmt.executeQuery(query);
                                if (!rs.isBeforeFirst()){
                                    System.out.println("There is no move out date in the database for this appartment");
                                    break;
                                }
                                while (rs.next()){
                                    String mdate = rs.getString("moveout_date");
                                    System.out.println("Move out date: " + mdate);
                                }
                            }
                            else if (optionBM == 5){ // View payment history for a certain tenant
                                System.out.println("You selected to view payment history for a certain tenant");
                                System.out.println("Enter the tenant ID you would like to search payment history for:");
                                int tenID = in.nextInt();
                                query = "select * from payment where tenant_id = " + tenID;
                                ResultSet rs = stmt.executeQuery(query);
                                if (!rs.isBeforeFirst()){
                                    System.out.println("There is no payment history for that tenant ID");
                                    break;
                                }
                                while (rs.next()){
                                    int paymentID = rs.getInt("payment_id");
                                    int amount = rs.getInt("amount");
                                    System.out.println("Payment ID: " + paymentID);
                                    System.out.println("\tAmount: " + amount);
                                }
                            }
                            else if (optionBM == 6){
                                System.out.println("Exiting BM interface...");
                            }
                            else {
                                System.out.println("Must enter an integer from 1-6. Returning to main menu...");
                            }
                            break;
                        case 4:
                            System.out.println("Program exiting...");
                            System.out.println("--");
                            break;
                        }
                    }
                    catch(Exception e){
                        System.out.println(e);
                    }
                }while(operation != 4);
                
                            
                            
                conn.close();
            } catch (SQLException se) {
                System.out.println("Username/password failure");
            }

        } while (conn == null);
        in.close();
    }

    public static int getOperation(Scanner input) {
		int op = 0;
		do {
			System.out.println("\nSelect an interface: ");
			System.out.println("  i.   Property Manager ");
			System.out.println("  ii.  Tenant");
			System.out.println("  iii. Business Manager");
			System.out.println("  iv.  Quit the program");
			if (input.hasNextInt()) {
				op = input.nextInt();
				if (op >= 1 && op <= 5)
					break;
				else
					System.out.println("Must enter integer from 1 to 4");
			}
			else {
				input.nextLine();
				System.out.println("False input");
			}
		} while (true);
		return op;
	}


}
