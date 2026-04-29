package MainPackage;

import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;

public class BankGUI extends Application {

    private QueueManager qManager = new QueueManager();
    private int ticketCounterID = 1;

    // Visual Containers
    private VBox visualQueueList;
    private VBox nowServingContainer;
    private Label servingNameLabel;
    private Label servingTicketLabel;

    @Override
    public void start(Stage primaryStage) {
        DatabaseHelper.initializeDatabase();
        primaryStage.setTitle("Premium FinTech Queue Manager");

        // MAIN LAYOUT (Split into Left Controls and Right Visuals)
        HBox root = new HBox(30);
        root.setPadding(new Insets(40));
        root.setStyle("-fx-background-color: #0f172a; -fx-font-family: 'Segoe UI', sans-serif;");

        // ================= LEFT PANEL: CONTROLS =================
        VBox leftPanel = new VBox(20);
        leftPanel.setPrefWidth(350);
        leftPanel.setStyle("-fx-background-color: #1e293b; -fx-background-radius: 15; -fx-border-color: #334155; -fx-border-radius: 15; -fx-border-width: 1;");
        leftPanel.setPadding(new Insets(30));

        Label headerLabel = new Label("Queue Controls");
        headerLabel.setStyle("-fx-text-fill: white; -fx-font-size: 24px; -fx-font-weight: bold;");

        TextField nameInput = new TextField();
        nameInput.setPromptText("Enter Customer Name...");
        nameInput.setStyle("-fx-background-color: #0f172a; -fx-text-fill: white; -fx-prompt-text-fill: #64748b; -fx-padding: 10; -fx-background-radius: 8; -fx-border-color: #334155; -fx-border-radius: 8;");

        CheckBox vipCheckBox = new CheckBox("VIP Customer (Priority Status)");
        vipCheckBox.setStyle("-fx-text-fill: #fbbf24; -fx-font-weight: bold;"); // Gold accent

        Button addBtn = new Button("Add to Queue");
        addBtn.setMaxWidth(Double.MAX_VALUE);
        addBtn.setStyle("-fx-background-color: #3b82f6; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 12; -fx-background-radius: 8; -fx-cursor: hand;");

        Button callNextBtn = new Button("Call Next Customer");
        callNextBtn.setMaxWidth(Double.MAX_VALUE);
        callNextBtn.setStyle("-fx-background-color: #10b981; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 12; -fx-background-radius: 8; -fx-cursor: hand;");

        leftPanel.getChildren().addAll(headerLabel, nameInput, vipCheckBox, addBtn, new Region(), callNextBtn);
        VBox.setVgrow(leftPanel.getChildren().get(4), Priority.ALWAYS); // Spacer

        // ================= RIGHT PANEL: VISUALS =================
        VBox rightPanel = new VBox(30);
        rightPanel.setPrefWidth(400);

        // "Now Serving" Banner
        nowServingContainer = new VBox(10);
        nowServingContainer.setAlignment(Pos.CENTER);
        nowServingContainer.setPadding(new Insets(20));
        nowServingContainer.setStyle("-fx-background-color: #1e293b; -fx-background-radius: 15; -fx-border-color: #10b981; -fx-border-width: 2; -fx-border-radius: 15;");
        
        Label servingHeader = new Label("NOW SERVING");
        servingHeader.setStyle("-fx-text-fill: #10b981; -fx-font-weight: bold; -fx-font-size: 14px;");
        servingNameLabel = new Label("Awaiting Customers...");
        servingNameLabel.setStyle("-fx-text-fill: white; -fx-font-size: 28px; -fx-font-weight: bold;");
        servingTicketLabel = new Label("--");
        servingTicketLabel.setStyle("-fx-text-fill: #94a3b8; -fx-font-size: 16px;");
        
        nowServingContainer.getChildren().addAll(servingHeader, servingNameLabel, servingTicketLabel);

        // Animated Queue List
        Label queueHeader = new Label("Current Queue");
        queueHeader.setStyle("-fx-text-fill: #94a3b8; -fx-font-size: 18px; -fx-font-weight: bold;");
        
        visualQueueList = new VBox(15); // 15px spacing between cards
        
        rightPanel.getChildren().addAll(nowServingContainer, queueHeader, visualQueueList);

        // ================= EVENT HANDLERS =================

        addBtn.setOnAction(e -> {
            String name = nameInput.getText();
            if (name.trim().isEmpty()) return;

            int priority = vipCheckBox.isSelected() ? 1 : 2;
            Account newAcc = new Account("ACC" + ticketCounterID, 1000.0);
            Customer newCust = new Customer(name, "ID" + ticketCounterID, "000", newAcc);
            Ticket newTicket = new Ticket(ticketCounterID, newCust, priority);
            
            qManager.addTicket(newTicket);
            DatabaseHelper.saveTicketToDB(name, priority);
            
            addCardToVisualQueue(newTicket); // The Magic Animation Method
            
            ticketCounterID++;
            nameInput.clear();
            vipCheckBox.setSelected(false);
        });

        callNextBtn.setOnAction(e -> {
            Ticket t = qManager.callNext();
            if (t != null) {
                updateNowServing(t);
                removeFirstCard(); // Animate out
            }
        });

        root.getChildren().addAll(leftPanel, rightPanel);
        Scene scene = new Scene(root, 850, 650);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // ================= ANIMATION & UI LOGIC =================

    private void addCardToVisualQueue(Ticket ticket) {
        // Build the physical card UI
        HBox card = new HBox(20);
        card.setAlignment(Pos.CENTER_LEFT);
        card.setPadding(new Insets(15, 20, 15, 20));
        
        String borderColor = ticket.getPriority() == 1 ? "#fbbf24" : "#334155"; // Gold for VIP, Grey for Normal
        card.setStyle("-fx-background-color: #1e293b; -fx-background-radius: 10; -fx-border-color: " + borderColor + "; -fx-border-radius: 10; -fx-border-width: 2;");
        
        DropShadow shadow = new DropShadow(10, Color.rgb(0,0,0,0.3));
        card.setEffect(shadow);

        Label idLbl = new Label("#" + ticket.getTicketNo());
        idLbl.setStyle("-fx-text-fill: #94a3b8; -fx-font-size: 18px; -fx-font-weight: bold;");
        
        Label nameLbl = new Label(ticket.getCustomer().getName());
        nameLbl.setStyle("-fx-text-fill: white; -fx-font-size: 18px; -fx-font-weight: bold;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        Label statusLbl = new Label(ticket.getPriority() == 1 ? "VIP" : "REG");
        statusLbl.setStyle("-fx-text-fill: " + borderColor + "; -fx-font-weight: bold; -fx-font-size: 14px;");

        card.getChildren().addAll(idLbl, nameLbl, spacer, statusLbl);

        // Sorting Logic: Where does it go visually?
        int insertIndex = visualQueueList.getChildren().size();
        if (ticket.getPriority() == 1) { // If VIP, push it to the top (under other VIPs)
            insertIndex = 0;
            // Simple visual sort: find the first non-VIP card
            for (int i = 0; i < visualQueueList.getChildren().size(); i++) {
                HBox existingCard = (HBox) visualQueueList.getChildren().get(i);
                Label existingStatus = (Label) existingCard.getChildren().get(3);
                if (existingStatus.getText().equals("REG")) {
                    insertIndex = i;
                    break;
                } else { insertIndex = i + 1; }
            }
        }
        visualQueueList.getChildren().add(insertIndex, card);

        // Animation: Slide in from the right and fade in
        card.setTranslateX(100);
        card.setOpacity(0);
        
        TranslateTransition tt = new TranslateTransition(Duration.millis(300), card);
        tt.setToX(0);
        FadeTransition ft = new FadeTransition(Duration.millis(300), card);
        ft.setToValue(1);
        
        ParallelTransition pt = new ParallelTransition(tt, ft);
        pt.play();
    }

    private void removeFirstCard() {
        if (visualQueueList.getChildren().isEmpty()) return;
        
        HBox topCard = (HBox) visualQueueList.getChildren().get(0);
        
        // Animation: Slide left and fade out
        TranslateTransition tt = new TranslateTransition(Duration.millis(250), topCard);
        tt.setToX(-100);
        FadeTransition ft = new FadeTransition(Duration.millis(250), topCard);
        ft.setToValue(0);
        
        ParallelTransition pt = new ParallelTransition(tt, ft);
        pt.setOnFinished(event -> visualQueueList.getChildren().remove(topCard)); // Remove after animation finishes
        pt.play();
    }

    private void updateNowServing(Ticket t) {
        servingNameLabel.setText(t.getCustomer().getName());
        servingTicketLabel.setText("Ticket #" + t.getTicketNo() + " (" + (t.getPriority() == 1 ? "VIP" : "Regular") + ")");
        
        // Quick pulse animation on the banner to grab attention
        FadeTransition ft = new FadeTransition(Duration.millis(150), nowServingContainer);
        ft.setFromValue(0.5);
        ft.setToValue(1.0);
        ft.play();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
