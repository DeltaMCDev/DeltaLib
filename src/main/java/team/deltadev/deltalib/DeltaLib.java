package team.deltadev.deltalib;

import dev.dejvokep.boostedyaml.YamlDocument;
import org.bukkit.plugin.java.JavaPlugin;
import team.deltadev.deltalib.integrations.VaultIntegration;
import team.deltadev.deltalib.bstats.Metrics;
import team.deltadev.deltalib.utils.ChatUtils;
import team.deltadev.deltalib.utils.ConfigUtils;

import java.util.logging.Level;

public class DeltaLib extends JavaPlugin {
    @Override
    public void onEnable() {
        getLogger().info("DeltaLib is enabling...");
        if (!VaultIntegration.setupEconomy(this)) {
            ChatUtils.send(Level.SEVERE, "Disabling DeltaLib due to no Vault dependency found!");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        try {
            new Metrics(this, 22095);
        } catch (Throwable ignored) {
        }
        ChatUtils.send(Level.FINE, "DeltaLib has been enabled successfully!");
    }

    @Override
    public void onDisable() {
        ChatUtils.send(Level.FINE, "DeltaLib is disabling...");
        ChatUtils.send(Level.FINE, "DeltaLib has been disabled successfully!");
    }
}
