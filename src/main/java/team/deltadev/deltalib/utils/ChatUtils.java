package team.deltadev.deltalib.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import team.deltadev.deltalib.integrations.PlaceholderAPIIntegration;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class ChatUtils {

    private static final Logger LOGGER = Logger.getLogger("Minecraft");

    /**
     * Send a formatted message to the console with a specified log level.
     *
     * @param level   The log level of the message.
     * @param message The message to send.
     * @return The original message.
     */
    public static String send(Level level, String message) {
        LOGGER.log(level, "[DeltaLib] " + message);
        return message;
    }

    /**
     * Translate color codes and parse placeholders in a message for a specific player.
     *
     * @param player  The player for whom to parse the message.
     * @param message The message to colorize and parse.
     * @return The colorized and parsed message.
     */
    public static String colour(Player player, String message) {
        message = PlaceholderAPIIntegration.parsePlaceholders(player, message);
        return ChatColor.translateAlternateColorCodes('&', message);
    }
    /**
     * Translate color codes .
     *
     * @param message The message to colorize and parse.
     * @return The colorized and parsed message.
     */
    public static String colour2 (String message) {
        return net.md_5.bungee.api.ChatColor.translateAlternateColorCodes('&', message);
    }
    /**
     * Translate color codes for each string in the list.
     *
     * @param messages The list of strings to colorize.
     * @return The list of colorized strings.
     */
    public static List<String> colour2List(List<String> messages) {
        return messages.stream()
                .map(message -> net.md_5.bungee.api.ChatColor.translateAlternateColorCodes('&', message))
                .collect(Collectors.toList());
    }
}