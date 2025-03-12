package entity;
import java.io.Serializable;
public class Player implements Serializable{
    
    private String name;
    private String country;
    private int ageInYears;
    private double heightInMeters;
    private String club;
    private String position;
    private Integer number;
    private  long weeklySalary;
    private boolean isInTransferList=false;

    public Player() {
        this.name = "";
        this.country = "";
        this.ageInYears = 0;
        this.heightInMeters = 0;
        this.club = "";
        this.position = "";
        this.number = null;
        this.weeklySalary = 0;
        this.isInTransferList = false;
    }
    public Player(String name, String country, int ageInYears, double heightInMeters,
            String club, String position, Integer number, long weeklySalary) {
        this.name = capitalize(name);
        this.country = capitalize(country);
        this.ageInYears = ageInYears;
        this.heightInMeters = heightInMeters;
        this.club = capitalize(club);
        this.position = capitalize(position);
        this.number = number;
        this.weeklySalary = weeklySalary;
    }
    
    
    public String getName() {
        return name;
    }

    public String getCountry() {
        return country;
    }

    public int getAge() {
        return ageInYears;
    }

    public double getHeight() {
        return heightInMeters;
    }

    public String getClub() {
        return club;
    }

    public String getPosition() {
        return position;
    }

    public Integer getNumber() {
        return number;
    }

    public long getWeeklySalary() {
        return weeklySalary;
    }
    public boolean isInTransferList() {
        return isInTransferList;
    }
    
    public void setInTransferList(boolean isInTransferList) {
        this.isInTransferList = isInTransferList;
    }
    public void setName(String name) {
        this.name = name;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setAgeInYears(int ageInYears) {
        this.ageInYears = ageInYears;
    }

    public void setHeightInMeters(double heightInMeters) {
        this.heightInMeters = heightInMeters;
    }

    public void setClub(String club) {
        this.club = club;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public void setWeeklySalary(long weeklySalary) {
        this.weeklySalary = weeklySalary;
    }

    public String tokenize() {
        return name+ "," + country+ "," + ageInYears + "," + heightInMeters + "," + club + "," + position + ","
                + (number == null ? "" : number) + "," + weeklySalary;
    }

    
    private String capitalize(String str) {
        String []parts= str.split(" ");
        StringBuilder sb= new StringBuilder();
        for (String part: parts) {
            sb.append(part.substring(0, 1).toUpperCase()).append(part.substring(1).toLowerCase()).append(" ");

        }
        return sb.toString().trim();
    }
    public void displayPlayerInfo() {
        System.out.println("\nName: " + name);
        System.out.println("Country: " + country);
        System.out.println("Age: " + ageInYears + " years");
        System.out.println("Height: " + heightInMeters + " meters");
        System.out.println("Club: " + club);
        System.out.println("Position: " + position);
        if (number != null)
            System.out.println("Jersey Number: " + number);
        else
            System.out.println("Jersey Number: N/A");
        System.out.println("Weekly Salary: $" +   weeklySalary);
        System.out.println();
    }

    @Override
    public String toString() {
        return "name: " + name + '\n' + "country: " + country + '\n' + "age: " + ageInYears + '\n' + "height: "
                + heightInMeters + '\n' + "club: " + club + '\n' + "position: " + position + '\n'
                + (number == null ? "N/A" : "number: " + number + '\n') + "salary: " + weeklySalary
                + '\n';
    }
}
