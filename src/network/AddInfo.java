package network;
import entity.Player;
import java.io.Serializable;

public class AddInfo implements Serializable {
    private MessageHeader messageHeader;
    private Player player;

    public AddInfo(MessageHeader messageHeader, Player player) {
        this.messageHeader = messageHeader;
        this.player = player;
       
    }

    public MessageHeader getMessageHeader() {
        return messageHeader;
    }

    public void setMessageHeader(MessageHeader messageHeader) {
        this.messageHeader = messageHeader;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

}
