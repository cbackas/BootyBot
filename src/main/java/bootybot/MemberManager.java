package bootybot;

import bootybot.util.Util;
import com.google.gson.reflect.TypeToken;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class MemberManager extends ListenerAdapter {

    private BootyBot bot;
    private List<IPAAMember> members;
    private File memberFile = new File(Util.botPath, "members.json");


    public MemberManager(BootyBot bot) {
        this.bot = bot;
        readMembers();
        bot.getClient().addEventListener(this);
    }

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        IPAAMember joinedMember = getMember(event.getUser().getId());
        if (joinedMember != null) {

            var guildMember = event.getMember();

            event.getGuild().getController().modifyMemberRoles(guildMember, bot.getClient().getRoleById(joinedMember.roleID), bot.getClient().getRoleById(joinedMember.human));

        }
    }

    public IPAAMember getMember(String userID) {
        return members.stream().filter(member -> member.getUserID().equalsIgnoreCase(userID)).findAny().orElse(null);
    }

    private void readMembers() {
        System.out.println("Reading members.");
        if (memberFile.exists()) {
            try {
                String readJson = FileUtils.readFileToString(memberFile, Charset.defaultCharset());
                members = Util.GSON.fromJson(readJson, new TypeToken<List<IPAAMember>>() {
                }.getType());
            } catch (IOException e) {
                e.printStackTrace();
                writeDefaults();
            }
        } else {
            writeDefaults();
        }
    }

    private void writeMembers() {
        System.out.println("Writing members.");
        try {
            FileUtils.writeStringToFile(memberFile, Util.GSON.toJson(members), Charset.defaultCharset(), false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeDefaults() {
        System.out.println("Writing default members.");
        members = new ArrayList<>();
        members.add(new IPAAMember("Zac", "109113911038464000", "193972344312692736", "73416411443113984"));
        members.add(new IPAAMember("Troy", "109114673709760512", "193972344312692736", "109109946565537792"));
        members.add(new IPAAMember("Jeremy", "109114986596454400", "193972344312692736", "109110807308029952"));
        members.add(new IPAAMember("Josh", "109114531648647168", "193972344312692736", "73463573900173312"));
        members.add(new IPAAMember("Gibs", "109115360581521408", "193972344312692736", "109109787517571072"));
        members.add(new IPAAMember("Ryan", "109112910340435968", "193972344312692736", "109109783952371712"));
        members.add(new IPAAMember("Tates", "184730166474440705", "193972344312692736", "110508617974697984"));

        writeMembers();
    }

    public class IPAAMember {

        private String userID;
        private String roleID;
        private String human;
        private String nick;

        public IPAAMember(String nick, String roleID, String human, String userID) {
            this.userID = userID;
            this.roleID = roleID;
            this.human = human;
            this.nick = nick;
        }

        public String getUserID() {
            return userID;
        }

        public String getRoleID() {
            return roleID;
        }

        public String getHuman() {
            return human;
        }

        public String getNick() {
            return nick;
        }

        public void setUserID(String userID) {
            this.userID = userID;
        }

        public void setRoleID(String roleID) {
            this.roleID = roleID;
        }

        public void setHuman(String human) {
            this.human = human;
        }

        public void setNick(String nick) {
            this.nick = nick;
        }

    }
}

