package troy;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.FileUtils;
import sx.blah.discord.handle.impl.events.GuildCreateEvent;
import sx.blah.discord.handle.obj.IChannel;

public class MediaBotFeatures {

    private static MediaBotFeatures instance;
    public static File botPath = new File("874389765AAAA4758");

    static {
        try {
            botPath = new File(MediaBotFeatures.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()).getParentFile();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private IChannel channel;
    private List<String> queue = new ArrayList<>();


    public MediaBotFeatures(IChannel channel) {
        Thread torrentListener = new Thread(new TorrentListener(this));
        torrentListener.start();
    }

    public static void onGuildCreate(GuildCreateEvent event) {
        if (event.getGuild().getID().equalsIgnoreCase("73463428634648576")) {
            IChannel mediaChannel = event.getGuild().getChannelByID("168983653760630784");
            if (mediaChannel != null) {
                if (instance == null) instance = new MediaBotFeatures(mediaChannel);
                instance.channel = mediaChannel;
                System.out.println("[MEDIABOT] Connected to mediaserver channel.");
                instance.processQueue();
            }
        }
    }

    public static String getMediaServerIP() {
        try {
            File ipFile = new File(botPath, "mediaIP.txt");
            if (ipFile.exists()) {
                String ip = FileUtils.readFileToString(ipFile, (String) null);
                if (!ip.isEmpty()) {
                    return ip;
                } else {
                    return null;
                }
            } else {
                FileUtils.writeStringToFile(ipFile, "0.0.0.0", (String) null);
                return "0.0.0.0";
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void processQueue() {
        if (channel != null) {
            for (Iterator<String> iterator = queue.iterator(); iterator.hasNext(); ) {
                String message = iterator.next();
                try {
                    channel.sendMessage(message);
                    iterator.remove();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public IChannel getChannel() {
        return channel;
    }

    public List<String> getQueue() {
        return queue;
    }

}
