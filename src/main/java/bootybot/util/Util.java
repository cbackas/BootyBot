package bootybot.util;

import bootybot.BootyBot;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.*;
import org.apache.commons.io.FileUtils;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.time.Instant;
import java.util.Optional;
import java.util.Random;
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
            File jarPath = new File(BootyBot.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()).getParentFile();
            botPath = new File(jarPath, "data");
            botPath.mkdir();
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
            System.out.println("Failed to send Message!");
            e.printStackTrace();
            reportHome("Failed to send Message in " + channel.getName(), e, null);
        }
    }

    public static void sendEmbed(MessageChannel channel, MessageEmbed embed) {
        try {
            channel.sendMessage(embed).queue();
        } catch (Exception e) {
            System.out.println("Failed to send Embed!");
            e.printStackTrace();
            reportHome("Failed to send Embed in " + channel.getName(), e, null);
        }
    }

    public static void simpleEmbed(MessageChannel channel, String message) {
        try {
            MessageEmbed embed = new EmbedBuilder().appendDescription(message).setColor(Color.ORANGE).build();
            channel.sendMessage(embed).queue();
        } catch (Exception e) {
            System.out.println("Failed to send Simple Embed");
            reportHome("Failed to send Simple Embed in " + channel.getName(), e, null);
        }
    }

    public static void simpleEmbed(MessageChannel channel, String message, Color color) {
        try {
            MessageEmbed embed = new EmbedBuilder().appendDescription(message).setColor(color).build();
            channel.sendMessage(embed).queue();
        } catch (Exception e) {
            System.out.println("Failed to send Simple Embed");
            reportHome("Failed to send Simple Embed in " + channel.getName(), e, null);
        }
    }

    public static void deleteMessage(Message message) {
        try {
            message.delete().queue();
        } catch (Exception e) {
            System.out.println("Failed to delete message in #" + message.getChannel().getName());
            System.out.println(e.getMessage());
            reportHome("Failed to delete message in #" + message.getChannel().getName(), e, message);
        }

    }

    private static Color[] colors = {
            Color.CYAN,
            Color.GREEN,
            Color.ORANGE,
            Color.YELLOW,
            Color.RED,
            Color.MAGENTA,
            Color.PINK
    };

    /**
     * Returns a random color from ^ that list
     */
    public static Color getNewColor() {
        int random = new Random().nextInt(colors.length);
        return colors[random];
    }

    /**
     * Send report to the Hub
     */
    public static void reportHome(String text, Exception e, Message message) {
        TextChannel errorChannel = BootyBot.getInstance().getClient().getTextChannelById(346104666796589056L);

        // Does some stuff to build a string and make sure its not too long for the discord message
        StringBuilder stack = new StringBuilder();
        for (StackTraceElement s : e.getStackTrace()) {
            stack.append(s.toString());
            stack.append("\n");
        }
        String stackString = stack.toString();
        if (stackString.length() > 800) {
            stackString = stackString.substring(0, 800);
        }

        // build report embed
        EmbedBuilder bld = new EmbedBuilder()
                .setColor(Color.orange)
                .setTimestamp(Instant.now());

        // includes message info if its given
        if (message != null) {
            bld
                    .setAuthor(message.getAuthor().getName() + '#' + message.getAuthor().getDiscriminator(), message.getAuthor().getEffectiveAvatarUrl())
                    .appendDescription(message.getContentRaw())
                    .addBlankField(false);
        }

        // print details of the error
        bld
                .appendDescription(text)
                .addField("Exception:", e.toString(), false)
                .addField("Stack:", stackString, false);

        //send to discord
        try {
            errorChannel.sendMessage(bld.build()).queue();
        } catch (Exception ex) {
            e.printStackTrace();
        }
    }

}
