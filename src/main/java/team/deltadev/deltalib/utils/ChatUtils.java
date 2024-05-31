package team.deltadev.deltalib.utils;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class ChatUtils {

    /**
     * Translates color codes in a string using the '&' character.
     *
     * @param message The message with color codes.
     * @return The message with translated color codes.
     */
    public static String translateColorCodes(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    /**
     * Sends a formatted message to a player.
     *
     * @param player  The player to send the message to.
     * @param message The message to send.
     */
    public static void sendFormattedMessage(Player player, String message) {
        player.sendMessage(translateColorCodes(message));
    }
}