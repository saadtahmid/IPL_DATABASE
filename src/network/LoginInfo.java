package network;

import java.io.Serializable;

public class LoginInfo implements Serializable {
    private MessageHeader messageHeader;
    private String username;
    
    public LoginInfo(MessageHeader messageHeader, String username) {
        this.messageHeader = messageHeader;
        this.username = username;
        
    }

    public MessageHeader getMessageHeader() {
        return messageHeader;
    }

    public void setMessageHeader(MessageHeader messageHeader) {
        this.messageHeader = messageHeader;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    
}
