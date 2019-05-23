package bootybot;

import bootybot.commands.Command;
import bootybot.util.Util;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class CommandManager extends ListenerAdapter {

    private static Pattern BOOTY_PATTERN = Pattern.compile("boo+[td]+y+", Pattern.CASE_INSENSITIVE);

    private BootyBot bootyBot;
    private List<Command> registeredCommands = new ArrayList<>();
    private static final Pattern COMMAND_PATTERN = Pattern.compile("^[?!]([^\\s]+) ?(.*)", Pattern.CASE_INSENSITIVE);

    public CommandManager(BootyBot bootyBot) {
        this.bootyBot = bootyBot;
        bootyBot.getClient().addEventListener(this);
        registerAllCommands();
    }

    private void registerAllCommands() {
        new Reflections("bootybot.commands").getSubTypesOf(Command.class).forEach(commandImpl -> {
            try {
                Command command = commandImpl.getDeclaredConstructor().newInstance();
                Optional<Command> commandOptional = registeredCommands.stream().filter(cmd -> cmd.getName().equalsIgnoreCase(command.getName())).findAny();
                if (commandOptional.isEmpty()) {
                    registeredCommands.add(command);
                } else {
                    System.out.println("Attempted to register two commands with the same name: " + commandOptional.get().getName());
                    System.out.println("Existing: " + commandOptional.get().getClass().getName());
                    System.out.println("Attempted: " + commandImpl.getName());
                }
            } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                e.printStackTrace();
            }
        });

        System.out.println("Registered commands: " + registeredCommands.stream().map(Command::getName).collect(Collectors.joining(", ")));
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return; //ignore bot messages
        String text = event.getMessage().getContentDisplay();

        if (event.getChannelType() == ChannelType.TEXT) {
            //process command if found
            Matcher matcher = COMMAND_PATTERN.matcher(text);
            if (matcher.matches()) {

                System.out.println(event.getAuthor().getName() + "#" + event.getAuthor().getDiscriminator() + " issued command '" + text + "' in #" + event.getChannel().getName());

                String baseCommand = matcher.group(1).toLowerCase();
                Optional<Command> foundCommand = registeredCommands.stream()
                        .filter(com -> com.getName().equalsIgnoreCase(baseCommand) || (com.getAliases() != null && com.getAliases().contains(baseCommand)))
                        .findAny();
                if (foundCommand.isPresent()) { //is registered command

                    Command command = foundCommand.get();

                    String args = matcher.group(2);
                    String[] argsArr = args.isEmpty() ? new String[0] : args.split(" ");
                    command.execute(event.getMessage(), event.getMessage().getContentRaw(), argsArr, event.getGuild(), event.getMember(), bootyBot);
                }
            }
        }

        if(BOOTY_PATTERN.matcher(event.getMessage().getContentRaw()).find()){
            Util.sendMessage(event.getChannel(), "Booty");
        }


    }
}
