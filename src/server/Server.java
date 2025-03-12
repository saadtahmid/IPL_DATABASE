package server;

import database.Database;
import entity.Player;
import util.NetworkUtil;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Server {
    // Volatile keyword ensures visibility of changes to variables across threads
    volatile Database db;
    private PlayerFileHandler fileOperations;
    volatile private List<Player> transferPlayerList;
    volatile private HashMap<String, ClientInfo> clientMap;

    public static void main(String[] args) {
        int port = 45045;
        new Server(port);
    }

    public Server(int port) {
        clientMap = new HashMap<>();
        transferPlayerList = new ArrayList<>();
        try {
            // Load the database from file
            loadDatabase();
            ServerSocket serverSocket = new ServerSocket(port);
            // Add shutdown hook to write players to file when the server shuts down
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                try {
                    fileOperations.writePlayers(db.getPlayers());
                    System.out.println("Players written to file successfully.");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }));
            // Continuously accept client connections
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("client connected");
                serve(clientSocket);
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("this is exception while connecting");
        }
    }

    synchronized public List<Player> getTransferPlayerList() {
        return transferPlayerList;
    }

    // Load the database from file
    private void loadDatabase() throws IOException {
        db = new Database();
        fileOperations = new PlayerFileHandler();
        fileOperations.readPlayers(db);
    }

    // Serve the client by creating a new thread for each connection
    private void serve(Socket socket) throws IOException {
        NetworkUtil networkUtil = new NetworkUtil(socket);
        System.out.println("networkutil for server created");
        new ThreadServer(networkUtil, this);
    }

    // Synchronized method to handle selling a player
    synchronized public boolean sellPlayer(String playerName, String newClubName) {
        boolean b = false;
        try {
            // Search for the player by name
            Player player = db.searchByName(playerName);
            // Check if the player is in the transfer list
            if (player.isInTransferList()) {
                // Remove the player from the transfer list
                transferPlayerList.remove(player);
                // Update the player's transfer status and club
                player.setInTransferList(false);
                player.setClub(newClubName);
                b = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return b;
    }

    // Synchronized method to add a player to the transfer window
    synchronized public boolean addToTransferWindow(String playerName) {
        boolean b = false;
        try {
            // Search for the player by name
            Player player = db.searchByName(playerName);
            // Update the player's transfer status and add to the transfer list
            player.setInTransferList(true);
            transferPlayerList.add(player);

            b = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return b;
    }

    // Synchronized method to handle club login
    // Returns true if login is successful, false otherwise
    synchronized public boolean loginClub(String username, NetworkUtil networkUtil) {
        System.out.println("login req to server: username " + username);
        ClientInfo clientInfo = new ClientInfo();
        clientInfo.setClubName(username);
        clientInfo.setNetworkUtil(networkUtil);
        // Add the client to the client map if not already present
        if (!clientMap.containsKey(username))
            clientMap.put(username, clientInfo);
        // Check if the client is not already logged in
        if (clientMap.containsKey(username)
                && !clientMap.get(username).isLoggedIn()) {
            clientMap.get(username).setLoggedIn(true);

            return true;
        }

        return false;
    }

    // Synchronized method to handle club logout
    // Returns true if logout is successful, false otherwise
    synchronized public boolean logoutClub(String username) {
        // Check if the client is logged in
        if (clientMap.containsKey(username) && clientMap.get(username).isLoggedIn()) {
            clientMap.get(username).setLoggedIn(false);
            return true;
        }
        return false;
    }

    // Synchronized method to send the list of clubs
    synchronized public List<String> sendClubList() {
        List<String> clubList = new ArrayList<>();
        // Iterate through the clubs in the database and add their names to the list
        for (var club : db.getClubs().values()) {
            clubList.add(club.getName());
        }

        return clubList;
    }

    // Synchronized method to add a player to a club
    synchronized public boolean addPlayer(Player player) {
        boolean b = false;
        try {
            db.addPlayer(player);
            b = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return b;
    }
}
