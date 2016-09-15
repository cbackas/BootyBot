package cback.message_events;

import cback.Util;
import com.memetix.mst.language.Language;
import com.memetix.mst.translate.Translate;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IMessage;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Globalmessages {

    @EventSubscriber
    public void globalMessageEvent(MessageReceivedEvent event) {
        IMessage message = event.getMessage();
        String text = message.getContent();
        //Commands
        //Booty
        if (text.equalsIgnoreCase("//booty")) {
            String sender = message.getAuthor().mention();
            Util.sendMessage(message.getChannel(), sender + " says booty");
            Util.deleteMessage(message);
            //Random Number
        } else if (text.toLowerCase().startsWith("//roll")) {
            Pattern pattern = Pattern.compile("^//roll (-?[0-9]+)");
            Matcher matcher = pattern.matcher(text);
            if (matcher.matches()) {
                try {
                    String rollInput = matcher.group(1);
                    int rollInt = Integer.parseInt(rollInput);
                    if (rollInt > 0) {
                        Random rand = new Random();
                        int n = rand.nextInt(rollInt) + 1;
                        Util.sendMessage(message.getChannel(), Integer.toString(n));
                    } else {
                        Util.sendMessage(message.getChannel(), "Lol that's too small of a number you silly goose");
                    }
                } catch (Exception e) {
                    System.out.println("ERROR: requested roll number too large");
                }
            } else {
                Util.sendMessage(message.getChannel(), "**ERROR:** number not recognized");
            }

            //Translate
        } else if (text.toLowerCase().startsWith("//translate")) {
            if (text.contains("\"")) {
                Pattern pattern = Pattern.compile("^//translate (.+) \"(.+)\"");
                Matcher matcher = pattern.matcher(text);
                if (matcher.find()) {
                    String language = matcher.group(1);
                    String translate = matcher.group(2);
                    Translate.setClientId("ipaa");
                    Translate.setClientSecret("ALyVSgi+7YPWucdwz5pyBxBpQE8QiGZkhGB6WIan2Zg=");
                    String translatedText = null;
                    try {
                        Language lang = Language.valueOf(language.toUpperCase());
                        translatedText = Translate.execute(translate, Language.AUTO_DETECT, lang);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Util.sendMessage(message.getChannel(), "**ERROR**: Is that a valid language?");
                    }
                    Util.sendMessage(message.getChannel(), translatedText);
                }
            } else {
                Util.sendMessage(message.getChannel(), "**Command usage:** \n     ``/translate desiredlanguage \"text to translate\"``");
            }
            //Flip a coin
        } else if (text.equalsIgnoreCase("//coin")) {
            Random rand = new Random();
            int n = rand.nextInt(2);
            if (n == 0) {
                Util.sendMessage(message.getChannel(), "Heads");
            } else if (n == 1) {
                Util.sendMessage(message.getChannel(), "Tails");
            } else {
                Util.sendMessage(message.getChannel(), "Something weird happened but it's probably ok");
            }
        }


        //Command Listing
        else if (text.equalsIgnoreCase("//list") || text.equalsIgnoreCase("//help")) {
            if (message.getGuild().getID().equals("73463428634648576")) {
                Util.sendMessage(message.getChannel(), "**Commands**" +
                        " \n  ``//booty`` - says booty" +
                        " \n  ``//roll <#>`` - picks a random number from 1-your#" +
                        " \n  ``//translate language \"text\"`` - translates your text to desired language" +
                        " \n  ``//coin`` - flips a coin" +
                        " \n  ``//color <#HEXVAL>`` - changes the color of your name" +
                        " \n  ``//color current`` - shows you the HEX value of your current color" +
                        " \n  ``//room <name>`` - returns the url of the desired Rabbit room" +
                        " \n      available rooms: ``zac``, ``josh``, ``troy``, ``jeremy``, ``gibs``, ``nayr``" +
                        " \n  ``//scrubin`` - scrubs in (doctors only)" +
                        " \n  ``//scrubout`` - scrubs out (doctors only)" +
                        " \n  ``//list`` - lists all commands current available" +
                        " \n \n use ``@BootyBot`` to get information about the bot");
            } else {
                Util.sendMessage(message.getChannel(), "**Commands**" +
                        " \n  ``//booty`` - says booty" +
                        " \n  ``//roll <#>`` - picks a random number from 1-your#" +
                        " \n  ``//coin`` - flips a coin" +
                        " \n  ``//translate language \"text\"`` - translates your text to desired language" +
                        " \n  ");
            }
        } else if (text.equals("<@194546381094912001>")) {
            int serverCount = event.getClient().getGuilds().size();
            Util.sendMessage(message.getChannel(), "BootyBot discord bot \n Created by *cback* \n Connected to " + serverCount + " communities \n I'm poor <https://cash.me/$zgibson> \n \n For a list of commands use ``//list``");
        }
    }

}
