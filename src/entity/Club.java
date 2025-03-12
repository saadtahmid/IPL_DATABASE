package entity;

import java.util.List;
import java.io.Serializable;


import java.util.ArrayList;

public class Club implements Serializable {
    private String name;
    private List<Player> players;

    public Club(String name) {
        this.name = name;
        this.players = new ArrayList<>();
    }
    public Club(){
        this.name = "";
        this.players = new ArrayList<>();
    }
    public void  setName(String name) {
        this.name = name;
    }
    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public String getName() {
        return name;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void addPlayer(Player player) {
        players.add(player);
    }
    public void removePlayer(String name) {
        Player player = null;
        for (Player p : players) {
            if (p.getName().equals(name)) {
                player = p;
                break;
            }
        }
        if (player != null) {
            players.remove(player);
        }
    }
    public List<Player> getPlayersWithMaxSalary() {
        long maxSalary = -1;
        List<Player> maxSalaryPlayers = new ArrayList<>();
        for (Player player : players) {
            if (player.getWeeklySalary() > maxSalary) {
                maxSalary = player.getWeeklySalary();
                maxSalaryPlayers.clear();
                maxSalaryPlayers.add(player);
            } else if (player.getWeeklySalary() == maxSalary) {
                maxSalaryPlayers.add(player);
            }
        }
        return maxSalaryPlayers;
    }
    public List<String>getCountryList(){
        List<String> countryList = new ArrayList<>();
        for(Player player: players){
            if(!countryList.contains(player.getCountry())){
                countryList.add(player.getCountry());
            }
        }
        return countryList;
    }
    public List<String>getPositionList(){
        List<String> positionList = new ArrayList<>();
        for(Player player: players){
            if(!positionList.contains(player.getPosition())){
                positionList.add(player.getPosition());
            }
        }
        return positionList;
    }
    public List<Player> getPlayersWithMaxAge() {
        int maxAge = -1;
        List<Player> maxAgePlayers = new ArrayList<>();
        for (Player player : players) {
            if (player.getAge() > maxAge) {
                maxAge = player.getAge();
                maxAgePlayers.clear();
                maxAgePlayers.add(player);
            } else if (player.getAge() == maxAge) {
                maxAgePlayers.add(player);
            }
        }
        return maxAgePlayers;
    }

    public List<Player> getPlayersWithMaxHeight() {
        double maxHeight = -1;
        List<Player> maxHeightPlayers = new ArrayList<>();
        for (Player player : players) {
            if (player.getHeight() > maxHeight) {
                maxHeight = player.getHeight();
                maxHeightPlayers.clear();
                maxHeightPlayers.add(player);
            } else if (player.getHeight() == maxHeight) {
                maxHeightPlayers.add(player);
            }
        }
        return maxHeightPlayers;
    }

    public long getTotalYearlySalary() {
        long totalSalary = 0;
        for (Player player : players) {
            totalSalary += player.getWeeklySalary() * 52; 
        }
        return totalSalary;
    }
}