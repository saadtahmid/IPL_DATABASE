package client;

import entity.*;
import network.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import util.NetworkUtil;

import java.io.IOException;
import java.util.List;

public class Client extends Application {

    private Stage stage;
    private NetworkUtil networkUtil;
    private TransferWindowRefreshThread refreshThread;

    private void connectToServer() throws IOException {
        String serverAddress = "127.0.0.1";
        int serverPort = 45045;
        networkUtil = new NetworkUtil(serverAddress, serverPort);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;
        connectToServer();
        showLoginPage();
    }

    private void showLoginPage() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/fxml/clubLoginWindow.fxml"));
        Parent root = fxmlLoader.load();

        ClubLoginWindowController controller = fxmlLoader.getController();
        controller.setClient(this);
        controller.init();

        Scene scene = new Scene(root);

        stage.setOnCloseRequest(e -> stage.close());

        stage.setTitle("Home");
        stage.setX(375);
        stage.setY(80);
        stage.setScene(scene);
        stage.show();
    }

    private void showClubHomePage(String clubName) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/fxml/clubHomeWindow.fxml"));

        Parent root = fxmlLoader.load();

        ClubHomeWindowController controller = fxmlLoader.getController();
        controller.init(this, clubName);

        Scene scene = new Scene(root);

        stage.setOnCloseRequest(e -> {
            e.consume();
            logoutClub(clubName);
        });

        stage.setTitle(clubName);
        stage.setScene(scene);
        stage.setX(10);
        stage.setY(10);
        stage.show();
    }
    
    // Method to handle club login
public void loginClub(String username) {
    LoginInfo loginInfo = new LoginInfo(MessageHeader.LOGIN, username);
    try {
        // Send login request to the server
        networkUtil.write(loginInfo);
        // Read the response from the server
        Object obj = networkUtil.read();
        // Check if the response is a boolean indicating login success
        if (obj instanceof Boolean) {
            Boolean b = (Boolean) obj;
            if (b) {
                // Show the club home page if login is successful
                showClubHomePage(username);
            } else {
                // Show an error alert if login is unsuccessful
                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setTitle("Error");
                a.setHeaderText("Login window");
                a.setContentText("Login is unsuccessful.");
                a.showAndWait();
            }
        }
    } catch (IOException | ClassNotFoundException e) {
        e.printStackTrace();
    }
}


    // Method to handle club logout
public void logoutClub(String clubName) {
    try {
        // Send logout request to the server
        networkUtil.write(new Message(MessageHeader.LOGOUT, clubName));
        // Read the response from the server
        Object obj = networkUtil.read();
        // Check if the response is a boolean indicating logout success
        if (obj instanceof Boolean) {
            Boolean b = (Boolean) obj;
            if (b) {
                // Interrupt the refresh thread and show the login page if logout is successful
                interruptRefreshThread();
                showLoginPage();
            } else {
                // Show an error alert if logout is unsuccessful
                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setTitle("Error");
                a.setHeaderText("Logout window");
                a.setContentText("Logout is unsuccessful.");
                a.showAndWait();
            }
        }
    } catch (IOException | ClassNotFoundException e) {
        e.printStackTrace();
    }
}

    // Method to handle selling a player
public boolean sellPlayer(String playerName) {
    try {
        // Send a request to the server to sell the player
        networkUtil.write(new SaleInfo(MessageHeader.SELL, playerName));
        // Read the response from the server
        Object obj = networkUtil.read();
        // Check if the response is a boolean indicating success
        if (obj instanceof Boolean) {
            Boolean b = (Boolean) obj;
            if (!b) {
                // Show an error alert if the player could not be sold
                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setTitle("Error");
                a.setHeaderText(playerName);
                a.setContentText("Player could not be sold!");
                a.showAndWait();
            }
            return b;
        }
    } catch (IOException | ClassNotFoundException e) {
        e.printStackTrace();
    }
    return false;
}

// Method to handle buying a player
public boolean buyPlayer(String playerName, String clubName) {
    try {
        // Send a request to the server to buy the player
        networkUtil.write(new BuyInfo(MessageHeader.BUY, playerName, clubName));
        // Read the response from the server
        Object obj = networkUtil.read();
        // Check if the response is a boolean indicating success
        if (obj instanceof Boolean) {
            Boolean b = (Boolean) obj;
            if (!b) {
                // Show an error alert if the player is unavailable
                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setTitle("Error");
                a.setHeaderText(playerName);
                a.setContentText("Player is unavailable!\nThis player may have already been bought.");
                a.showAndWait();
            }
            return b;
        }
    } catch (IOException | ClassNotFoundException e) {
        e.printStackTrace();
    }
    return false;
}

    // Method to add a player to a club
public boolean addPlayer(Player player) {
    try {
        // Send a request to the server to add the player to the club
        networkUtil.write(new AddInfo(MessageHeader.ADD, player));
        // Read the response from the server
        Object obj = networkUtil.read();
        // Check if the response is a boolean indicating success
        if (obj instanceof Boolean) {
            Boolean b = (Boolean) obj;
            if (!b) {
                // Show an error alert if the player could not be added
                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setTitle("Error");
                a.setHeaderText(player.getName());
                a.setContentText("Player could not be added!");
                a.showAndWait();
            }
            return b;
        }
    } catch (IOException | ClassNotFoundException e) {
        e.printStackTrace();
    }
    return false;
}
// Method to load club information from the server
public Club loadClubFromServer(String clubName) {
    try {
        // Send a request to the server to get the club information
        networkUtil.write(new Message(MessageHeader.CLUB_INFO, clubName));
        // Read the response from the server
        Object obj = networkUtil.read();
        // Check if the response is a Club object
        if (obj instanceof Club) {
            return (Club) obj;
        }
    } catch (IOException | ClassNotFoundException e) {
        e.printStackTrace();
    }
    return null;
}

    // Method to load the list of clubs from the server
public List<?> loadClubList() {
    try {
        // Send a request to the server to get the list of clubs
        networkUtil.write(new Message(MessageHeader.CLUB_LIST, null));
        // Read the response from the server
        Object obj = networkUtil.read();
        // Check if the response is a list
        if (obj instanceof List) {
            return (List<?>) obj;
        }
    } catch (IOException | ClassNotFoundException e) {
        e.printStackTrace();
    }
    return null;
}

    // Method to load the list of players in the transfer window from the server
public List<?> loadTransferList() {
    try {
        // Send a request to the server to get the list of players in the transfer window
        networkUtil.write(new Message(MessageHeader.TRANSFER_WINDOW, null));
        // Read the response from the server
        Object obj = networkUtil.read();
        // Check if the response is a list
        if (obj instanceof List) {
            return (List<?>) obj;
        }
    } catch (IOException | ClassNotFoundException e) {
        e.printStackTrace();
    }
    return null;
}

     public void startRefreshThread(ClubHomeWindowController clubHomeWindowController) {
        refreshThread = new TransferWindowRefreshThread(clubHomeWindowController);
    }

     public void interruptRefreshThread() {
        if (refreshThread != null) refreshThread.getThread().interrupt();
    }

    public NetworkUtil getNetworkUtil() {
        return networkUtil;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
