package MainPackage;

public class Customer extends Person {
    private Account account;

    public Customer(String name, String nationalID, String phone, Account account) {
        super(name, nationalID, phone); // Passes data to the abstract Person class
        this.account = account;
    }

    public Account getAccount() { return account; }

    // Fulfilling the abstract method contract
    @Override
    public String getRole() {
        return "Customer";
    }

    // Overriding the interface method to include Account info
    @Override
    public String getDisplayDetails() {
        return super.getDisplayDetails() + " | Account No: " + account.getAccountNumber();
    }
}
