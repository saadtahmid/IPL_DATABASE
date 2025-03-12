package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.List;
import database.Database;
import entity.Player;
public class PlayerFileHandler {
    private String INPUT_FILE_NAME;
    private String OUTPUT_FILE_NAME;
    
    public PlayerFileHandler() {
        this.INPUT_FILE_NAME = "players.txt";
        this.OUTPUT_FILE_NAME = "players.txt";
    }
    public PlayerFileHandler(String inputFileName, String outputFileName) {
        this.INPUT_FILE_NAME = inputFileName;
        this.OUTPUT_FILE_NAME = outputFileName;
    }

    public void setInputFileName(String inputFileName) {
        this.INPUT_FILE_NAME = inputFileName;
    }

    public void setOutputFileName(String outputFileName) {
        this.OUTPUT_FILE_NAME = outputFileName;
    }
    public String getInputFileName() {
        return INPUT_FILE_NAME;
    }
    public String getOutputFileName() {
        return OUTPUT_FILE_NAME;
    }
    public void readPlayers(Database db) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE_NAME));
            
            while (true) {
                String line = br.readLine();
                if (line == null)
                    break;
                String[] parts = line.split(",");

                String name = parts[0];
                String country = parts[1];
                int age = Integer.parseInt(parts[2]);
                double height = Double.parseDouble(parts[3]);
                String club = parts[4];
                String position = parts[5];
                Integer number = null;
                if (!parts[6].equals("")) {
                    number = Integer.parseInt(parts[6]);
                }
                long salary = Long.parseLong(parts[7]);
                Player player = new Player(name, country, age, height, club, position, number, salary);
                db.addPlayer(player);
            }
            br.close();
            
        } catch (Exception e) {
            System.out.println("Error reading from file: " + e);
            
        }
    }

    public void writePlayers(List<Player> players) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(OUTPUT_FILE_NAME));
            for (Player player : players) {

                bw.write(player.tokenize());
                bw.write(System.lineSeparator());
            }
            bw.close();
        } catch (Exception e) {
            System.out.println("Error writing to file: " + e);
        }
    }
}
