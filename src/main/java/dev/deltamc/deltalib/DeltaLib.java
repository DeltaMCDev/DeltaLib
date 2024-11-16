package dev.deltamc.deltalib;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.deltamc.deltalib.utils.config.ConfigUtils;
import org.bukkit.plugin.java.JavaPlugin;
import dev.deltamc.deltalib.updater.AutoUpdater;
import dev.deltamc.deltalib.analytics.bStats;
import dev.deltamc.deltalib.utils.chat.ChatUtils;

import java.util.logging.Level;

public class DeltaLib extends JavaPlugin {

    @Override
    public void onEnable() {
        String version = getDescription().getVersion();
        getLogger().info("DeltaLib v" + version + " is enabling...");

        YamlDocument config = ConfigUtils.createConfig(this, "config.yml");

        try {
            new bStats(this, 22095);
            ChatUtils.send(Level.INFO, "bStats metrics tracking enabled successfully.");
        } catch (Exception e) {
            ChatUtils.send(Level.WARNING, "Failed to initialize bStats: " + e.getMessage());
        }


        assert config != null;
        boolean autoUpdate = ConfigUtils.getBoolean(config, "AUTO-UPDATER");

        if (autoUpdate) {
            ChatUtils.send(Level.INFO, "Auto-updater is enabled. Checking for updates...");
            AutoUpdater updater = new AutoUpdater(this);
            updater.checkForUpdates();
        } else {
            ChatUtils.send(Level.INFO, "Auto-updater is disabled.");
        }
        ChatUtils.send(Level.INFO, "DeltaLib v" + version + " has been enabled successfully!");
    }

    @Override
    public void onDisable() {
        String version = getDescription().getVersion();
        ChatUtils.send(Level.INFO, "DeltaLib v" + version + " has been disabled successfully.");
    }
}
