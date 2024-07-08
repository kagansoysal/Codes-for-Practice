import java.util.ArrayList;

public class Team {
    String name;
    String country;
    int coefficient;
    Team couple;
    ArrayList<Character> groupsToGo;

    Team(String name, String country, int coefficient){
        this.name = name;
        this.country = country;
        this.coefficient = coefficient;
    }
}
