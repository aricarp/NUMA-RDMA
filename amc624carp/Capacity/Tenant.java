public class Tenant {
    private String username;
    private String password;
    
    public Tenant(){

    }

    public Tenant(String username, String password){
        this.username = username;
        this.password = password;
    }

    public boolean checkValidTenant(String username, String password){
        if(username.equals("TenantUser") && password.equals("TenantPassWD!"))
            return true;
        return false;
    }

    public void optionMenu(){
        System.out.println("Select an action: ");
		System.out.println("  i.   Check payment status ");
		System.out.println("  ii.  Make payment");
		System.out.println("  iii. Add tenant to lease");
        System.out.println("  iv.  View tenants on your lease");
        System.out.println("  v.   Update personal data");
        System.out.println("  vi.  Exit tenant interafce");
    }

}