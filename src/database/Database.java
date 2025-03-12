package database;
import entity.*;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Database implements PlayerService, ClubService {
    private List<Player> players;
    private Map<String, Club> clubs;
    private Map<String, Country> countries;

    public Database(List<Player> players) {
        this.players = players;
        this.clubs = new HashMap<>();
        this.countries = new HashMap<>();
        for (Player player : players) {
            addPlayerToClub(player);
            addPlayerToCountry(player);
        }
    }
    public Database() {
        this.players = new ArrayList<>();
        this.clubs = new HashMap<>();
        this.countries = new HashMap<>();
    }
    public List<Player> getPlayers() {
        return players;
    }
    public Map<String, Club> getClubs() {
        return clubs;
    }
    public Map<String, Country> getCountries() {
        return countries;
    }
    public void setPlayers(List<Player> players) {
        this.players = players;
    }
    public boolean addPlayer(Player player) {
        if(searchByName(player.getName()) != null) {
            return false;
        }
        players.add(player);
        addPlayerToClub(player);
        addPlayerToCountry(player);
        return true;
    }
   public boolean removePlayerFromClub(String name) {
        Player player = searchByName(name);
        if (player == null) {
            return false;
        }
        String clubName = player.getClub();
        String clubKey = clubName.toUpperCase();
        Club club = clubs.get(clubKey);
        if (club != null) {
            club.removePlayer(name);
            return true;
        }
        return false;
    }
    private void addPlayerToClub(Player player) {
        String clubName = player.getClub();
        String clubKey= clubName.toUpperCase();
        Club club = clubs.get(clubKey);
        if (club == null) {
            club = new Club(clubName);
            clubs.put(clubKey, club);
        }
        club.addPlayer(player);
    }

    private void addPlayerToCountry(Player player) {
        String countryName = player.getCountry();
        String countryKey = countryName.toUpperCase();
        Country country = countries.get(countryKey);
        if (country == null) {
            country = new Country(countryName);
            countries.put(countryKey, country);
        }
        country.addPlayer(player);
    }
    // Player methods
    @Override
    public Player searchByName(String name) {
        for (Player player : players) {
            if (player.getName().equalsIgnoreCase(name)) {
                return player;
            }
        }
        return null;
    }

    @Override
    public List<Player> searchByClubAndCountry(String country, String club) {
        List<Player> result = new ArrayList<>();
        Country countryObj = countries.get(country.toUpperCase());
        if (countryObj != null) {
            for (Player player : countryObj.getPlayers()) {
                if (club.equalsIgnoreCase("ANY") || player.getClub().equalsIgnoreCase(club)) {
                    result.add(player);
                }
            }
        }
        return result;
    }

    @Override
    public List<Player> searchByPosition(String position) {
        List<Player> result = new ArrayList<>();
        for (Player player : players) {
            if (player.getPosition().equalsIgnoreCase(position)) {
                result.add(player);
            }
        }
        return result;
    }
    public List<Player> searchByHeight(double low, double high) {
        List<Player> result = new ArrayList<>();
        for (Player player : players) {
            if (player.getHeight() >= low && player.getHeight() <= high) {
                result.add(player);
            }
        }
        return result;
    }
    public List<Player> searchByAge(int low, int high) {
        List<Player> result = new ArrayList<>();
        for (Player player : players) {
            if (player.getAge() >= low && player.getAge() <= high) {
                result.add(player);
            }
        }
        return result;
    }
    @Override
    public List<Player> searchBySalaryRange(long minSalary, long maxSalary) {
        List<Player> result = new ArrayList<>();
        for (Player player : players) {
            if (player.getWeeklySalary() >= minSalary && player.getWeeklySalary() <= maxSalary) {
                result.add(player);
            }
        }
        return result;
    }
    public List<Player> searchByCountry(String country) {
        List<Player> result = new ArrayList<>();
        Country countryObj = countries.get(country.toUpperCase());
        if (countryObj != null) {
            result.addAll(countryObj.getPlayers());
        }
        return result;
    }
    @Override
    public Map<String, Integer> countryWisePlayerCount() {
        Map<String, Integer> countryCount = new HashMap<>();
        for (Country country : countries.values()) {
            countryCount.put(country.getName(), country.getPlayerCount());
        }
        return countryCount;
    }


    // Club methods
    @Override
    public List<Player> searchByMaxSalary(String clubName) {
        Club club = clubs.get(clubName.toUpperCase());
        return club != null ? club.getPlayersWithMaxSalary() : new ArrayList<>();
    }

    @Override
    public List<Player> searchByMaxAge(String clubName) {
        Club club = clubs.get(clubName.toUpperCase());
        return club != null ? club.getPlayersWithMaxAge() : new ArrayList<>();
    }

    @Override
    public List<Player> searchByMaxHeight(String clubName) {
        Club club = clubs.get(clubName.toUpperCase());
        return club != null ? club.getPlayersWithMaxHeight() : new ArrayList<>();
    }

    @Override
    public long calculateTotalYearlySalary(String clubName) {
        Club club = clubs.get(clubName.toUpperCase());
        return club != null ? club.getTotalYearlySalary() : -1;
    }
}
