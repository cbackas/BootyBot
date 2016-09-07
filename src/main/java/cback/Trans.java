package cback;

import com.memetix.mst.language.Language;
import com.memetix.mst.translate.Translate;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IRole;
import sx.blah.discord.handle.obj.IUser;

import java.util.List;

public class Trans {
    public static Language currentLang;

    @EventSubscriber
    public void translateMessageEvent(MessageReceivedEvent event) {
        IMessage message = event.getMessage();
        String text = message.getContent();
        String author = message.getAuthor().getDisplayName(message.getGuild());
        if (message.getGuild().getID().equals("73463428634648576") && message.getChannel().getID().equals("73463428634648576")) {
            if (text.startsWith("//") || text.startsWith("!") || text.startsWith("`")) {
                //do nothing
            } else if (text.contains("http") || text.contains(".com") || text.contains(".net") || text.equals(".co.uk") || text.contains(".gov") || text.contains("www.") || text.contains(".org")) {
                Util.sendMessage(event.getClient().getChannelByID("199229124076634112"), author + ": " + text);
            } else {
                Translate.setClientId("ipaa");
                Translate.setClientSecret("ALyVSgi+7YPWucdwz5pyBxBpQE8QiGZkhGB6WIan2Zg=");
                String finalText = null;
                List<IUser> mentionsU = message.getMentions();
                List<IRole> mentionsG = message.getRoleMentions();
                try {
                    finalText = Translate.execute(text, Language.AUTO_DETECT, currentLang);
                } catch (Exception e) {
                    Util.sendMessage(event.getClient().getChannelByID("199229124076634112"), "**ERROR:** Translation failed");
                }
                if (mentionsU.isEmpty() && mentionsG.isEmpty()) {
                    Util.sendMessage(event.getClient().getChannelByID("199229124076634112"), author + ": " + finalText);
                } else {
                    for (IUser u : mentionsU) {
                        String displayName = "\\@" + u.getDisplayName(message.getGuild());
                        finalText = finalText.replace(u.mention(false), displayName).replace(u.mention(true), displayName);
                    }
                    for (IRole g : mentionsG) {
                        String displayName = "\\@" + g.getName();
                        finalText = finalText.replace(g.mention(), displayName).replace(g.mention(), displayName);
                    }
                    Util.sendMessage(event.getClient().getChannelByID("199229124076634112"), author + ": " + finalText);
                }
            }
        }
    }
}
