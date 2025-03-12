package client;

import entity.Player;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class PlayerDetailsWindowController {

    

    @FXML
    private Label playerNameLabel;

    @FXML
    private Label playerPositionLabel;

    @FXML
    private Label playerClubLabel;

    @FXML
    private Label playerCountryLabel;
    @FXML
    private ImageView playerImage;
    @FXML
    private Label playerAgeLabel;

    @FXML
    private Label playerSalaryLabel;

    @FXML
    private Label playerHeightLabel;

    @FXML
    private Label playerNumberLabel;

    public void setData(Player player) {
        playerNameLabel.setText(player.getName());
        Image image;
        try {
            image = new Image(getClass().getResourceAsStream("/pic/" + player.getName() + ".png"));
        } catch (Exception e) {
            image = new Image(getClass().getResourceAsStream("/pic/default.png"));
        }
        playerImage.setImage(image);
        playerPositionLabel.setText(player.getPosition());
        playerClubLabel.setText(player.getClub());
        playerCountryLabel.setText(player.getCountry());
        playerCountryLabel.setStyle("-fx-font-family: Cambria");
        playerAgeLabel.setText("Age: " + player.getAge() + " years");
        playerHeightLabel.setText("Height: " + player.getHeight() + " meters");
        playerNumberLabel.setText("Number: " + player.getNumber());
        playerSalaryLabel.setText("Weekly Salary: " + player.getWeeklySalary());
    }
   

}
