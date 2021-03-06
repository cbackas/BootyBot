package bootybot.commands;

import bootybot.BootyBot;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;

import java.util.List;

public interface Command {

    String getName();

    List<String> getAliases();

    String getSyntax();

    String getDescription();

    void execute(Message message, String content, String[] args, Guild guild, Member author, BootyBot bootyBot);
}
