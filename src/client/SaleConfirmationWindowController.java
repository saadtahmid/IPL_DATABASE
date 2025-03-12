package client;

import entity.Player;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class SaleConfirmationWindowController {

    @FXML
    private Label playerNameLabel;

    @FXML
    private Button confirmSaleButton;

    @FXML
    private Button undoSaleButton;

    private Player player;
    private boolean isSaleConfirm;
    private Stage stage;


    @FXML
    void confirmSale(ActionEvent event) {
        try {
       
            isSaleConfirm = true;
            this.stage.close();
        } catch (Exception e) {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setTitle("Error");
            a.setHeaderText("Sale Request Failed!");
            a.showAndWait();
        }
    }

    @FXML
    void undoSale(ActionEvent event) {
        isSaleConfirm = false;
        this.stage.close();
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
        playerNameLabel.setText("Are You Sure you want to sell " + player.getName() +"?");
    }

    public boolean isSaleConfirm() {
        return isSaleConfirm;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
