package client;

import entity.Player;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class PlayerCardController {


    @FXML
    private Label playerNameLabel;

    @FXML
    private Label playerPositionLabel;

    @FXML
    private Button playerDetailsButton;
    @FXML
    private ImageView image;
    @FXML
    private Button playerSellButton;

   

    private Player player;
    private ClubHomeWindowController clubHomeWindowController;

    @FXML
    void sellPlayer(ActionEvent event) {
        if (player.isInTransferList()) {
            // buy
            clubHomeWindowController.buyPlayer(player);
        } else {
            // sell
            boolean b = showPlayerSaleConfirmationWindow();
            if (b) clubHomeWindowController.sellPlayer(player.getName());
        }
    }

    private boolean showPlayerSaleConfirmationWindow() {
        boolean b = false;
        try {
            Stage window = new Stage();
            window.initModality(Modality.APPLICATION_MODAL);
            window.setTitle("Sale Confirmation");

            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/fxml/saleConfirmationWindow.fxml"));

            Parent root = fxmlLoader.load();

            SaleConfirmationWindowController controller = fxmlLoader.getController();
            controller.setPlayer(this.player);
            controller.setStage(window);

            Scene scene = new Scene(root);
            window.setScene(scene);
            window.showAndWait();

            b = controller.isSaleConfirm();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return b;
    }

    @FXML
    void showPlayerDetails(ActionEvent event) {

        try {
            Stage window = new Stage();
            window.initModality(Modality.APPLICATION_MODAL);
            window.setTitle(player.getName());

            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/fxml/playerDetailsWindow.fxml"));
            Parent root = fxmlLoader.load();

            PlayerDetailsWindowController playerDetails = fxmlLoader.getController();
            playerDetails.setData(player);

            Scene scene = new Scene(root);
            window.setScene(scene);
            window.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public void setData(Player player) {
        this.player = player;
        playerNameLabel.setText(player.getName());
        playerPositionLabel.setText(player.getPosition());
        image.setImage(new Image("pic/" + player.getPosition() + ".png"));

        if (player.isInTransferList()) {
            playerSellButton.setText("Buy");
          
        } else {
            playerSellButton.setText("Sell");
          
        }
    }

    public ClubHomeWindowController getClubHomeWindowController() {
        return clubHomeWindowController;
    }

    public void setClubHomeWindowController(ClubHomeWindowController clubHomeWindowController) {
        this.clubHomeWindowController = clubHomeWindowController;
    }
}
