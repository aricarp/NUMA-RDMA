public class PropManager {
    private String username;
    private String password;
    
    public PropManager(){

    }
    public PropManager(String username, String password){
        this.username = username;
        this.password = password;
    }

    public boolean checkValidPM(String user, String passWd){
        if (user.equals("GoCeltics") && passWd.equals("GoRedSox!"))
            return true;
        return false;
    }

    public void optionMenu(){
        System.out.println("Select an action: ");
		System.out.println("  i.   Record a visit ");
		System.out.println("  ii.  Update lease data");
        System.out.println("  iii. View past visits for certain appartment");
        System.out.println("  iv.  Record move out date");
        System.out.println("  v.   View lease information on particular appartment");
		System.out.println("  vi.  Exit Property Manager Interface");
    }

    public String enterVisit(int property_id, int appartment_number, String date, String first_name, String last_name){
        
        String query = "insert into visits (PROPERTY_ID, APPARTMENT_NUMBER, DATE_OF_VISIT, FIRST_NAME_VISITOR, LAST_NAME_VISITOR) values (" + property_id + ", " + 
        appartment_number + ", TO_DATE('" + date + "', 'yyyy/mm/dd'), '" + first_name + "', '" + last_name + "')"; 
        
        return query;
    }

    public String checkVisit(int property_id, int appartment_number){
        String query = "select * from visits where appartment_number = " + appartment_number + "and property_id = " + property_id;
        return query;
    }

}