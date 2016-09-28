package cback;

import org.apache.commons.io.FileUtils;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.sql.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class Util {

    public static void sendMessage(IChannel channel, String message) {
        try {
            channel.sendMessage(message);
        } catch (Exception ignored) {
        }
    }

    public static void deleteMessage(IMessage message) {
        try {
            message.delete();
        } catch (Exception ignored) {
        }
    }

    public static void sendPrivateMessage(IUser user, String message) {
        try {
            user.getClient().getOrCreatePMChannel(user).sendMessage(message);
        } catch (Exception ignored) {
        }
    }

    public static File botPath;

    static {
        try {
            botPath = new File(BootyBot.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()).getParentFile();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    //Properties File Things
    public static Map<String, String> properties;

    public static Map<String, String> readProperties() {
        Map<String, String> properties = new HashMap<>();
        File propFile = new File(Util.botPath, "properties.txt");
        try {
            if (propFile.exists()) {
                List<String> lines = FileUtils.readLines(propFile, Charset.defaultCharset());
                for (String line : lines) {
                    String[] args = line.split(" = ", 2);
                    properties.put(args[0], args[1]);
                }
                //Uncomment to display props on startup
                //lines.forEach(line -> System.out.println(line));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }

/*    public static void sqlStuff() {
        Connection connection = null;
        try {
            Class.forName("org.sqlite.JDBC");
            // create a database connection
            connection = DriverManager.getConnection("jdbc:sqlite:" + botPath + "/mydatabase.db");
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.

            statement.executeUpdate("create table settings (token string, status string, language string, torrent# int, admins string)");
            ResultSet rs = statement.executeQuery("select * from string");
            while(rs.next())
            {
                // read the result set
                System.out.println("name = " + rs.getString("name"));
                System.out.println("id = " + rs.getInt("id"));
            }
        } catch(Exception e) {
            System.err.println(e.getMessage());
        }
    }*/

    public static void writeProperties() {
        File propFile = new File(Util.botPath, "properties.txt");
        List<String> lines = properties.keySet().stream().map(key -> key + " = " + properties.get(key)).collect(Collectors.toList());
        try {
            FileUtils.writeLines(propFile, lines, false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
