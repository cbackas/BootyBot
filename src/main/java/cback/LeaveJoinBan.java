package cback;

import cback.enums.Roles;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.UserBanEvent;
import sx.blah.discord.handle.impl.events.UserJoinEvent;
import sx.blah.discord.handle.impl.events.UserLeaveEvent;
import sx.blah.discord.handle.obj.IRole;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;

public class LeaveJoinBan {

    @EventSubscriber
    public void bannedUser(UserBanEvent event) {
        String bannedguy = event.getUser().toString();
        String server = event.getGuild().getID();
        String displayName = event.getUser().getDisplayName(event.getGuild());
        Util.sendMessage(event.getClient().getChannelByID(server), bannedguy + " was banned from the server");
        System.out.println(displayName + " was banned");
    }

    @EventSubscriber
    public void userLeft(UserLeaveEvent event) {
        String leftguy = event.getUser().toString();
        String server = event.getGuild().getID();
        String displayName = event.getUser().getDisplayName(event.getGuild());
        Util.sendMessage(event.getClient().getChannelByID(server), leftguy + " left the server D:");
        System.out.print(displayName + " left the server");
    }

    @EventSubscriber
    public void userJoined(UserJoinEvent event) throws DiscordException {
        IUser joinedguy = event.getUser();
        String user = event.getUser().getID();
        String server = event.getGuild().getID();
        Util.sendMessage(event.getClient().getChannelByID(server), joinedguy.toString() + " joined the server");
        //Permissions
        if (server.equals("73463428634648576")) {
            Roles role = Roles.getUserRole(user);
            if (role != null) {
                IRole[] roles = {event.getGuild().getRoleByID(role.role), event.getGuild().getRoleByID(role.human)};
                try {
                    event.getGuild().editUserRoles(joinedguy, roles);
                    event.getGuild().setUserNickname(joinedguy, role.nick);
                    Util.sendMessage(event.getClient().getChannelByID(server), joinedguy.toString() + "'s perms were updated");
                } catch (RateLimitException | MissingPermissionsException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
