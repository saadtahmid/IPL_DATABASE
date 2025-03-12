package server;

import entity.Club;

import network.*;
import util.NetworkUtil;

import java.io.IOException;

public class ThreadServer implements Runnable {
    private NetworkUtil networkUtil;
    private Thread thread;
    private Server server;

    public ThreadServer(NetworkUtil networkUtil, Server server) {
        this.networkUtil = networkUtil;
        this.server = server;
        thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        try {
            while (true) {
                Object obj = networkUtil.read();
                if (obj instanceof Message) {
                    Message msg = (Message) obj;
                    if (msg.getMessageHeader() == MessageHeader.CLUB_INFO) {
                        String clubName = msg.getMessage();
                        var clubs = server.db.getClubs();
                        Club club = null;
                        // Find the club by name
                        for (Club c : clubs.values()) {
                            if (c.getName().equalsIgnoreCase(clubName)) {
                                club = c;
                                break;
                            }
                        }
                        // Send the club information back to the client
                        networkUtil.write(club);
                    } else if (msg.getMessageHeader() == MessageHeader.TRANSFER_WINDOW) {
                        // Send the list of players in the transfer window to the client
                        networkUtil.write(server.getTransferPlayerList());
                    } else if (msg.getMessageHeader() == MessageHeader.LOGOUT) {
                         // Handle club logout
                        networkUtil.write(server.logoutClub(msg.getMessage()));
                    } else if (msg.getMessageHeader() == MessageHeader.CLUB_LIST) {
                         // Send the list of clubs to the client
                        networkUtil.write(server.sendClubList());
                    }
                } else if (obj instanceof LoginInfo) {
                    LoginInfo loginInfo = (LoginInfo) obj;
                    if (loginInfo.getMessageHeader() == MessageHeader.LOGIN) {
                        // Handle club login
                        networkUtil.write(server.loginClub(loginInfo.getUsername(), networkUtil));
                    }
                } else if (obj instanceof BuyInfo) {
                    BuyInfo buyInfo = (BuyInfo) obj;
                    if (buyInfo.getMessageHeader() == MessageHeader.BUY) {
                        // Handle buying a player
                        networkUtil.write(server.sellPlayer(buyInfo.getPlayerName(), buyInfo.getClubName()));
                    }
                } else if (obj instanceof SaleInfo) {
                    SaleInfo saleInfo = (SaleInfo) obj;
                    if (saleInfo.getMessageHeader() == MessageHeader.SELL) {
                        // Handle adding a player to the transfer window
                        networkUtil.write(server.addToTransferWindow(saleInfo.getPlayerName()));
                    }
                }else if (obj instanceof AddInfo) {
                    AddInfo addInfo = (AddInfo) obj;
                    if (addInfo.getMessageHeader() == MessageHeader.ADD) {
                        // Handle adding a player to a club
                        networkUtil.write(server.addPlayer(addInfo.getPlayer()));
                    }
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println(e);
        } finally {
            try {
                networkUtil.closeConnection();
                
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
