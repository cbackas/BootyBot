package cback;

import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IMessage;

public class Globalmessages {

    @EventSubscriber
    public void globalMessageEvent(MessageReceivedEvent event) {
        IMessage message = event.getMessage();
        String text = message.getContent();
        if (text.equalsIgnoreCase("//booty")) {
            String sender = message.getAuthor().mention();
            Util.sendMessage(message.getChannel(), sender + " says booty");
            Util.deleteMessage(message);
        } else if (text.equalsIgnoreCase("//list") || text.equalsIgnoreCase("//help")) {
            if (message.getGuild().getID().equals("73463428634648576")) {
                Util.sendMessage(message.getChannel(), "**Commands** \n  ``//booty`` - says booty \n  ``//color <#HEXVAL>`` - changes the color of your name \n  ``//color current`` - shows you the HEX value of your current color \n  ``//room <name>`` - returns the url of the desired Rabbit room \n      available rooms: ``zac``, ``josh``, ``troy``, ``jeremy``, ``gibs``, ``nayr``\n  ``//scrubin`` - scrubs in (doctors only) \n  ``//scrubout`` - scrubs out (doctors only) \n  ``//list`` - lists all commands current available \n \n use ``@BootyBot`` to get information about the bot");
            } else {
                Util.sendMessage(message.getChannel(), "**Commands** \n  ``//booty`` - says booty");
            }
        } else if (text.equals("<@194546381094912001>")) {
            int serverCount = event.getClient().getGuilds().size();
            Util.sendMessage(message.getChannel(), "BootyBot discord bot \n Created by *cback* \n Connected to " + serverCount + " communities \n \n For a list of commands use ``//list``");
        }
    }

}
