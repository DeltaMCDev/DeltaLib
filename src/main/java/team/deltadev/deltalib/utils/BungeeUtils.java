package team.deltadev.deltalib.utils;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.messaging.PluginMessageRecipient;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class BungeeUtils {
    private final Plugin plugin;
    private final List<String> supportedPlatforms = Arrays.asList("BungeeCord", "Velocity", "Folia");

    public BungeeUtils(Plugin plugin) {
        this.plugin = plugin;
    }

    public void sendServer(Player player, String serverName) {
        for (String platform : supportedPlatforms) {
            if (trySending(player, serverName, platform)) {
                break;
            }
        }
    }

    private boolean trySending(Player player, String serverName, String platform) {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(b);

        try {
            if (platform.equalsIgnoreCase("BungeeCord")) {
                out.writeUTF("Connect");
                out.writeUTF(serverName);
            } else if (platform.equalsIgnoreCase("Velocity")) {
                out.writeUTF("velocity:connect");
                out.writeUTF(serverName);
            } else if (platform.equalsIgnoreCase("Folia")) {
                out.writeUTF("connect");
                out.writeUTF(serverName);
            } else {
                throw new IllegalArgumentException("Unsupported platform: " + platform);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        if (player instanceof PluginMessageRecipient) {
            ((PluginMessageRecipient) player).sendPluginMessage(this.plugin, platform, b.toByteArray());
            return true;
        }

        return false;
    }
}
