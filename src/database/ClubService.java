package database;

import entity.Player;
import java.util.List;

public interface ClubService {
    List<Player> searchByMaxSalary(String club);
    List<Player> searchByMaxAge(String club);
    List<Player> searchByMaxHeight(String club);
    long calculateTotalYearlySalary(String club);
}
