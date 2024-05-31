package team.deltadev.deltalib.command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.SimplePluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import team.deltadev.deltalib.utils.ConfigUtils;
import dev.dejvokep.boostedyaml.YamlDocument;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Abstract base class for custom commands, providing registration functionality.
 */
public abstract class CommandHandler implements CommandExecutor, TabCompleter {

    private String name;
    private String description;
    private List<String> aliases;
    private String permission;
    private YamlDocument config;

    private static CommandMap commandMap;

    /**
     * Constructor for CommandHandler.
     *
     * @param name   The name of the command.
     * @param plugin The JavaPlugin instance that owns the command.
     */
    public CommandHandler(String name, JavaPlugin plugin) {
        this(name, plugin, null);
    }

    /**
     * Constructor for CommandHandler with a specific config file.
     *
     * @param name   The name of the command.
     * @param plugin The JavaPlugin instance that owns the command.
     * @param config The YamlDocument instance representing the config file.
     */
    public CommandHandler(String name, JavaPlugin plugin, YamlDocument config) {
        this.name = name;
        this.config = config;

        try {
            if (commandMap == null) {
                Field field = SimplePluginManager.class.getDeclaredField("commandMap");
                field.setAccessible(true);
                commandMap = (CommandMap) field.get(Bukkit.getPluginManager());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (commandMap == null) {
            plugin.getLogger().warning("Failed to initialize the command register for " + plugin.getName());
        }
    }

    /**
     * Register the command in the commandMap.
     */
    public void register() {
        PluginCommand command = getCommand(name);
        String permissionMessage = ConfigUtils.getString(config, "SERVER.NO-PERMISSION");
        command.setPermissionMessage(ChatColor.translateAlternateColorCodes('&', permissionMessage != null ? permissionMessage : "You don't have permission to use this command."));
        if (permission != null) {
            command.setPermission(permission.toLowerCase());
        }
        command.setDescription(description != null ? description : "This is the default description!");
        command.setAliases(aliases);
        command.setExecutor(this);
        command.setTabCompleter(this);
        if (!commandMap.register(name, command)) {
            command.unregister(commandMap);
            commandMap.register(name, command);
        }
    }

    /**
     * Get a PluginCommand instance for the command.
     *
     * @param name The name of the command.
     * @return PluginCommand instance.
     */
    private PluginCommand getCommand(String name) {
        PluginCommand command = null;

        try {
            Constructor<PluginCommand> constructor = PluginCommand.class.getDeclaredConstructor(String.class, Plugin.class);
            constructor.setAccessible(true);

            command = constructor.newInstance(name, JavaPlugin.getProvidingPlugin(getClass()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return command;
    }

    /**
     * Abstract method to handle command execution.
     *
     * @param sender  Command sender.
     * @param command The command.
     * @param label   The command label.
     * @param args    Command arguments.
     * @return True if the command was handled successfully.
     */
    @Override
    public abstract boolean onCommand(CommandSender sender, Command command, String label, String[] args);

    /**
     * Method to handle tab completion.
     *
     * @param sender Command sender.
     * @param command The command.
     * @param alias The command alias.
     * @param args Command arguments.
     * @return List of tab completions.
     */
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (getAliases() != null && !getAliases().isEmpty()) {
            List<String> completions = new ArrayList<>();
            for (String cmdAlias : getAliases()) {
                if (cmdAlias.startsWith(args[0])) {
                    completions.add(cmdAlias);
                }
            }
            return completions;
        }
        return null;
    }

    /**
     * Setter for the config file.
     *
     * @param config The YamlDocument instance representing the config file.
     */
    public void setConfig(YamlDocument config) {
        this.config = config;
    }

    /**
     * Getter for the command name.
     *
     * @return The command name.
     */
    public String getName() {
        return name;
    }

    /**
     * Getter for the command description.
     *
     * @return The command description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Setter for the command description.
     *
     * @param description The new command description.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Getter for the command aliases.
     *
     * @return List of command aliases.
     */
    public List<String> getAliases() {
        return aliases;
    }

    /**
     * Setter for the command aliases.
     *
     * @param aliases The new list of command aliases.
     */
    public void setAliases(List<String> aliases) {
        this.aliases = aliases;
    }
    /**
     * Getter for the command permission.
     *
     * @return The command permission.
     */
    public String getPermission() {
        return permission;
    }

    /**
     * Setter for the command permission.
     *
     * @param permission The new command permission.
     */
    public void setPermission(String permission) {
        this.permission = permission;
    }
}
