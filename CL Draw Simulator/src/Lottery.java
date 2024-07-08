import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Lottery {
    ArrayList<Group> groups;
    ArrayList<Character> redGropus = new ArrayList<>(Arrays.asList('A', 'B', 'C', 'D'));
    ArrayList<Character> blueGroups = new ArrayList<>(Arrays.asList('E', 'F', 'G', 'H'));
    ArrayList<Team> teams;
    ArrayList<List<Team>> pots;
    HashMap<Integer, Team> teamBalls;
    HashMap<Integer, Character> groupBalls;
    Scanner scanner = new Scanner(System.in);

    Lottery(ArrayList<Group> groups, ArrayList<Team> teams, ArrayList<List<Team>> pots) {
        this.groups = groups;
        this.teams = teams;
        this.pots = pots;

        writePots();
        draw();
    }

    void writePots() {
        System.out.println();
        for (int potNum = 0; potNum < pots.size(); potNum++) {
            System.out.printf("%d.Pot: %s%n", potNum + 1, pots.get(potNum).stream().map(team -> team.name).collect(Collectors.joining(" | ")));
        }
        System.out.println("\n");
    }

    void draw(){
        for (int potNumber = 1; potNumber <= 4; potNumber++) {
            System.out.println(potNumber + ".POT DRAW");
            teamBalls = identifyBalls(pots.get(potNumber - 1));
            drawPot(potNumber);
            Utilities.cleanConsole(15);
        }
    }

    <T> HashMap<Integer, T> identifyBalls(List<T> items) {
        HashMap<Integer, T> balls = new HashMap<>();
        Collections.shuffle(items);

        for (int ballNum = 1; ballNum <= items.size(); ballNum++)
            balls.put(ballNum, items.get(ballNum - 1));

        return balls;
    }

    void drawPot(int potNumber){
        Team selectedTeam = selectTeam();
        Group selectedGroup = potNumber == 1 ? groups.get(8 - teamBalls.size()) : selectGroup(selectedTeam, potNumber);

        placeTeam(selectedTeam, selectedGroup);
        drawNewTeam(potNumber);
    }

    Team selectTeam() {
        System.out.print("\nTeam Balls-> ");
        teamBalls.keySet().forEach(ballNum -> System.out.print(ballNum + " "));
        System.out.print("\nDraw a Team: ");

        int selectedBall = scanner.nextInt();
        Team selectedTeam = teamBalls.get(selectedBall);

        System.out.println("\n");
        return selectedTeam;
    }

    Group selectGroup(Team selectedTeam, int potNumber) {
        writeGroups();
        Utilities.cleanConsole(7);
        System.out.println("{IN POTS}");

        //Create the pot
        ArrayList<Team> inPot = new ArrayList<>(teamBalls.values());
        Collections.shuffle(inPot);

        //Write teams in the pots
        inPot.forEach(team -> System.out.println(team.name));
        Utilities.cleanConsole(10 - inPot.size());
        System.out.println("Drawn Team is " + selectedTeam.name + "\n");

        //Determine the groups that the teams can go
        ArrayList<Team> potTeams = new ArrayList<>(teamBalls.values());
        selectedTeam.groupsToGo = validGroups(selectedTeam, potNumber);
        selectedTeam.groupsToGo.removeAll(removedGroups(potTeams, selectedTeam, potNumber));

        System.out.print("Groups To Go: ");
        selectedTeam.groupsToGo.forEach(groupName -> System.out.print(groupName + " "));

        groupBalls = identifyBalls(selectedTeam.groupsToGo);

        System.out.print("\nDraw a Group: ");
        groupBalls.keySet().forEach(ballNum -> System.out.print(ballNum + " "));

        //Select a ball to select group
        int groupNum = scanner.nextInt();
        Group selectedGroup = groups.get((int) groupBalls.get(groupNum) - (int) 'A');

        Utilities.cleanConsole(4);
        return selectedGroup;
    }

    void placeTeam(Team team, Group group) {
        group.teams.add(team);
        teamBalls.values().remove(team);
        writeGroups();
        System.out.println("\n\n" + team.name.toUpperCase().replace("İ", "I") + " GOES TO GROUP " + group.name);
    }

    void writeGroups(){
        groups.forEach(group -> System.out.printf(" %-19s", group.name));
        System.out.println();

        groups.forEach(group -> System.out.printf("%-20s", "---"));
        System.out.println();

        for (int i = 0; i < 4; i++) {
            final int index = i;
            groups.forEach(group -> {
                String teamName = (index < group.teams.size()) ? group.teams.get(index).name : "";
                System.out.printf("%-20s", "-" + teamName);
            });
            System.out.println();
        }
    }

    void drawNewTeam(int potNumber) {
        if (!teamBalls.isEmpty()) {
            Utilities.cleanConsole(5);
            System.out.println("{IN POTS}");

            ArrayList<Team> inPot = new ArrayList<>(teamBalls.values());
            Collections.shuffle(inPot);

            inPot.forEach(team -> System.out.println(team.name));

            Utilities.cleanConsole(10 - inPot.size());
            drawPot(potNumber);
        }
    }

    ArrayList<Character> validGroups(Team team, int potNumber) {
        ArrayList<Character> groupNames = new ArrayList<>(Arrays.asList('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H'));

        groups.forEach(group -> {
            if (team.couple != null && group.teams.contains(team.couple)) {
                groupNames.removeAll(redGropus.contains(group.name) ? redGropus : blueGroups);
            } else if (group.teams.size() == potNumber || group.teams.stream().anyMatch(team1 -> team1.country.equals(team.country))) {
                groupNames.remove(group.name);
            }
        });
        return groupNames;
    }

    ArrayList<Character> removedGroups(ArrayList<Team> potTeams, Team selectedTeam, int potNumber){
        ArrayList<Character> removedGroups = new ArrayList<>();
        potTeams.remove(selectedTeam);

        List<ArrayList<Team>> teamCombinations = IntStream.rangeClosed(1, potTeams.size())
                .mapToObj(i -> Utilities.findCombinations(potTeams, i))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        teamCombinations.forEach(teams -> {

            List<Character> allGroups = teams.stream()
                    .map(team -> validGroups(team, potNumber))
                    .flatMap(Collection::stream).distinct().collect(Collectors.toList());

            if (!(allGroups.size() > teams.size())) {
                removedGroups.addAll(allGroups);
            }
        });
        return removedGroups;
    }
}