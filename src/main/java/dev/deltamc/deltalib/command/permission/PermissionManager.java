package dev.deltamc.deltalib.command.permission;

import org.bukkit.command.CommandSender;

public interface PermissionManager {
    boolean hasPermission(CommandSender sender, String permission);
}