package bootybot.games;

import bootybot.BootyBot;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class GameList {

    private BootyBot bot;

    private SheetsAPI sheetsAPI;

    private Map<String, Integer> gameMaxPlayers = new HashMap<>();
    private Map<String, List<String>> userGames = new HashMap<>();

    public GameList(BootyBot bot) {
        this.bot = bot;
        try {
            this.sheetsAPI = new SheetsAPI();
            readSheet();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Pull new data from google sheet and updates the game data
     */
    public void updateGameData() {
        sheetsAPI.updateData();
        readSheet();
    }


    /**
     * Interpret sheet data and stores game info to memory
     */
    public void readSheet() {
        gameMaxPlayers.clear();
        userGames.clear();

        List<List<Object>> rawData = sheetsAPI.getRawData();

        if (rawData != null) {

            //convert user row to only IDs
            String[] users = rawData.get(0).toArray(new String[rawData.get(0).size()]);
            for (int i = 1; i < users.length; i++) {
                String csvUser = users[i];
                Matcher matcher = Pattern.compile("\\(([0-9]+)\\)").matcher(csvUser);
                if (matcher.find()) {
                    String userID = matcher.group(1);
                    userGames.put(userID, new ArrayList<>());
                    users[i] = userID;
                }
            }

            //add games to users
            for (int row = 1; row < rawData.size(); row++) { //loop over games
                String[] gameRow = rawData.get(row).toArray(new String[rawData.get(row).size()]);
                String gameData = gameRow[0];
                Matcher matcher = Pattern.compile("(.+) \\(([0-9]+)\\)$").matcher(gameData);
                if (matcher.find()) {
                    String gameName = matcher.group(1);
                    Integer maxPlayers = Integer.parseInt(matcher.group(2));
                    gameMaxPlayers.put(gameName, maxPlayers);

                    for (int cell = 1; cell < gameRow.length; cell++) { //loop over each user for this game
                        boolean hasGame = gameRow[cell].equalsIgnoreCase("x");
                        if (hasGame) userGames.get(users[cell]).add(gameName);
                    }

                }
            }

            userGames.forEach((k, v) -> {
                System.out.println(k);
                System.out.println("  " + v);
            });
        }
    }

    /**
     * Gets the games a user has
     *
     * @param userID
     * @return
     */
    public List<String> getUserGames(String userID) {
        return userGames.get(userID);
    }

    /**
     * Gets the games a set of users is able to play together
     *
     * @param userIDs
     * @return
     */
    public List<String> getMatchingGames(List<String> userIDs) {

        //get all the games of all the users requested
        List<List<String>> gamesOfUsers = userIDs.stream()
                .filter(user -> this.getUserGames(user) != null)
                .map(user -> this.getUserGames(user))
                .collect(Collectors.toList());

        //convert to list of distinct matching games
        List<String> matchingGames = gamesOfUsers.stream()
                .flatMap(games -> games.stream())
                .distinct()
                .filter(game -> gamesOfUsers.stream().allMatch(userGames -> userGames.contains(game)))
                .collect(Collectors.toList());

        //filter to games that support enough players
        List<String> playableGames = matchingGames.stream()
                .filter(game -> gameMaxPlayers.get(game) >= userIDs.size())
                .collect(Collectors.toList());

        return playableGames;
    }

    private static Map<String, List<String>> userAliases = new HashMap<>();

    static {
        userAliases.put("109109946565537792", Arrays.asList("troy", "trey"));
        userAliases.put("109110807308029952", Arrays.asList("jeremy", "jerome"));
        userAliases.put("73463573900173312", Arrays.asList("josh", "jish", "joish"));
        userAliases.put("109109787517571072", Arrays.asList("gibs", "goobs", "gabs"));
        userAliases.put("73416411443113984", Arrays.asList("zac", "zacolio", "zacbell", "zeke", "zack"));
        userAliases.put("109109783952371712", Arrays.asList("ryan", "nayr", "rain"));
    }

    /**
     * Gets IDs associated with matching usernames or mentions from a set of command arguments
     *
     * @param arguments
     * @return
     */
    public static List<String> getMatchingUsers(String[] arguments) {
        List<String> matchingUsers = new ArrayList<>();
        for (String arg : arguments) {

            for (String userID : userAliases.keySet()) {
                if (!matchingUsers.contains(userID)) {
                    if (arg.matches("<@!?" + userID + ">") || userAliases.get(userID).contains(arg.toLowerCase())) {
                        matchingUsers.add(userID);
                        break;
                    }
                }
            }

        }
        return matchingUsers;
    }

}