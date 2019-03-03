package bootybot.commands;

import bootybot.BootyBot;
import bootybot.games.GameList;
import bootybot.util.Util;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

public class CommandGames implements Command {
    @Override
    public String getName() {
        return "games";
    }

    @Override
    public List<String> getAliases() {
        return null;
    }

    @Override
    public String getSyntax() {
        return "!games <user1> <user2>...";
    }

    @Override
    public String getDescription() {
        return "Lists the games you can all play.";
    }

    @Override
    public void execute(Message message, String content, String[] args, Guild guild, Member author, BootyBot bootyBot) {

        if (guild.getId().equalsIgnoreCase("73463428634648576")) {
            List<String> matchingUserIDs = GameList.getMatchingUsers(args);
            if (matchingUserIDs.size() == 0) matchingUserIDs.add(message.getAuthor().getId());
            List<String> matchingGames = bootyBot.getGameList().getMatchingGames(matchingUserIDs);
            if (matchingGames.size() > 0) {
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

                Util.simpleEmbed(message.getChannel(), "**Games (" + users + ") can play together:**\n" + StringUtils.join(matchingGames, "\n"));
            } else {
                Util.simpleEmbed(message.getChannel(), "No games found.");
            }
        }
    }

}
