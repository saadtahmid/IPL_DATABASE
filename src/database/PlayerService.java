package database;

import entity.Player;
import java.util.List;
import java.util.Map;

public interface PlayerService {
    Player searchByName(String name);
    List<Player> searchByClubAndCountry(String country, String club);
    List<Player> searchByPosition(String position);
    List<Player> searchBySalaryRange(long minSalary, long maxSalary);
    Map<String, Integer> countryWisePlayerCount();
}
