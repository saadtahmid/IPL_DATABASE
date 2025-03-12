package entity;

import java.util.List;


import java.util.ArrayList;

public class Country {
    private String name;
    private List<Player> players;

    public Country(String name) {
        this.name = name;
        this.players = new ArrayList<>();
    }
    public Country(){
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
    public void removePlayer(Player player) {
        players.remove(player);
    }
    public int getPlayerCount() {
        return players.size();
    }
}