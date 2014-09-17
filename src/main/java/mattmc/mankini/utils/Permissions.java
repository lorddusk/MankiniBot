package mattmc.mankini.utils;

import mattmc.mankini.commands.CommandLinks;
import mattmc.mankini.commands.CommandRegular;
import mattmc.mankini.common.ModCommon;
import mattmc.mankini.libs.Strings;
import org.pircbotx.hooks.events.MessageEvent;

import java.sql.SQLException;

/**
 * Project MankiniBot
 * Created by MattMc on 7/14/14.
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 */
public class Permissions {

    static boolean owner = false;
    static boolean moderator = false;
    static boolean regular = false;


    public static boolean isOwner(String user, MessageEvent event) {
        if (user.equals("runew0lf")) {
            owner = true;
            return true;
        } else {
            owner = false;
            noPermissionOwnMessage(user, event);
        }
        return false;
    }

    public static boolean isModerator(String user, MessageEvent event) {
        if (ModCommon.moderators.contains(user.toLowerCase()) || user.equals("runew0lf")) {
            moderator = true;
            return true;
        } else {
            moderator = false;
            noPermissionModMessage(user, event);
        }
        return false;
    }

    public static boolean isRegular(String user, MessageEvent event) throws IllegalAccessException, InstantiationException, SQLException {
        if (CommandRegular.class.newInstance().isRegular(user) || ModCommon.moderators.contains(user.toLowerCase()) || user.equals("runew0lf")) {
            regular = true;
            return true;
        } else {
            regular = false;
            noPermissionRegMessage(user, event);
        }
        return false;
    }

    public static boolean isPermitted(String user, MessageEvent event) throws IllegalAccessException, SQLException, InstantiationException {
        if (CommandLinks.permitted.contains(user) || isRegular(user,event)) {
            return true;
        } else {
            if (!CommandLinks.strike1.contains(user)) {
                event.getBot().sendRaw().rawLine("PRIVMSG " + event.getChannel().getName() + " :.timeout " + event.getUser().getNick() + " 5");
                event.respond(Strings.strike1);
                CommandLinks.strike1.add(user);

            } else {
                if (CommandLinks.strike1.contains(user)) {
                    event.getBot().sendRaw().rawLine("PRIVMSG " + event.getChannel().getName() + " :.timeout " + event.getUser()
                            .getNick() + Strings.bantime);
                    event.respond(Strings.strike2 + Strings.bantimeOnMSG);
                    CommandLinks.strike1.remove(user);
                }
            }
        }
        return false;
    }

    public static void noPermissionRegMessage(String user, MessageEvent event) {
        if(!regular && !moderator && !owner) {
            MessageSending.sendMessageWithPrefix(user + " you do not have permissions to do that", user, event);
        }
    }

    public static void noPermissionModMessage(String user, MessageEvent event) {
        if(!moderator && !owner) {
            MessageSending.sendMessageWithPrefix(user + " you do not have permissions to do that", user, event);
        }
    }

    public static void noPermissionOwnMessage(String user, MessageEvent event) {
        if(!owner) {
            MessageSending.sendMessageWithPrefix(user + " you do not have permissions to do that", user, event);
        }
    }
}