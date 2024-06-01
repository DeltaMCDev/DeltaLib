package team.deltadev.deltalib.integrations;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import team.deltadev.deltalib.utils.ChatUtils;

import java.util.logging.Level;

public class PlaceholderAPIIntegration {

    private static boolean isPlaceholderAPIAvailable = false;

    static {
        Plugin plugin = Bukkit.getPluginManager().getPlugin("PlaceholderAPI");
        if (plugin != null && plugin.isEnabled()) {
            isPlaceholderAPIAvailable = true;
        } else {
            ChatUtils.send(Level.INFO, "PlaceholderAPI not found, continuing without it.");
        }
    }

    public static String parsePlaceholders(Player player, String message) {
        if (isPlaceholderAPIAvailable) {
            return PlaceholderAPI.setPlaceholders(player, message);
        }
        return message;
    }
}
