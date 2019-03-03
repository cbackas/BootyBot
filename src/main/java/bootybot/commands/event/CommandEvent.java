package bootybot.commands.event;

import bootybot.commands.Command;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.events.Event;

public class CommandEvent extends Event {

    private Command command;

    public CommandEvent(JDA api, Command command) {
        super(api);
        this.command = command;
    }


    public Command getCommand() {
        return command;
    }
}
