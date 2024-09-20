package dev.deltamc.deltalib.utils.chat;

import dev.deltamc.deltalib.integrations.PlaceholderAPIIntegration;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class ChatUtils {

    private static final Logger LOGGER = Logger.getLogger("Minecraft");
    private static JavaPlugin plugin;

    public static void initialize(JavaPlugin javaPlugin) {
        plugin = javaPlugin;
    }

    public static String send(Level level, String message) {
        if (plugin != null) {
            LOGGER.log(level, "[" + plugin.getName() + "] " + message);
        } else {
            LOGGER.log(level, "[DeltaLib] " + message);
        }
        return message;
    }


    public static String colour(Player player, String message) {
        message = PlaceholderAPIIntegration.parsePlaceholders(player, message);
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static List<String> colourList(Player player, List<String> messages) {
        return messages.stream()
                .map(message -> colour(player, message))
                .collect(Collectors.toList());
    }

    public static String colour2 (String message) {
        return net.md_5.bungee.api.ChatColor.translateAlternateColorCodes('&', message);
    }

    public static List<String> colour2List(List<String> messages) {
        return messages.stream()
                .map(message -> net.md_5.bungee.api.ChatColor.translateAlternateColorCodes('&', message))
                .collect(Collectors.toList());
    }
}