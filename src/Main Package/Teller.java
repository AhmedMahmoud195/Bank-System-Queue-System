package MainPackage;

public class Teller extends Person {
    private String employeeID;
    private int experienceYears;

    public Teller(String name, String nationalID, String phone, String employeeID, int experienceYears) {
        super(name, nationalID, phone);
        this.employeeID = employeeID;
        this.experienceYears = experienceYears;
    }

    public String getEmployeeID() { return employeeID; }

    // Fulfilling the abstract method contract
    @Override
    public String getRole() {
        return "Bank Teller";
    }

    // Overriding the interface method to include Employee ID
    @Override
    public String getDisplayDetails() {
        return super.getDisplayDetails() + " | Employee ID: " + employeeID;
    }
}
