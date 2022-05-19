Ari Carp
05/06/2022 (executable due date)
CSE 241 Final Project

This README file will serve as a guide on how to interact with my CSE 241 final project.

Note on Recompilation:
 - I stored all of my .java files in a seperate folder 'Capacity' within the amc624carp folder
 - If you would like to recompile, the .class files would then need to be moved from the Capacity folder to the amc624 folder
 - Sorry for the inconvinience if you would like to recompile, I just just figured it would make it easier if you would like to look at source code
 - All of the .class files in amc624 currently are up to date at the time the project was turned in

Directory Overview:
 - 95% of my code is in the Capacity.java file
 - I created class files for each interface but used them much more minimally then I had originally intended
    - BM.java (Business Manager Interface), PropManager.java (Property Manager Interface), Tenant.java (Tenant Interface)
 - The architecture of this project is less organized than I had hoped but made sense to me after diving into the development
 - Additionally, on the top layer, I have a tableCreate.sql file which is the initial creation of the tables in this project
    - I then edited some of them manualy in the Oracle SQLDeveloper
Data Note
 - I generated data for the property, tenant, appartment, and lease tables from Mockaroo - a data generation website

Walkthrough
1. Enter your Oracle Username and Password. If entered wrong, you will be prompted to enter the credentials again. 
2. Select an interface to enter (after each action is complpeted in an interface, the interface exits and returns to main menu). Below will be walkthroughs of each interface.
 Property Manager (PM) Interface
  - Step 1: Enter PM ID (there are 20 properties) so you can enter a number from 1-20. All of the actions in the PM interface are dependednt on the PM thus it is entered first.
  - Step 2: Select an action (enter the corresponding integer - e.g. first is '1', etc.); below the options is a walkthrough of each option
        There are 4 actions and 1 exit action:
         1) Record a visit 
		 2) Update lease data
         3) View past visits for certain appartment
         4) Record move out date
		 5) Exit Property Manager Interface
    
    1) Record a visit.
      a) When chosing to record a visit you will first be promted for the appartment number that was visited
         - I made it so that all appartment numbers that the PM has access to are printed above (and the user must chose from that, if not the visit will not be added succesfully).
      b) Next the user must enter the date in the (****/**/** - year/month/day format). Additionally, the user must enter a 0 before the number for month and day if only one number is required. e.g. 01 for January
      c) Next the user must enter the first name of the visitor
      d) Next the user must enter the last name of the visitor 
         - I could have added more attributes about the visitor, however, I was not sure what information NUMA would have about visitors and thus assumed the minimum.
      e) If the date and appartment numbers are entered correctly, then a success message will print out. If the visit can not be logged an error message will print.
    2) Update lease data
      a) When updating a lease you are shown all appartments you have access to and asked to chose one.
      b) Next: you are asked to enter how many months are left on the lease, the security deposit, and how many weeks are on the notice period
      c) After all of the user input there will be a success message or an error message.
        - ******* lease update can be viewed ___ **********   
    3) View past visits for certain appartment
      a) When viewing past visits you are promted with all the appartments you can check to see past visits for
      b) Then enter one of those numbers
      c) If no visits are registered under that number it will say that. If there are visits, they will be displayed.
        - This is a way to check what was done was added to the database in part a 
    4) Record move out date
      a) You are given options of appartments that you have and are not for sale (I assumed if they were for sale, there would be nobody to move out)
      b) Enter an appartment number
      c) Enter year, month, and day (similar format to recording a visit, just on seperate lines and no '/' characters).
      d) Success/error message will be printed out
    5) View lease information on particular appartment
      a) the list of appartments is displayed and then you chose a number that you would like to see the lease information
      b) enter appartment number (** some appartments don't have lease information tied to them so there will be some that do not return anything)
         - in the case that there is no lease, that will be displayed
      c) if there is lease information, it will be displayed and then the user will redirected to the main menu.
    6) Exit PM interface
      a) after entering 6, interface is exited and returns to main menu.
    7) Not a number 1-6
      a) If a number (or anything else) is entered that is not a number 1-5 an error message will print out and it will return to the main menu.
    
 Tenant Interface
  - Step 1: The first step is to enter a tenant ID (USER CAN PRETEND TO BE: 2907, 8147, 8155, 2799)
      - tenant ID 2907 just has a utility payment due
      - tenant ID 8147 and 8155 have both rent and utility payments due
      - tenant ID 2799 does not have any payments due
      - if you would like to use other tenants, simply call "select tenant_id from tenant" in Oracle to see other tenant IDs that can be used
  - Step 2: Chose an option (below I will walk through all of the options)
    1) Check payment status 
        a) After selecting 1, the payment status will be displayed. There is nothing else that occurs for this option.
	2) Make payment
        a) After selecting 2, you will be promted to enter 'U' or 'R' for rent or utility payment
        b) Next, enter 'CC' or 'VENMO' for payment option
        c) based off of your previous response for part B you will be asked to add aditiional information
             - for CC: credit card number, expliration date, security code (format of how to enter is provided in the instruction)
             - for VENMO: senders first and last name, recievers first and last name (I know this is not enough if it were actually being used in Venmo but I thought it fufilled the need for the project)
        d) After entering the information requested, there will be either a success/failure message. You can view the payment history in the BM interface option 5
           - if you make a payment when one is not required the amount for the payment will be 0 in the BM interface (it will still recieive a transaction ID however)
	3) Add tenant to lease
        a) After selecting 3, you will be promted for the new tenants first name and last name
        b) Then you will recieive a confirmation that the tenant was added
           - the added tenant can be viewed in Tenant interface option 4
    4) View tenants on your lease
        a) After selecting 4, all tenants on your lease will be printed out
    5) Update personal data
        a) After selecting 5, you will be promted to enter your new first name and last name for the tenant
        b) After you enter, there will be a success statement
    6) Exit tenant interafce
        a) Exit interface back to main menu
    7) Anything other than 1-6
        a) error statement and return to main menu

 Business Manager (BM) Interface
  - Step 1: Enter an action you would like to do (will walk through actions below)
       - This interface is read only, so nothing done in this interface will update or insert data in any table in the database
    1) View a property
        a) Enter the property ID and information about the property will be displayed 
	2) View appartments at a certain property
        a) Enter property ID and see all appartments and information about them at that property (Can see move out date if set)
	3) View visits for a certain appartment
        a) Enter property ID and appartment number
        b) This can also be checked in the PM interface in option 3. However, I still figured it was important information to have a BM.
    4) Check move out date for certain appartment
        a) enter property ID and appartment number to see if there is a move out date set
        b) after entering move out date in PM interface option 4, this is a way to check that it was logged by entering the same property ID and appartment number
    5) View payment history for a certain tenant
        a) enter tenant ID and view payment hitstory from that tenant
          - I did not add the CC/VENMO part of the payment information (even though it is in the database) since I did not think it would be important to see
          - the data avalible is the transaction ID and the amount of the payment
    6) Exit BM interafce
        a) if 6 is entered, exit the interface
    7) If a number not 1-6 is entered an error is thrown and the program returns to the main menu


