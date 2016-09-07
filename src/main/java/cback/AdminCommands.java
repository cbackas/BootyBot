package cback;

import com.memetix.mst.language.Language;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.Status;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AdminCommands {
    public static String adminsFromFile;

    @EventSubscriber
    public void adminCommands(MessageReceivedEvent event) {
        IMessage message = event.getMessage();
        String text = message.getContent();
        List<String> admins = Arrays.asList(adminsFromFile.split(", "));
        if (admins.contains(message.getAuthor().getID())) {
            //Set Bot Status
            if (text.startsWith("//status")) {
                Pattern pattern = Pattern.compile("^//status (.+)");
                Matcher matcher = pattern.matcher(text);
                if (matcher.find()) {
                    String status = matcher.group(1);
                    if (status.equalsIgnoreCase("clear")) {
                        event.getClient().changeStatus(Status.empty());
                    } else {
                        event.getClient().changeStatus(Status.game(status));
                    }
                    Util.properties.put("status", status);
                    Util.writeProperties();
                    Util.deleteMessage(message);
                }
                //Say things with the bot
            } else if (message.getContent().startsWith("//say")) {
                Pattern pattern = Pattern.compile("^//say (.+) \"(.+)\"");
                Matcher matcher = pattern.matcher(message.getContent());
                if (matcher.find()) {
                    String channel = matcher.group(1);
                    String sayText = matcher.group(2);
                    Util.sendMessage(event.getClient().getChannelByID(channel), sayText);
                    Util.deleteMessage(message);
                }
            } else if (text.startsWith("//setlanguage")) {
                Pattern pattern = Pattern.compile("^//setlanguage (.+)");
                Matcher matcher = pattern.matcher(text);
                if (matcher.find()) {
                    String language = matcher.group(1);
                    Language lang = Language.valueOf(language.toUpperCase());
                    if (lang != null) {
                        Trans.currentLang = lang;
                        Util.properties.put("language", language);
                        Util.writeProperties();
                    }
                }
            }
        }
    }

}
