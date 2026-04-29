//package MainPackage;

public class Account {
    private String accountNumber;
    private double balance;

    public Account(String accountNumber, double balance) {
        this.accountNumber = accountNumber;
        this.balance = balance;
    }

    public void deposit(double amount) {
        // Exception Handling: Prevent negative deposits
        if (amount <= 0) {
            throw new IllegalArgumentException("Deposit amount must be greater than zero.");
        }
        balance += amount;
        System.out.println("Deposited: " + amount);
    }

    public void deposit(double amount, String note) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Deposit amount must be greater than zero.");
        }
        balance += amount;
        System.out.println("Deposited: " + amount + " - Reason: " + note);
    }

    public void withdraw(double amount) {
        // Exception Handling: Prevent overdrafts or negative withdrawals
        if (amount <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be greater than zero.");
        }
        if (amount > balance) {
            // Custom exception logic
            throw new IllegalStateException("Insufficient funds for withdrawal of: " + amount);
        }
        balance -= amount;
        System.out.println("Withdrawn: " + amount);
    }

    public String getAccountNumber() { return accountNumber; }
    public double getBalance() { return balance; }
}
