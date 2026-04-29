

import java.util.ArrayList;
import java.util.List;

public class QueueManager {
    private List<Ticket> waitingList;

    public QueueManager() {
        waitingList = new ArrayList<>();
    }

    public void addTicket(Ticket t) {
        waitingList.add(t);
        sortQueue(); // Automatically sorts perfectly every time a ticket is added
    }

    public void sortQueue() {
        // Bulletproof sorting logic:
        waitingList.sort((t1, t2) -> {
            if (t1.getPriority() != t2.getPriority()) {
                // Priority 2 (VIP) ALWAYS comes before Priority 1 (REG)
                return Integer.compare(t2.getPriority(), t1.getPriority()); 
            }
            // If they have the same priority, the lower Ticket Number goes first
            return Integer.compare(t1.getTicketNo(), t2.getTicketNo());
        });
    }

    public Ticket callNext() {
        if (waitingList.isEmpty()) return null;
        return waitingList.remove(0);
    }

    public List<Ticket> getWaitingList() {
        return new ArrayList<>(waitingList);
    }
}
