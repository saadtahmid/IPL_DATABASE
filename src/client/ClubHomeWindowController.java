package client;

import entity.Club;
import database.Database;
import entity.Player;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.image.ImageView;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ClubHomeWindowController {

    @FXML
    private Label clubNameFirstLine;

    @FXML
    private Label clubNameSecondLine;

    @FXML
    private Button buyPlayerButton;

    @FXML
    private VBox bodyVBox;

    @FXML
    private HBox topBarHBox;
    @FXML
    private ImageView clubImage;
    @FXML
    private TextField searchPlayerNameTextField;

    @FXML
    private Button searchPlayerNameButton;

    @FXML
    private Button resetPlayerNameButton;
    @FXML
    private Button addPlayerButton;

    @FXML
    private MenuButton clubMenuButton;

    @FXML
    private MenuItem usernameMenuItem;

    @FXML
    private HBox listPlayerHBox;

    @FXML
    private VBox playerListVBox;

    // @FXML
    // private ScrollPane scrollPane;
    //
    // @FXML
    // private GridPane gridPane;

    @FXML
    private TreeView<CheckBox> filterTreeCountry;

    @FXML
    private TreeView<CheckBox> filterTreePosition;

    @FXML
    private TextField ageFromTextField;

    @FXML
    private TextField ageToTextField;

    @FXML
    private TextField heightFromTextField;

    @FXML
    private TextField salaryFromTextField;

    @FXML
    private TextField heightToTextField;

    @FXML
    private TextField salaryToTextField;

    @FXML
    private Button applyFiltersButton;

    @FXML
    private Button resetFiltersButton;

    @FXML
    private HBox bottomBarHBox;

    private Club club;
    private String clubName;
    private List<Player> playerListOnDisplay;
    private Client client;

    @FXML
    void showTransferWindow(ActionEvent event) {
        if (buyPlayerButton.getText().equals("Buy Player")) {
            client.startRefreshThread(this);
            buyPlayerButton.setText("Home");
        } else {
            client.interruptRefreshThread();
            buyPlayerButton.setText("Buy Player");
            loadPlayerCards(this.club.getPlayers());
        }

    }

    void loadTransferWindow() {
        List<?> players = this.client.loadTransferList();
        if (players != null) {
            List<Player> playerList = new ArrayList<>();
            for (Object e : players) {
                if (e instanceof Player && !((Player) e).getClub().equals(this.clubName)) {
                    playerList.add((Player) e);
                }
            }
            loadPlayerCards(playerList);
        }

    }

    @FXML
    void searchPlayerByName(ActionEvent event) {
        String playerName = searchPlayerNameTextField.getText().trim();
        Database db = new Database();
        List<Player> players = this.club.getPlayers();
        for (Player player : players) {
            db.addPlayer(player);
        }
        Player player = db.searchByName(playerName);
        List<Player> playerList = new ArrayList<>();
        if (player != null) {
            playerList.add(player);
        }
        loadPlayerCards(playerList);
    }

    @FXML
    void resetPlayerNameTextField(ActionEvent event) {
        searchPlayerNameTextField.setText("");
        loadPlayerCards(club.getPlayers());
    }
    @FXML
    void addPlayer(ActionEvent event){
            try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/addPlayerWindow.fxml"));
            Parent root = fxmlLoader.load();

            AddPlayerWindowController controller = fxmlLoader.getController();
            controller.setClubHomeWindowController(this);

            Stage stage = new Stage();
            controller.setStage(stage);

            stage.setTitle("Add New Player");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    void applyFilters(ActionEvent event) {
        Database db = new Database();
        List<Player> players = this.club.getPlayers();
        for (Player player : players) {
            db.addPlayer(player);
        }
        applyFiltersCountry(db);
        applyFiltersPosition(db);
        applyFiltersAge(db);
        applyFiltersHeight(db);
        applyFiltersSalary(db);

        loadPlayerCards(db.getPlayers());
    }

    private void applyFiltersSalary(Database db) {
        long lo, hi;
        try {
            lo = Long.parseLong(salaryFromTextField.getText());
        } catch (Exception e) {
            lo = 0;
            salaryFromTextField.setText(String.valueOf(lo));
        }
        try {
            hi = Long.parseLong(salaryToTextField.getText());
        } catch (Exception e) {
            hi = club.getPlayersWithMaxSalary().get(0).getWeeklySalary();
            salaryToTextField.setText(String.valueOf(hi));
        }
        db.setPlayers(db.searchBySalaryRange(lo, hi));
    }

    private void applyFiltersHeight(Database db) {
        double lo, hi;
        try {
            lo = Double.parseDouble(heightFromTextField.getText());
        } catch (Exception e) {
            lo = 0;
            heightFromTextField.setText(String.valueOf(lo));
        }
        try {
            hi = Double.parseDouble(heightToTextField.getText());
        } catch (Exception e) {
            hi = club.getPlayersWithMaxHeight().get(0).getHeight();
            heightToTextField.setText(String.valueOf(hi));
        }
        db.setPlayers(db.searchByHeight(lo, hi));
    }

    private void applyFiltersAge(Database db) {
        int lo, hi;
        try {
            lo = Integer.parseInt(ageFromTextField.getText());
        } catch (Exception e) {
            lo = 0;
            ageFromTextField.setText(String.valueOf(lo));
        }
        try {
            hi = Integer.parseInt(ageToTextField.getText());
        } catch (Exception e) {
            hi = club.getPlayersWithMaxAge().get(0).getAge();
            ageToTextField.setText(String.valueOf(hi));
        }
        db.setPlayers(db.searchByAge(lo, hi));
    }

    private void applyFiltersPosition(Database db) {
        for (TreeItem<CheckBox> item : filterTreePosition.getRoot().getChildren()) {
            if (!item.getValue().isSelected()) {
                // remove players playing at this position
                for (Player player : db.searchByPosition(item.getValue().getText())) {
                    db.getPlayers().remove(player);
                }
            }
        }
    }

    private void applyFiltersCountry(Database db) {
        for (TreeItem<CheckBox> item : filterTreeCountry.getRoot().getChildren()) {
            if (!item.getValue().isSelected()) {
                // remove players from this country
                for (Player player : db.searchByCountry(item.getValue().getText())) {
                    db.getPlayers().remove(player);
                }
            }
        }
    }

    @FXML
    void resetFilters(ActionEvent event) {
        // reset countries
        for (TreeItem<CheckBox> item : filterTreeCountry.getRoot().getChildren()) {
            if (!item.getValue().isSelected()) {
                item.getValue().setSelected(true);
            }
        }

        // reset position
        for (TreeItem<CheckBox> item : filterTreePosition.getRoot().getChildren()) {
            if (!item.getValue().isSelected()) {
                item.getValue().setSelected(true);
            }
        }

        // reset text fields
        ageFromTextField.setText("");
        ageToTextField.setText("");

        heightFromTextField.setText("");
        heightToTextField.setText("");

        salaryFromTextField.setText("");
        salaryToTextField.setText("");

        applyFilters(event);
    }

    public void init(Client client, String clubName) {
        this.client = client;
        this.clubName = clubName;
        loadClubData();
        initClubInfo();
        loadPlayerCards(club.getPlayers());
        makeFilterTree();

        makeMenu();

    }

    private void makeMenu() {
        clubMenuButton.setText("Profile");
        usernameMenuItem.setText("Signed in as " + clubName);
    }

    private void makeFilterTree() {
        makeFilterTreeCountry();
        makeFilterTreePosition();
    }

    private void makeFilterTreePosition() {
        TreeItem<CheckBox> root;
        root = new TreeItem<>();
        root.setExpanded(true);

        this.club.getPositionList().forEach(e -> makeBranchFilterTree(e, root));

        filterTreePosition.setRoot(root);
        filterTreePosition.setShowRoot(false);
    }

    private void makeFilterTreeCountry() {
        TreeItem<CheckBox> root;
        root = new TreeItem<>();
        root.setExpanded(true);

        club.getCountryList().forEach(e -> makeBranchFilterTree(e, root));

        filterTreeCountry.setRoot(root);
        filterTreeCountry.setShowRoot(false);
    }

    private TreeItem<CheckBox> makeBranchFilterTree(String title, TreeItem<CheckBox> parent) {
        CheckBox checkBox = new CheckBox(title);
        checkBox.setSelected(true);
        TreeItem<CheckBox> item = new TreeItem<>(checkBox);
        parent.getChildren().add(item);
        return item;
    }

    private void initClubInfo() {
        String clubName = this.clubName.replace(' ', '_');
        Image image;
        try {
            image = new Image(getClass().getResourceAsStream("/pic/" + clubName + ".png"));
        } catch (Exception e) {
            image = new Image(getClass().getResourceAsStream("/pic/defaultclub.png"));
        }
        clubImage.setImage(image);
        String[] words = clubName.split("_");
        clubNameFirstLine.setText(words[0].toUpperCase());
        if (words.length > 1) {
            StringBuilder secondLine = new StringBuilder(words[1].toUpperCase());
            for (int i = 2; i < words.length; i++) {
                secondLine.append(" ").append(words[i].toUpperCase());
            }
            clubNameSecondLine.setText(secondLine.toString());
        } else {
            clubNameSecondLine.setText("");
        }
    }

    // for listing players under any condition
    private void loadPlayerCards(List<Player> playerList) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/fxml/playerListView.fxml"));
            Parent root = fxmlLoader.load();

            PlayerListViewController playerListViewController = fxmlLoader.getController();
            playerListViewController.setClubHomeWindowController(this);
            playerListViewController.loadPlayerCards(playerList);

            playerListVBox.getChildren().clear();
            playerListVBox.getChildren().add(root);

            this.playerListOnDisplay = new ArrayList<>(playerList);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void loadClubData() {
        this.club = client.loadClubFromServer(this.clubName);
    }

    @FXML
    void listMaxAgePlayers(ActionEvent event) {
        loadPlayerCards(club.getPlayersWithMaxAge());
    }

    @FXML
    void listMaxHeightPlayers(ActionEvent event) {
        loadPlayerCards(club.getPlayersWithMaxHeight());
    }

    @FXML
    void listMaxSalaryPlayers(ActionEvent event) {
        loadPlayerCards(club.getPlayersWithMaxSalary());
    }

    @FXML
    void showTotalYearlySalary(ActionEvent event) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle("Total Yearly Salary");
        a.setHeaderText(this.clubName);
        a.setContentText("Total yearly salary is " + this.club.getTotalYearlySalary());
        a.showAndWait();
    }

    public String getClubName() {
        return clubName;
    }

    public void setClubName(String clubName) {
        this.clubName = clubName;
    }

    @FXML
    void logoutClub(ActionEvent event) {
        client.logoutClub(this.clubName);
    }

    public void sellPlayer(String playerName) {
        boolean b = client.sellPlayer(playerName);
        if (b) {

            this.club.removePlayer(playerName);
            makeFilterTree();
            loadPlayerCards(this.club.getPlayers());
        }
    }

    public void buyPlayer(Player player) {
        boolean b = client.buyPlayer(player.getName(), this.clubName);
        if (b) {
            this.playerListOnDisplay.remove(player);
            player.setInTransferList(false);
            player.setClub(this.clubName);
            this.club.addPlayer(player);
            makeFilterTree();
            loadPlayerCards(this.playerListOnDisplay);
        }
    }
    public boolean playerExists(String playerName) {
        for (Player player : club.getPlayers()) {
            if (player.getName().equalsIgnoreCase(playerName)) {
                return true;
            }
        }
        return false;
    }
    public void addPlayerToDatabase(Player player) {
        boolean b = client.addPlayer(player);
        if (b) {
            this.playerListOnDisplay.add(player);
            player.setInTransferList(false);
            player.setClub(this.clubName);
            this.club.addPlayer(player);
            makeFilterTree();
            loadPlayerCards(this.playerListOnDisplay);
        }
    }
}
