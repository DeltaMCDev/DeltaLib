package dev.deltamc.deltalib.command.permission;

import org.bukkit.command.CommandSender;

public class BukkitPermissionManager implements PermissionManager {
    @Override
    public boolean hasPermission(CommandSender sender, String permission) {
        return sender.hasPermission(permission);
    }
}