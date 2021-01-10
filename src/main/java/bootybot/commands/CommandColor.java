package bootybot.commands;

import bootybot.BootyBot;
import bootybot.util.Util;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;

import java.awt.*;
import java.util.List;

public class CommandColor implements Command {
    @Override
    public String getName() {
        return "color";
    }

    @Override
    public List<String> getAliases() {
        return null;
    }

    @Override
    public String getSyntax() {
        return "!color #[hex value]";
    }

    @Override
    public String getDescription() {
        return "Changes your color";
    }

    @Override
    public void execute(Message message, String content, String[] args, Guild guild, Member author, BootyBot bootyBot) {
        if(args.length >= 1){
            String colorArg = args[0];
            if(colorArg.startsWith("#")){
                try {
                    if(bootyBot.getMemberManager().getMember(author.getUser().getId()) != null) {
                        Color newColor = Color.decode(colorArg);
                        bootyBot.getMemberManager().setColor(author.getUser().getId(), newColor);
                        Util.simpleEmbed(message.getChannel(), "Changed color to " + toHex(newColor), newColor);
                    } else {
                        Util.simpleEmbed(message.getChannel(), "You can't change your color, you're not on the list bud.");
                    }
                }catch (Exception e){
                    Util.simpleEmbed(message.getChannel(),"Invalid color");
                }

            } else if (colorArg.equalsIgnoreCase("current")){
                Color currentColor = bootyBot.getMemberManager().getColor(author.getUser().getId());
                if(currentColor == null){
                    Util.simpleEmbed(message.getChannel(), "You don't have a color?");
                } else {
                    Util.simpleEmbed(message.getChannel(), "Your current color is " + toHex(currentColor), currentColor);
                }
            } else if(colorArg.equalsIgnoreCase("random")){
                if(bootyBot.getMemberManager().getMember(author.getUser().getId()) != null) {
                    Color newColor = new Color((int)(Math.random() * 0x1000000));
                    bootyBot.getMemberManager().setColor(author.getUser().getId(), newColor);
                    Util.simpleEmbed(message.getChannel(), "Changed color to " + toHex(newColor), newColor);
                } else {
                    Util.simpleEmbed(message.getChannel(),"You can't change your color, you're not on the list bud.");
                }
            } else {
                Util.simpleEmbed(message.getChannel(),"Invalid syntax: " + getSyntax());
            }
        } else {
            Util.simpleEmbed(message.getChannel(),"Invalid syntax: " + getSyntax());
        }
    }

    private static String toHex(Color color){
        String hexColor = Integer.toHexString(color.getRGB() & 0xffffff);
        if (hexColor.length() < 6) {
            hexColor = "000000".substring(0, 6 - hexColor.length()) + hexColor;
        }
        return "#" + hexColor.toUpperCase();
    }
}
