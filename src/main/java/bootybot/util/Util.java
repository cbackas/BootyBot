package bootybot.util;

import bootybot.BootyBot;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * Created by Troy on 4/21/2018.
 */
public class Util {
    public static File botPath;

    private static final Pattern USER_MENTION_PATTERN = Pattern.compile("^<@!?(\\d+)>$");

    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    static {
        try {
            botPath = new File(BootyBot.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()).getParentFile();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public static Optional<String> getToken(String name) {
        try {
            File tokenFile = new File(botPath, name + ".txt");
            if (tokenFile.exists()) {
                String token = FileUtils.readLines(tokenFile, Charset.defaultCharset()).get(0);
                if (!token.equalsIgnoreCase("TOKEN") && !token.isEmpty()) {
                    return Optional.of(token);
                } else {
                    return Optional.empty();
                }
            } else {
                FileUtils.writeStringToFile(tokenFile, "TOKEN", (String) null);
                return Optional.empty();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public static void sendMessage(MessageChannel channel, String message) {
        try {
            channel.sendMessage(message).queue();
        } catch (Exception e) {
            System.out.println("Failed to send message!");
            e.printStackTrace();
        }

    }

    public static void deleteMessage(Message message) {
        try {
            message.delete().queue();
        } catch (Exception e) {
            System.out.println("Failed to delete message in #" + message.getChannel().getName());
            System.out.println(e.getMessage());
        }

    }

    public static String formatName(User user) {
        return user.getName() + '#' + user.getDiscriminator();
    }

    public static String getAvatar(User user) {
        return user.getAvatarUrl() != null ? user.getAvatarUrl() : "https://discordapp.com/assets/322c936a8c8be1b803cd94861bdfa868.png";
    }
}
