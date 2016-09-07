package cback;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IVoiceChannel;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MessageBuilder;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;

import java.awt.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IPAAmessages {

    @EventSubscriber
    public void ipaaMessageEvent(MessageReceivedEvent event) {
        IMessage message = event.getMessage();
        String text = message.getContent();
        String user = message.getAuthor().getID();
        if (message.getGuild().getID().equals("73463428634648576")) {
            String zacsroom = "zgibson";
            if (text.equalsIgnoreCase("//scrubin")) {
                if (user.equals("73463573900173312") || user.equals("73416411443113984")) {
                    Util.sendMessage(message.getChannel(), "You might want to consult Pete on that one. :mask: :thermometer_face:  http://rabb.it/" + zacsroom);
                    List<IVoiceChannel> channels = message.getAuthor().getConnectedVoiceChannels();
                    if (channels.isEmpty()) {
                        System.out.println("User not in a voice channel");
                    } else {
                        try {
                            IVoiceChannel newChannel = event.getClient().getVoiceChannelByID("131488358940540928");
                            message.getAuthor().moveToVoiceChannel(newChannel);
                        } catch (RateLimitException | DiscordException | MissingPermissionsException e) {
                            e.printStackTrace();
                        }
                    }
                }
            } else if (text.equalsIgnoreCase("//scrubout")) {
                String doctor = message.getAuthor().getID();
                if (doctor.equals("73463573900173312") || doctor.equals("73416411443113984")) {
                    Util.sendMessage(message.getChannel(), "Breaking the sterile field");
                    List<IVoiceChannel> channels = message.getAuthor().getConnectedVoiceChannels();
                    if (channels.isEmpty()) {
                        System.out.println("User not in a voice channel");
                    } else {
                        try {
                            IVoiceChannel newChannel = event.getClient().getVoiceChannelByID("173638379462852609");
                            message.getAuthor().moveToVoiceChannel(newChannel);
                        } catch (RateLimitException | DiscordException | MissingPermissionsException e) {
                            e.printStackTrace();
                        }
                    }
                }
            } else if (text.toLowerCase().startsWith("//room")) {
                Pattern pattern = Pattern.compile("^//room (.+)");
                Matcher matcher = pattern.matcher(text);
                if (matcher.find()) {
                    String room = matcher.group(1);
                    Rabbit rabbit = Rabbit.getURLFromCommand(room);
                    if (rabbit != null) {
                        Util.sendMessage(message.getChannel(), rabbit.url);
                    } else {
                        Util.sendMessage(message.getChannel(), "Room not found");
                    }
                } else {
                    IDiscordClient client = event.getClient();
                    try {
                        new MessageBuilder(client).withChannel(message.getChannel()).withCode("XL", "USAGE: //room <name>").send();
                    } catch (RateLimitException | DiscordException | MissingPermissionsException e) {
                        e.printStackTrace();
                    }
                }
            } else if (text.toLowerCase().startsWith("//color")) {
                Roles role = Roles.getUserRole(user);
                if (role != null) {
                    String[] args = text.split(" ");
                    if (args.length >= 2) {
                        String argument = args[1];
                        Color currentColor = message.getGuild().getRoleByID(role.role).getColor();
                        String hexColor = Integer.toHexString(currentColor.getRGB() & 0xffffff);
                        if (hexColor.length() < 6) {
                            hexColor = "000000".substring(0, 6 - hexColor.length()) + hexColor;
                        }
                        if (argument.startsWith("#")) {
                            Color color = Color.decode(argument);
                            try {
                                message.getGuild().getRoleByID(role.role).changeColor(color);
                            } catch (RateLimitException | DiscordException | MissingPermissionsException e) {
                                e.printStackTrace();
                            }
                            String target = message.getGuild().getRoleByID(role.role).mention();
                            Util.sendMessage(message.getChannel(), "Color for " + target + " changed from #" + hexColor.toUpperCase() + " to " + argument.toUpperCase());
                        } else if (argument.equalsIgnoreCase("current")) {
                            String target = message.getGuild().getRoleByID(role.role).mention();
                            Util.sendMessage(message.getChannel(), "The current value of " + target + "'s color is #" + hexColor.toUpperCase());
                        } else {
                            try {
                                new MessageBuilder(event.getClient()).withChannel(message.getChannel()).withCode("XL", "ERROR: hex value must contain #").send();
                            } catch (RateLimitException | DiscordException | MissingPermissionsException e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        try {
                            new MessageBuilder(event.getClient()).withChannel(message.getChannel()).withCode("XL", "USAGE: //color <hex value> MUST CONTAIN #").send();
                        } catch (RateLimitException | DiscordException | MissingPermissionsException e) {
                            e.printStackTrace();
                        }
                    }
                    Util.deleteMessage(message);
                }
            }
        }
    }

}
