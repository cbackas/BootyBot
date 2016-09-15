package cback.message_events;

import cback.Util;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IMessage;

public class Privatemessages {

    @EventSubscriber
    public void privateMessageEvent(MessageReceivedEvent event) {
        IMessage message = event.getMessage();
        if (message.getChannel().isPrivate()) {
            String username = message.getAuthor().getName();
            if (event.getClient().getUserByID("73416411443113984") != null) {
                Util.sendPrivateMessage(event.getClient().getUserByID("73416411443113984"), username + ": " + message.getContent());
            }
        }
    }

}
