//package MainPackage;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class BankGUI extends Application {
    
    private QueueManager qManager = new QueueManager();
    private int ticketCounterID = 1;

    @Override
    public void start(Stage primaryStage) {
        // Initialize Database
        DatabaseHelper.initializeDatabase();

        primaryStage.setTitle("Bank Queue System");

        // UI Components
        Label nameLabel = new Label("Customer Name:");
        TextField nameInput = new TextField();
        
        CheckBox vipCheckBox = new CheckBox("Is VIP Customer?");
        
        Button addTicketBtn = new Button("Add Customer to Queue");
        Button callNextBtn = new Button("Call Next Customer");
        
        TextArea consoleOutput = new TextArea();
        consoleOutput.setEditable(false);

        // Event Handlers
        addTicketBtn.setOnAction(e -> {
            try {
                String name = nameInput.getText();
                if (name.trim().isEmpty()) {
                    throw new IllegalArgumentException("Name cannot be empty.");
                }

                int priority = vipCheckBox.isSelected() ? 1 : 2;
                
                // Create dummy account and customer
                Account newAcc = new Account("ACC" + ticketCounterID, 1000.0);
                Customer newCust = new Customer(name, "ID" + ticketCounterID, "0000000", newAcc);
                Ticket newTicket = new Ticket(ticketCounterID, newCust, priority);
                
                qManager.addTicket(newTicket);
                DatabaseHelper.saveTicketToDB(name, priority); // Save to DB
                
                consoleOutput.appendText("Ticket #" + ticketCounterID + " created for " + name + "\n");
                ticketCounterID++;
                nameInput.clear();
                vipCheckBox.setSelected(false);
                
            } catch (Exception ex) { // Catching the exception we threw!
                consoleOutput.appendText("Error: " + ex.getMessage() + "\n");
            }
        });

        callNextBtn.setOnAction(e -> {
            Ticket t = qManager.callNext();
            if (t != null) {
                consoleOutput.appendText("Serving Ticket #" + t.getTicketNo() + " (" + t.getOwner().getName() + ")\n");
            } else {
                consoleOutput.appendText("Queue is empty.\n");
            }
        });

        // Layout setup
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));
        layout.getChildren().addAll(nameLabel, nameInput, vipCheckBox, addTicketBtn, callNextBtn, consoleOutput);

        Scene scene = new Scene(layout, 400, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
