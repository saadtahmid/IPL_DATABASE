package client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import java.util.List;

public class ClubLoginWindowController {

    @FXML
    private ChoiceBox<String> usernameChoiceBox;

   

    @FXML
    private Button loginButton;

    @FXML
    private Button resetButton;


    private Client client;

    @FXML
    void login(ActionEvent event) {
        String username = usernameChoiceBox.getValue();
        if (username == null ) {
            showAlert("Login");
        } else {
            client.loginClub(username);
            reset(event);
        }
    }

    private void showAlert(String header) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setTitle("Error");
        a.setHeaderText(header + " not successful");
        a.setContentText("Team name cannot be empty");
        a.showAndWait();
    }


    @FXML
    void reset(ActionEvent event) {
        usernameChoiceBox.setValue(null);
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public void init() {
        initializeUsernameChoiceBox();
    }

    private void initializeUsernameChoiceBox() {
        List<?> clubList = client.loadClubList();
        clubList.forEach(e -> {
            if (e instanceof String) {
                usernameChoiceBox.getItems().add((String) e);
            }
        });
    }
}
