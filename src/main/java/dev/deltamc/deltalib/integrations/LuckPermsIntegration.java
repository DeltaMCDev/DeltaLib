package dev.deltamc.deltalib.integrations;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.user.User;
import net.luckperms.api.query.QueryOptions;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.Optional;
import java.util.logging.Level;
import dev.deltamc.deltalib.utils.chat.ChatUtils;

public class LuckPermsIntegration {

    private static LuckPerms luckPerms;

    static {
        Plugin plugin = Bukkit.getPluginManager().getPlugin("LuckPerms");
        if (plugin != null && plugin.isEnabled()) {
            luckPerms = Bukkit.getServicesManager().load(LuckPerms.class);
            ChatUtils.send(Level.INFO, "LuckPerms integration enabled.");
        } else {
            ChatUtils.send(Level.WARNING, "LuckPerms not found, continuing without it.");
        }
    }

    public static boolean isLuckPermsAvailable() {
        return luckPerms != null;
    }

    public static Optional<User> getUser(Player player) {
        return Optional.ofNullable(luckPerms.getUserManager().getUser(player.getUniqueId()));
    }

    public static boolean hasPermission(Player player, String permission) {
        return getUser(player).map(user ->
                user.getCachedData()
                        .getPermissionData(QueryOptions.defaultContextualOptions())
                        .checkPermission(permission).asBoolean()
        ).orElse(false);
    }

    public static Optional<String> getPrimaryGroup(Player player) {
        return getUser(player).map(User::getPrimaryGroup);
    }
}