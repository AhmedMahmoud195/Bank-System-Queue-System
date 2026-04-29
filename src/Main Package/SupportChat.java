package MainPackage;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class SupportChat extends Application {

    private TextArea chatArea = new TextArea();
    private TextField messageInput = new TextField();
    private PrintWriter out;
    private BufferedReader in;
    private Socket socket;
    private ServerSocket serverSocket;
    private String userName = "Unknown";

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Bank Support Chat (Networking Bonus)");

        // Setup Chat Display
        chatArea.setEditable(false);
        chatArea.setPrefHeight(300);
        chatArea.setStyle("-fx-control-inner-background: #f4f4f4; -fx-font-family: 'Consolas';");

        // Buttons
        Button hostBtn = new Button("1. Start Server (Teller)");
        Button connectBtn = new Button("2. Connect (Customer)");
        Button sendBtn = new Button("Send");

        hostBtn.setStyle("-fx-base: #28a745;"); // Green
        connectBtn.setStyle("-fx-base: #007bff;"); // Blue

        // Layouts
        HBox connectionBox = new HBox(10, hostBtn, connectBtn);
        HBox inputBox = new HBox(10, messageInput, sendBtn);
        
        VBox root = new VBox(10, connectionBox, chatArea, inputBox);
        root.setPadding(new Insets(15));

        // Event Listeners
        hostBtn.setOnAction(e -> startServer());
        connectBtn.setOnAction(e -> connectToServer());
        
        // Send message on button click OR pressing Enter key
        sendBtn.setOnAction(e -> sendMessage());
        messageInput.setOnAction(e -> sendMessage()); 

        Scene scene = new Scene(root, 400, 450);
        primaryStage.setScene(scene);
        
        // Safely close network ports when window is closed
        primaryStage.setOnCloseRequest(e -> closeConnections());
        primaryStage.show();
    }

    // USER 1 LOGIC: Opens a port and waits
    private void startServer() {
        userName = "Teller";
        chatArea.appendText(">> Starting server on port 5000...\n");
        
        // Network tasks MUST run on a separate background thread so the GUI doesn't freeze
        new Thread(() -> {
            try {
                serverSocket = new ServerSocket(5000);
                Platform.runLater(() -> chatArea.appendText(">> Waiting for a customer to connect...\n"));
                
                socket = serverSocket.accept(); // Code pauses here until someone connects
                Platform.runLater(() -> chatArea.appendText(">> Customer connected! You can now chat.\n"));
                
                setupStreams();
            } catch (Exception ex) {
                Platform.runLater(() -> chatArea.appendText(">> Server Error: " + ex.getMessage() + "\n"));
            }
        }).start();
    }

    // USER 2 LOGIC: Connects to the open port
    private void connectToServer() {
        userName = "Customer";
        chatArea.appendText(">> Connecting to Teller...\n");
        
        new Thread(() -> {
            try {
                socket = new Socket("localhost", 5000);
                Platform.runLater(() -> chatArea.appendText(">> Connected to Teller! You can now chat.\n"));
                setupStreams();
            } catch (Exception ex) {
                Platform.runLater(() -> chatArea.appendText(">> Connection Error: Make sure the Server is running first!\n"));
            }
        }).start();
    }

    // CORE LOGIC: Reading incoming messages
    private void setupStreams() throws Exception {
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        String incomingMessage;
        // Continuously listen for new messages
        while ((incomingMessage = in.readLine()) != null) {
            String finalMessage = incomingMessage;
            
            // Platform.runLater safely updates the JavaFX UI from the background thread
            Platform.runLater(() -> chatArea.appendText(finalMessage + "\n"));
        }
    }

    // CORE LOGIC: Sending outgoing messages
    private void sendMessage() {
        String text = messageInput.getText();
        if (!text.isEmpty() && out != null) {
            out.println(userName + ": " + text); // Send across the network
            chatArea.appendText("Me: " + text + "\n"); // Show locally
            messageInput.clear();
        }
    }

    private void closeConnections() {
        try {
            if (out != null) out.close();
            if (in != null) in.close();
            if (socket != null) socket.close();
            if (serverSocket != null) serverSocket.close();
        } catch (Exception e) {
            System.out.println("Error closing streams.");
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
