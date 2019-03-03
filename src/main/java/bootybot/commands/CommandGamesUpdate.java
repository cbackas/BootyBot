package bootybot.commands;

import bootybot.BootyBot;
import bootybot.util.Util;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;

import java.awt.*;
import java.util.List;

public class CommandGamesUpdate implements Command {
    @Override
    public String getName() {
        return "updategames";
    }

    @Override
    public List<String> getAliases() {
        return null;
    }

    @Override
    public String getSyntax() {
        return null;
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public void execute(Message message, String content, String[] args, Guild guild, Member author, BootyBot bootyBot) {

        if (guild.getId().equalsIgnoreCase("73463428634648576")) {
            Util.simpleEmbed(message.getChannel(), "Updating games from Google Sheet...", Color.YELLOW);
            bootyBot.getGameList().updateGameData();
            Util.simpleEmbed(message.getChannel(), "Updated.", Color.GREEN);
        }

    }
}
