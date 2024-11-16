package dev.deltamc.deltalib.command.permission;

import dev.deltamc.deltalib.integrations.LuckPermsIntegration;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LuckPermsPermissionManager implements PermissionManager {

    @Override
    public boolean hasPermission(CommandSender sender, String permission) {
        if (sender instanceof Player) {
            return LuckPermsIntegration.hasPermission((Player) sender, permission);
        }
        return false;
    }
}