package cback;

import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.StatusChangeEvent;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.Status;

public class StatusChange {

    @EventSubscriber
    public void notifyStreamingEvent(StatusChangeEvent event) {
        IGuild guild = event.getClient().getGuildByID("73463428634648576");
        String user = event.getUser().getDisplayName(guild);
        Status.StatusType status = event.getNewStatus().getType();
        if (status == Status.StatusType.STREAM) {
            Util.sendMessage(event.getClient().getChannelByID("73463428634648576"), user + " is now streaming on twitch");
        }
    }

}
