import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("~~~WELCOME TO CHAMPIONS LEAGUE DRAW~~~");

        String season = chooseSeason();

        WebScraper webScraper = new WebScraper();
        String HTML = webScraper.pullDatas(season);

        DataRegulation dataRegulation = new DataRegulation();

        ArrayList<Group> groups = dataRegulation.createGroups();
        ArrayList<Team> teams = dataRegulation.createTeams(HTML);
        ArrayList<List<Team>> pots = dataRegulation.createPots(teams);

        new Lottery(groups, teams, pots);

        System.out.println("~~~~GOOD LUCK TO ALL TEAMS~~~~");
    }

    private static String chooseSeason() {
        System.out.println("\nWrite the season you want in \"yy-yy\" format. (The program does not support the 2013-14 season and earlier)");
        System.out.print("Which Season: ");

        Scanner scanner = new Scanner(System.in);
        return scanner.next();
    }
}