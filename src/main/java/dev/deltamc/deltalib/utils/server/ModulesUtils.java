package dev.deltamc.deltalib.utils.server;

import dev.dejvokep.boostedyaml.block.implementation.Section;
import dev.dejvokep.boostedyaml.YamlDocument;
import dev.deltamc.deltalib.utils.chat.ChatUtils;
import dev.deltamc.deltalib.utils.config.ConfigUtils;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.RegisteredListener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import java.util.logging.Level;

public class ModulesUtils {
    private final JavaPlugin plugin;
    private final Map<String, Supplier<Object>> modules;
    private YamlDocument modulesConfig;

    public ModulesUtils(JavaPlugin plugin) {
        this.plugin = plugin;
        this.modules = new HashMap<>();
    }

    public void registerModules(String configFileName) {
        modulesConfig = ConfigUtils.createConfig(plugin, configFileName);
        if (modulesConfig != null) {
            Section modulesSection = modulesConfig.getSection("modules");
            if (modulesSection != null) {
                for (String moduleName : modulesSection.getRoutesAsStrings(false)) {
                    boolean isEnabled = modulesSection.getBoolean(moduleName, false);
                    if (isEnabled) {
                        Supplier<Object> moduleSupplier = modules.get(moduleName);
                        if (moduleSupplier != null) {
                            Object moduleInstance = moduleSupplier.get();
                            if (moduleInstance instanceof Listener) {
                                Bukkit.getPluginManager().registerEvents((Listener) moduleInstance, plugin);
                                ChatUtils.send(Level.INFO, "Module " + moduleName + " enabled!");
                            } else {
                                ChatUtils.send(Level.WARNING, "Module " + moduleName + " could not be enabled as it is not a valid listener.");
                            }
                        }
                    } else {
                        modules.remove(moduleName);
                        ChatUtils.send(Level.INFO, "Module " + moduleName + " is disabled in the configuration.");
                    }
                }
            } else {
                ChatUtils.send(Level.WARNING, "No 'modules' section found in the configuration.");
            }
        } else {
            ChatUtils.send(Level.SEVERE, "Failed to load modules configuration.");
        }
    }

    public void unregisterModules() {
        for (RegisteredListener listener : HandlerList.getRegisteredListeners(plugin)) {
            HandlerList.unregisterAll(listener.getListener());
        }
    }

    public void addModule(String moduleName, Supplier<Object> moduleSupplier) {
        modules.put(moduleName, moduleSupplier);
    }
}
