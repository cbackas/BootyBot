package troy;

import cback.Util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;

public class TorrentAcceptor implements Runnable {
    private MediaBotFeatures bot;
    private Socket clientSocket;

    public TorrentAcceptor(MediaBotFeatures bot, Socket clientSocket) {
        this.bot = bot;
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            System.out.println("[MEDIABOT] Torrent received.");
            String data = in.readLine();
            String[] contents = data.split(":::");
            String message = null;
            if (contents[0].equalsIgnoreCase("add")) {
                message = "**Torrent Added:**  " + contents[1];
            } else if (contents[0].equalsIgnoreCase("complete")) {
                String countString = Util.readProperties().get("torrent#");
                int newCount = Integer.parseInt(countString) + 1;
                message = "*#" + newCount + "* **Torrent Complete:**  " + contents[1];
                Util.properties.put("torrent#", Integer.toString(newCount));
                Util.writeProperties();
                if (newCount % 500 == 0) {
                    Util.sendMessage(bot.getChannel().getClient().getChannelByID("168983653760630784"), "**" + Integer.toString(newCount) + "torrents downloaded! Way to break the law!** \n https://media.giphy.com/media/XreQmk7ETCak0/giphy.gif");
                }
            }

            if (message != null) {
                if (bot.getChannel().getClient().isReady() && bot.getChannel().getClient() != null) {
                    try {
                        bot.getChannel().sendMessage(message);
                        bot.processQueue();
                    } catch (Exception e) {
                        System.out.println("[MEDIABOT] Failed to send message, added to queue: " + message);
                        bot.getQueue().add(message);
                    }
                } else {
                    System.out.println("[MEDIABOT] Channel not available, added to queue: " + message);
                    bot.getQueue().add(message);
                }
            } else {
                System.out.println("[MEDIABOT] Invalid format: " + data);
            }

            in.close();
            clientSocket.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}