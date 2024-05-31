package team.deltadev.deltalib;

import dev.dejvokep.boostedyaml.YamlDocument;
import org.bukkit.plugin.java.JavaPlugin;
import team.deltadev.deltalib.command.ExampleCommand;
import team.deltadev.deltalib.integrations.VaultIntegration;
import team.deltadev.deltalib.bstats.Metrics;
import team.deltadev.deltalib.utils.ConfigUtils;

public class DeltaLib extends JavaPlugin {
    @Override
    public void onEnable() {
        getLogger().info("DeltaLib is enabling...");
        if (!VaultIntegration.setupEconomy(this)) {
            getLogger().severe("Disabling DeltaLib due to no Vault dependency found!");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        try {
            new Metrics(this, 22095);
        } catch (Throwable ignored) {
        }
        getLogger().info("DeltaLib has been enabled successfully!");
    }

    @Override
    public void onDisable() {
        getLogger().info("DeltaLib is disabling...");
        getLogger().info("DeltaLib has been disabled successfully!");
    }
}
