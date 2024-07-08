import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataRegulation {
    public ArrayList<Team> createTeams(String HTMLOfPots){
        ArrayList<Team> teams = new ArrayList<>();

        String countryPattern = "alt=\"([^\"]+)\"";
        String teamPattern = ">([^<]+)<\\/a><\\/td><td align=\"right\">";
        String coefficientPattern = "align=\"right\">([\\d,]+)<\\/td>";

        Pattern countryRegex = Pattern.compile(countryPattern);
        Pattern teamRegex = Pattern.compile(teamPattern);
        Pattern coefficientRegex = Pattern.compile(coefficientPattern);

        Matcher countryMatcher = countryRegex.matcher(HTMLOfPots);
        Matcher teamMatcher = teamRegex.matcher(HTMLOfPots);
        Matcher coefficientMatcher = coefficientRegex.matcher(HTMLOfPots);

        while (countryMatcher.find() && teamMatcher.find() && coefficientMatcher.find()) {
            String country = countryMatcher.group(1);
            String name = teamMatcher.group(1);
            int coefficient = Integer.parseInt(coefficientMatcher.group(1).replace(",", "")) ;

            teams.add(new Team(name, country, coefficient));
        }
        matchCouples(teams);
        return teams;
    }

    public void matchCouples(ArrayList<Team> teams){
        for (Team team : teams) {
            ArrayList<Team> sameCountry = new ArrayList<>();

            for (Team otherTeam : teams) {
                if (otherTeam.country.equals(team.country)) {
                    sameCountry.add(otherTeam);
                }
            }
            sameCountry.sort((team1, team2) -> Integer.compare(team2.coefficient, team1.coefficient));

            for (int i = 0; i < sameCountry.size(); i++) {
                if ((sameCountry.get(i).couple == null) && (i != sameCountry.size()-1)) {
                    sameCountry.get(i).couple = sameCountry.get(i+1);
                    sameCountry.get(i+1).couple = sameCountry.get(i);
                }
            }
        }
    }

    public ArrayList<List<Team>> createPots(ArrayList<Team> teams) {
        ArrayList<List<Team>> pots = new ArrayList<>();
        pots.add(teams.subList(0,8));
        pots.add(teams.subList(8,16));
        pots.add(teams.subList(16,24));
        pots.add(teams.subList(24,32));
        return pots;
    }

    public ArrayList<Group> createGroups(){
        ArrayList<Group> groups = new ArrayList<>();
        for (char ch = 'A'; ch <= 'H'; ch++) groups.add(new Group(ch));
        return groups;
    }
}
