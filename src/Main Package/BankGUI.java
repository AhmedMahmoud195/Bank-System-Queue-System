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
import javafx.stage.Stage;
import javafx.util.Duration;

public class BankGUI extends Application {

    private QueueManager qManager = new QueueManager();
    private int ticketCounterID = 1;

    private VBox visualQueueList;
    private VBox nowServingContainer;
    private Label servingNameLabel;
    private Label servingTicketLabel;

    @Override
    public void start(Stage primaryStage) {
        DatabaseHelper.initializeDatabase();
        primaryStage.setTitle("Premium FinTech Queue Manager");

        HBox root = new HBox(30);
        root.setPadding(new Insets(40));
        root.setStyle("-fx-background-color: #0f172a; -fx-font-family: 'Segoe UI', sans-serif;");

        // LEFT PANEL
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
        vipCheckBox.setStyle("-fx-text-fill: #fbbf24; -fx-font-weight: bold;");

        Button addBtn = new Button("Add to Queue");
        addBtn.setMaxWidth(Double.MAX_VALUE);
        addBtn.setStyle("-fx-background-color: #3b82f6; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 12; -fx-background-radius: 8; -fx-cursor: hand;");

        Button callNextBtn = new Button("Call Next Customer");
        callNextBtn.setMaxWidth(Double.MAX_VALUE);
        callNextBtn.setStyle("-fx-background-color: #10b981; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 12; -fx-background-radius: 8; -fx-cursor: hand;");

        leftPanel.getChildren().addAll(headerLabel, nameInput, vipCheckBox, addBtn, new Region(), callNextBtn);
        VBox.setVgrow(leftPanel.getChildren().get(4), Priority.ALWAYS);

        // RIGHT PANEL
        VBox rightPanel = new VBox(30);
        rightPanel.setPrefWidth(400);

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

        Label queueHeader = new Label("Current Queue");
        queueHeader.setStyle("-fx-text-fill: #94a3b8; -fx-font-size: 18px; -fx-font-weight: bold;");
        
        visualQueueList = new VBox(15);
        
        rightPanel.getChildren().addAll(nowServingContainer, queueHeader, visualQueueList);

        // EVENT HANDLERS
        addBtn.setOnAction(e -> {
            String name = nameInput.getText();
            if (name.trim().isEmpty()) return;

            // Match original backend logic (VIP = 2, REG = 1)
            int priority = vipCheckBox.isSelected() ? 2 : 1;
            Account newAcc = new Account("ACC" + ticketCounterID, 1000.0);
            Customer newCust = new Customer(name, "ID" + ticketCounterID, "000", newAcc);
            Ticket newTicket = new Ticket(ticketCounterID, newCust, priority);
            
            qManager.addTicket(newTicket);
            qManager.sortQueue(); // Explicitly sort the backend to keep it perfectly aligned with UI
            DatabaseHelper.saveTicketToDB(name, priority);
            
            // Check the backend index, and force the visual card into that exact slot
            int exactIndex = qManager.getWaitingList().indexOf(newTicket);
            addCardToVisualQueue(newTicket, exactIndex);
            
            ticketCounterID++;
            nameInput.clear();
            vipCheckBox.setSelected(false);
        });

        callNextBtn.setOnAction(e -> {
            Ticket t = qManager.callNext();
            if (t != null) {
                updateNowServing(t);
                removeFirstCard();
            }
        });

        root.getChildren().addAll(leftPanel, rightPanel);
        Scene scene = new Scene(root, 850, 650);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void addCardToVisualQueue(Ticket ticket, int insertIndex) {
        HBox card = new HBox(20);
        card.setAlignment(Pos.CENTER_LEFT);
        card.setPadding(new Insets(15, 20, 15, 20));
        
        // VIP priority is 2
        String borderColor = ticket.getPriority() == 2 ? "#fbbf24" : "#334155";
        card.setStyle("-fx-background-color: #1e293b; -fx-background-radius: 10; -fx-border-color: " + borderColor + "; -fx-border-radius: 10; -fx-border-width: 2;");
        
        DropShadow shadow = new DropShadow(10, Color.rgb(0,0,0,0.3));
        card.setEffect(shadow);

        Label idLbl = new Label("#" + ticket.getTicketNo());
        idLbl.setStyle("-fx-text-fill: #94a3b8; -fx-font-size: 18px; -fx-font-weight: bold;");
        
        Label nameLbl = new Label(ticket.getOwner().getName()); // FIXED: getOwner()
        nameLbl.setStyle("-fx-text-fill: white; -fx-font-size: 18px; -fx-font-weight: bold;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        Label statusLbl = new Label(ticket.getPriority() == 2 ? "VIP" : "REG");
        statusLbl.setStyle("-fx-text-fill: " + borderColor + "; -fx-font-weight: bold; -fx-font-size: 14px;");

        card.getChildren().addAll(idLbl, nameLbl, spacer, statusLbl);

        // Place the card exactly where the backend queue says it belongs
        if (insertIndex >= 0 && insertIndex <= visualQueueList.getChildren().size()) {
            visualQueueList.getChildren().add(insertIndex, card);
        } else {
            visualQueueList.getChildren().add(card);
        }

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
        
        TranslateTransition tt = new TranslateTransition(Duration.millis(250), topCard);
        tt.setToX(-100);
        FadeTransition ft = new FadeTransition(Duration.millis(250), topCard);
        ft.setToValue(0);
        
        ParallelTransition pt = new ParallelTransition(tt, ft);
        pt.setOnFinished(event -> visualQueueList.getChildren().remove(topCard));
        pt.play();
    }

    private void updateNowServing(Ticket t) {
        servingNameLabel.setText(t.getOwner().getName()); // FIXED: getOwner()
        servingTicketLabel.setText("Ticket #" + t.getTicketNo() + " (" + (t.getPriority() == 2 ? "VIP" : "Regular") + ")");
        
        FadeTransition ft = new FadeTransition(Duration.millis(150), nowServingContainer);
        ft.setFromValue(0.5);
        ft.setToValue(1.0);
        ft.play();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
