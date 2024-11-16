package dev.deltamc.deltalib.command.permission;

import dev.deltamc.deltalib.integrations.LuckPermsIntegration;

public class PermissionManagerFactory {

    public static PermissionManager getPermissionManager() {
        if (LuckPermsIntegration.isLuckPermsAvailable()) {
            return new LuckPermsPermissionManager();
        }
        return new BukkitPermissionManager();
    }
}