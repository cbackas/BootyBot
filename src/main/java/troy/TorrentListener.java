package troy;

import java.net.ServerSocket;
import java.net.Socket;

public class TorrentListener implements Runnable {

    private MediaBotFeatures bot;
    private static final String mediaServerIP = MediaBotFeatures.getMediaServerIP().trim();

    public TorrentListener(MediaBotFeatures bot) {
        this.bot = bot;
    }

    @Override
    public void run() {
        try {
            System.out.println("[MEDIABOT] Listening for connections..");
            @SuppressWarnings("resource")
            ServerSocket serverSocket = new ServerSocket(1337);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                String ipAddress = clientSocket.getInetAddress().getHostAddress();
                if (ipAddress.equalsIgnoreCase(mediaServerIP)) {
                    Thread thread = new Thread(new TorrentAcceptor(bot, clientSocket));
                    thread.start();
                } else {
                    System.out.println("[MEDIABOT] Received data from unknown IP: " + ipAddress + " (closed connection, not accepting data");
                    clientSocket.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
