package MainPackage;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class BankGUI extends Application {
    
    private QueueManager qManager = new QueueManager();
    private int ticketCounterID = 1;

    @Override
    public void start(Stage primaryStage) {
        DatabaseHelper.initializeDatabase();
        primaryStage.setTitle("Tactical Queue Monitor v1.0");

        // UI Components
        Label headerLabel = new Label("SYSTEM STATUS: ACTIVE QUEUE");
        headerLabel.setStyle("-fx-text-fill: #39d353; -fx-font-weight: bold; -fx-font-size: 16px; -fx-font-family: 'Consolas';");

        Label nameLabel = new Label("TARGET IDENTIFIER (Name):");
        nameLabel.setStyle("-fx-text-fill: #c9d1d9; -fx-font-family: 'Consolas';");
        
        TextField nameInput = new TextField();
        nameInput.setStyle("-fx-background-color: #161b22; -fx-text-fill: #39d353; -fx-border-color: #30363d; -fx-font-family: 'Consolas';");
        
        CheckBox vipCheckBox = new CheckBox("FLAG: HIGH PRIORITY (VIP)");
        vipCheckBox.setStyle("-fx-text-fill: #ff7b72; -fx-font-family: 'Consolas';"); // Red for high priority
        
        Button addTicketBtn = new Button("[ INJECT TARGET TO QUEUE ]");
        addTicketBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: #39d353; -fx-border-color: #39d353; -fx-font-family: 'Consolas'; -fx-cursor: hand;");
        
        Button callNextBtn = new Button("[ EXECUTE: CALL NEXT ]");
        callNextBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: #58a6ff; -fx-border-color: #58a6ff; -fx-font-family: 'Consolas'; -fx-cursor: hand;"); // Blue for execution
        
        TextArea consoleOutput = new TextArea();
        consoleOutput.setEditable(false);
        // Styling the text area to look like a terminal
        consoleOutput.setStyle("-fx-control-inner-background: #010409; -fx-text-fill: #39d353; -fx-font-family: 'Consolas'; -fx-border-color: #30363d;");

        // Event Handlers
        addTicketBtn.setOnAction(e -> {
            try {
                String name = nameInput.getText();
                if (name.trim().isEmpty()) {
                    throw new IllegalArgumentException("ERR: IDENTIFIER CANNOT BE NULL.");
                }

                int priority = vipCheckBox.isSelected() ? 1 : 2;
                
                Account newAcc = new Account("ACC" + ticketCounterID, 1000.0);
                Customer newCust = new Customer(name, "ID" + ticketCounterID, "0000000", newAcc);
                Ticket newTicket = new Ticket(ticketCounterID, newCust, priority);
                
                qManager.addTicket(newTicket);
                DatabaseHelper.saveTicketToDB(name, priority);
                
                String priorityStr = priority == 1 ? "[VIP]" : "[REG]";
                consoleOutput.appendText(">> LOG: Ticket #" + ticketCounterID + " " + priorityStr + " allocated to " + name + "\n");
                ticketCounterID++;
                nameInput.clear();
                vipCheckBox.setSelected(false);
                
            } catch (Exception ex) {
                consoleOutput.appendText(">> FAULT: " + ex.getMessage() + "\n");
            }
        });

        callNextBtn.setOnAction(e -> {
            Ticket t = qManager.callNext();
            if (t != null) {
                consoleOutput.appendText(">> ACTION: Processing Ticket #" + t.getTicketNo() + " (ID: " + t.getCustomer().getName() + ")\n");
            } else {
                consoleOutput.appendText(">> STATUS: Queue empty. Awaiting input.\n");
            }
        });

        // Layout setup
        VBox layout = new VBox(15);
        layout.setPadding(new Insets(25));
        layout.setStyle("-fx-background-color: #0d1117;"); // Dark background
        layout.getChildren().addAll(headerLabel, nameLabel, nameInput, vipCheckBox, addTicketBtn, callNextBtn, consoleOutput);

        Scene scene = new Scene(layout, 500, 550);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
