package MainPackage;

public abstract class Person implements IDisplayable {
    protected String name;
    protected String nationalID;
    protected String phone;

    public Person(String name, String nationalID, String phone) {
        this.name = name;
        this.nationalID = nationalID;
        this.phone = phone;
    }

    public String getName() { return name; }
    public String getNationalID() { return nationalID; }
    public String getPhone() { return phone; }

    // Abstract method: Forces subclasses to define their specific role in the bank
    public abstract String getRole();

    // Implementing the interface method using a Template Pattern
    // This provides a default display format for ALL persons
    @Override
    public String getDisplayDetails() {
        return "[" + getRole() + "] Name: " + name + " | Phone: " + phone;
    }
}
