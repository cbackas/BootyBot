package cback;

import com.memetix.mst.language.Language;
import org.apache.commons.io.FileUtils;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.Status;

import java.io.File;
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
            if (text.toLowerCase().startsWith("//status")) {
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
                } else {
                    Util.sendMessage(message.getChannel(), "**ERROR:** Something weird happened. Could not match arguments.");
                }
                Util.deleteMessage(message);
                //Say things with the bot
            } else if (text.toLowerCase().startsWith("//say")) {
                Pattern pattern = Pattern.compile("^//say (.+) \"(.+)\"");
                Matcher matcher = pattern.matcher(message.getContent());
                if (matcher.find()) {
                    String channel = matcher.group(1);
                    String sayText = matcher.group(2);
                    Util.sendMessage(event.getClient().getChannelByID(channel), sayText);
                } else {
                    Util.sendMessage(message.getChannel(), "**ERROR:** Something weird happened. Could not match arguments.");
                }
                Util.deleteMessage(message);
                //Set the language of the translation channel
            } else if (text.toLowerCase().startsWith("//setlanguage")) {
                Pattern pattern = Pattern.compile("^//setlanguage (.+)");
                Matcher matcher = pattern.matcher(text);
                if (matcher.find()) {
                    String language = matcher.group(1);
                    Language lang = Language.valueOf(language.toUpperCase());
                    if (lang != null) {
                        Trans.currentLang = lang;
                        Util.properties.put("language", language);
                        Util.writeProperties();
                        Util.sendMessage(message.getChannel(), "Translation language updated to " + language);
                    } else {
                        Util.sendMessage(message.getChannel(), "**ERROR:** Desired language not found");
                    }
                    Util.deleteMessage(message);
                }
                //Set the MediaIP in the file
            } else if (text.toLowerCase().startsWith("//mediaip")) {
                String ip = null;
                Pattern pattern = Pattern.compile("^//mediaip (.+)");
                Matcher matcher = pattern.matcher(text);
                if (matcher.find()) {
                    ip = matcher.group(1);
                    File ipFile = new File(Util.botPath, "mediaIP.txt");
                    try {
                        if (ipFile.exists()) {
                            FileUtils.deleteQuietly(ipFile);
                        }
                        FileUtils.writeStringToFile(ipFile, ip, (String) null);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (ip != null) {
                    Util.sendMessage(message.getChannel(), "MediaIP updated to " + ip);
                } else {
                    Util.sendMessage(message.getChannel(), "MediaIP update failed - ip is null");
                }
                Util.deleteMessage(message);
            }
        }
    }

}
