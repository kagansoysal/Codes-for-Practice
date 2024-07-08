import java.util.ArrayList;
import java.util.stream.IntStream;

public class Utilities {
    public static void cleanConsole(int spaceRows){
        IntStream.range(0, spaceRows).forEach(i -> System.out.println());
    }

    public static ArrayList<ArrayList<Team>> findCombinations(ArrayList<Team> teams, int combinationSize) {
        ArrayList<ArrayList<Team>> result = new ArrayList<>();
        findCombinationsRecursive(result, new ArrayList<>(), teams, combinationSize, 0);
        return result;
    }

    public static void findCombinationsRecursive(ArrayList<ArrayList<Team>> result, ArrayList<Team> currentCombination, ArrayList<Team> teams,
                                                 int combinationSize, int startIndex) {
        if (combinationSize == 0) {
            result.add(new ArrayList<>(currentCombination));
            return;
        }

        for (int i = startIndex; i < teams.size(); i++) {
            currentCombination.add(teams.get(i));
            findCombinationsRecursive(result, currentCombination, teams, combinationSize - 1, i + 1);
            currentCombination.remove(currentCombination.size() - 1);
        }
    }
}
