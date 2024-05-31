package team.deltadev.deltalib.utils;
import dev.dejvokep.boostedyaml.block.implementation.Section;
import dev.dejvokep.boostedyaml.YamlDocument;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.RegisteredListener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public class ModulesUtils {

    private final JavaPlugin plugin;
    private final Map<String, Object> modules;
    private YamlDocument modulesConfig;

    public ModulesUtils(JavaPlugin plugin) {
        this.plugin = plugin;
        this.modules = new HashMap<>();
    }

    /**
     * Register modules based on the configuration.
     * This method reads the configuration file, initializes enabled modules, and registers their listeners.
     */
    public void registerModules(String configFileName) {
        modulesConfig = ConfigUtils.createConfig(plugin, configFileName);
        if (modulesConfig != null) {
            Section modulesSection = ConfigUtils.getSection(modulesConfig, "modules");
            if (modulesSection != null) {
                for (String moduleName : modulesSection.getRoutesAsStrings(false)) {
                    boolean isEnabled = ConfigUtils.getBoolean(modulesConfig, "modules." + moduleName);
                    if (isEnabled) {
                        Object moduleInstance = modules.get(moduleName);
                        if (moduleInstance instanceof Listener) {
                            Bukkit.getPluginManager().registerEvents((Listener) moduleInstance, plugin);
                            plugin.getLogger().log(Level.INFO, "Module " + moduleName + " enabled!");
                        } else {
                            plugin.getLogger().log(Level.WARNING, "Module " + moduleName + " could not be enabled as it is not a valid listener.");
                        }
                    }
                }
            }
        }
    }

    /**
     * Unregister all registered listeners for modules.
     * This method unregisters all listeners associated with modules.
     */
    public void unregisterModules() {
        for (RegisteredListener listener : HandlerList.getRegisteredListeners(plugin)) {
            HandlerList.unregisterAll(listener.getListener());
        }
    }

    /**
     * Add a module to the map of modules.
     * This method adds a module with the specified name and class to the map.
     *
     * @param moduleName The name of the module.
     * @param moduleInstance The instance of the module.
     */
    public void addModule(String moduleName, Object moduleInstance) {
        modules.put(moduleName, moduleInstance);
    }
}