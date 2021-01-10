package bootybot.commands;

import bootybot.BootyBot;
import bootybot.games.GameList;
import bootybot.util.Util;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class CommandGame implements Command {

    private static Random rand = new Random();

    @Override
    public String getName() {
        return "game";
    }

    @Override
    public List<String> getAliases() {
        return null;
    }

    @Override
    public String getSyntax() {
        return "!game <user1> <user2>...";
    }

    @Override
    public String getDescription() {
        return "Suggests a random game you can all play.";
    }

    @Override
    public void execute(Message message, String content, String[] args, Guild guild, Member author, BootyBot bootyBot) {

        if (guild.getId().equals("73463428634648576")) {
            List<String> matchingUserIDs = GameList.getMatchingUsers(args);
            if (matchingUserIDs.size() == 0) matchingUserIDs.add(author.getUser().getId());
            List<String> matchingGames = bootyBot.getGameList().getMatchingGames(matchingUserIDs);
            if (matchingGames.size() > 0) {
                String randomGame = matchingGames.get(rand.nextInt(matchingGames.size()));
                String users = StringUtils.join(
                        matchingUserIDs.stream()
                                .map(userID -> {
                                    Member user = guild.getMemberById(userID);
                                    if (user != null) {
                                        return user.getEffectiveName();
                                    } else {
                                        return userID;
                                    }
                                })
                                .collect(Collectors.toList()), ", ");

                Util.simpleEmbed(message.getChannel(), "You should play **" + randomGame + "**. (" + users + ")", Util.getNewColor());
            } else {
                Util.simpleEmbed(message.getChannel(), "No games found.");
            }
        }
    }
}
