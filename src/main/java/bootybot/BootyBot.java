package bootybot;

import bootybot.games.GameList;
import bootybot.util.Util;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.Optional;

public class BootyBot extends ListenerAdapter {

    private static BootyBot instance;

    private JDA client;
    private GameList gameList;
    private MemberManager memberManager;

    public BootyBot() {
        if (instance == null) instance = this;

        connect();

        //instantiate game list
        gameList = new GameList(this);
        //instantiate the member restorer
        memberManager = new MemberManager(this);
        //instantiate the command manager (we do not need to save its instance)
        new CommandManager(this);


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
            this.client = JDABuilder.createDefault(token.get())
                    .setAutoReconnect(true)
                    .addEventListeners(this)
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

    public GameList getGameList() {
        return gameList;
    }

    public MemberManager getMemberManager() {
        return memberManager;
    }

    public static BootyBot getInstance() {
        return instance;
    }

}
