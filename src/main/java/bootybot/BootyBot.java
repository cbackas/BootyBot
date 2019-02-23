package bootybot;

import bootybot.util.Util;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.util.Optional;

public class BootyBot extends ListenerAdapter {

    public static BootyBot instance;
    private JDA client;


    public BootyBot() {
        if (instance == null) instance = this;
        connect();

        //client.addEventListener(commandManager);
        //client.addEventListener(eventWaiter);

    }

    public void connect() {
        Optional<String> token = Util.getToken("bottoken");
        if (token.isEmpty()) {
            System.out.println("-------------------------------------");
            System.out.println("Insert your bot's token in bottoken.txt");
            System.out.println("Exiting......");
            System.out.println("-------------------------------------");
            System.exit(0);
            return;
        }

        try {
            this.client = new JDABuilder(AccountType.BOT)
                    .setToken(token.get())
                    .setAudioEnabled(true)
                    .setAutoReconnect(true)
                    .addEventListener(this)
                    .build();

            //Wait until JDA is ready
            client.awaitReady();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onReady(ReadyEvent event) {
        System.out.println("Connected to Discord successfully.");
    }

    public JDA getClient() {
        return client;
    }
}
