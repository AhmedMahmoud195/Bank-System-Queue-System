

public class Ticket {
    private int ticketNo;
    private int priority;
    private Customer owner;
    private String serviceType; // NEW: Stores what the customer wants to do

    // Original Constructor (Keeps your Main.java from breaking)
    public Ticket(int ticketNo, Customer owner, int priority) {
        this.ticketNo = ticketNo;
        this.owner = owner;
        this.priority = priority;
        this.serviceType = "General Service"; // Default value
    }

    // Overloaded Constructor (Used by our new Premium GUI)
    public Ticket(int ticketNo, Customer owner, int priority, String serviceType) {
        this.ticketNo = ticketNo;
        this.owner = owner;
        this.priority = priority;
        this.serviceType = serviceType;
    }

    public int getTicketNo() { return ticketNo; }
    public int getPriority() { return priority; }
    public Customer getOwner() { return owner; }
    public String getServiceType() { return serviceType; } // NEW
}
