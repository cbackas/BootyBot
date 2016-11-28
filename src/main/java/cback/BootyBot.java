package cback;

import cback.message_events.AdminCommands;
import cback.message_events.Globalmessages;
import cback.message_events.IPAAmessages;
import cback.message_events.Privatemessages;
import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.*;
import sx.blah.discord.handle.obj.*;
import sx.blah.discord.util.DiscordException;

public class BootyBot {
    public IDiscordClient client;

    public static void main(String[] args) {
        new BootyBot();
    }

    public BootyBot() {
        Util.properties = Util.readProperties(); //Populate those properties girl
        connect();
        client.getDispatcher().registerListener(this);
        client.getDispatcher().registerListener(new IPAAmessages());
        client.getDispatcher().registerListener(new Globalmessages());
        client.getDispatcher().registerListener(new Privatemessages());
        client.getDispatcher().registerListener(new LeaveJoinBan());
        client.getDispatcher().registerListener(new StatusChange());
        client.getDispatcher().registerListener(new AdminCommands());
    }

    private void connect() {
        ClientBuilder clientBuilder = new ClientBuilder(); //Creates the ClientBuilder instance
        clientBuilder.withToken(Util.properties.get("token")); //Token stored in properties.txt
        clientBuilder.setMaxReconnectAttempts(5);
        try {
            client = clientBuilder.login();
        } catch (DiscordException e) {
            e.printStackTrace();
        }
    }

    @EventSubscriber
    public void onReadyEvent(ReadyEvent event) {
        System.out.println("Logged in.");

        //Do properties things
        AdminCommands.adminsFromFile = Util.properties.get("admins");
        String alpha = Util.properties.get("status");
        if (alpha.equalsIgnoreCase("clear")) {
            client.changeStatus(Status.empty());
        } else {
            client.changeStatus(Status.game(Util.properties.get("status")));
        }
    }

    @EventSubscriber
    public void onDisconnectEvent(DiscordDisconnectedEvent event) {
        System.out.println("BOT DISCONNECTED");
        System.out.println("Reason: " + event.getReason());
    }

}
