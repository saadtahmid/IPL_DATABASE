package client;

import entity.Player;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddPlayerWindowController {

    @FXML
    private TextField nameTextField;

    @FXML
    private TextField positionTextField;

    @FXML
    private TextField countryTextField;

    @FXML
    private TextField ageTextField;

    @FXML
    private TextField heightTextField;
    @FXML
    private TextField numberTextField;

    @FXML
    private TextField salaryTextField;

    private Stage stage;
    private ClubHomeWindowController clubHomeWindowController;

    @FXML
    private void addPlayer() {
        String name = nameTextField.getText();
        String position = positionTextField.getText();
        String country = countryTextField.getText();
        int age = Integer.parseInt(ageTextField.getText());
        double height = Double.parseDouble(heightTextField.getText());
        Integer number ;
        try{
            number = Integer.parseInt(numberTextField.getText());
        }catch (NumberFormatException e){
            number = null;
        }
        long salary = Long.parseLong(salaryTextField.getText());

        // Check if a player with the same name already exists in the club
        if (clubHomeWindowController.playerExists(name)) {
            // Show an error alert if the player already exists
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Player Exists");
            alert.setContentText("A player with the name " + name + " already exists in the club.");
            alert.showAndWait();
        } else {
            // Use the correct constructor for the Player class
            Player player = new Player(name, country, age, height, clubHomeWindowController.getClubName(), position, number, salary);
            clubHomeWindowController.addPlayerToDatabase(player);
            stage.close();
        }
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setClubHomeWindowController(ClubHomeWindowController clubHomeWindowController) {
        this.clubHomeWindowController = clubHomeWindowController;
    }
}