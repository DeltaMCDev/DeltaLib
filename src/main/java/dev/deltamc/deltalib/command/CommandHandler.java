package dev.deltamc.deltalib.command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.SimplePluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import dev.deltamc.deltalib.utils.chat.ChatUtils;
import dev.deltamc.deltalib.utils.config.ConfigUtils;
import dev.dejvokep.boostedyaml.YamlDocument;
import dev.deltamc.deltalib.command.permission.PermissionManager;
import dev.deltamc.deltalib.command.permission.PermissionManagerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;

public abstract class CommandHandler implements CommandExecutor, TabCompleter {

    private final String name;
    private Optional<String> description = Optional.empty();
    private Optional<List<String>> aliases = Optional.empty();
    private Optional<String> permission = Optional.empty();
    private YamlDocument config;

    private static CommandMap commandMap;
    private static final String CONFIG_FILENAME = "settings.yml";

    private final PermissionManager permissionManager;

    public CommandHandler(String name, JavaPlugin plugin) {
        this.name = name;
        this.config = ConfigUtils.createConfig(plugin, CONFIG_FILENAME);

        this.permissionManager = PermissionManagerFactory.getPermissionManager();

        try {
            if (commandMap == null) {
                Field field = SimplePluginManager.class.getDeclaredField("commandMap");
                field.setAccessible(true);
                commandMap = (CommandMap) field.get(Bukkit.getPluginManager());
            }
        } catch (Exception e) {
            ChatUtils.send(Level.SEVERE, "Failed to initialize commandMap for plugin: " + plugin.getName());
        }

        if (commandMap == null) {
            ChatUtils.send(Level.WARNING, "CommandMap is null for " + plugin.getName() + ". Command registration will fail.");
        }
    }

    public void register() {
        PluginCommand command = getCommand(name);

        String permissionMessage = ConfigUtils.getString(config, "SERVER.NO-PERMISSION");
        command.setPermissionMessage(ChatColor.translateAlternateColorCodes('&',
                Optional.ofNullable(permissionMessage).orElse("You don't have permission to use this command.")));

        permission.ifPresent(command::setPermission);
        command.setDescription(description.orElse("This is the default description!"));
        command.setAliases(aliases.orElse(new ArrayList<>()));

        command.setExecutor(this);
        command.setTabCompleter(this);

        if (commandMap != null && !commandMap.register(name, command)) {
            ChatUtils.send(Level.WARNING, "Command '" + name + "' already registered, unregistering and registering again.");
            command.unregister(commandMap);
            commandMap.register(name, command);
        }
    }

    private PluginCommand getCommand(String name) {
        try {
            Constructor<PluginCommand> constructor = PluginCommand.class.getDeclaredConstructor(String.class, Plugin.class);
            constructor.setAccessible(true);
            return constructor.newInstance(name, JavaPlugin.getProvidingPlugin(getClass()));
        } catch (Exception e) {
            ChatUtils.send(Level.SEVERE, "Failed to create PluginCommand for " + name);
        }
        return null;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (permission.isPresent() && !permissionManager.hasPermission(sender, permission.get())) {
            String permissionMessage = ConfigUtils.getString(config, "SERVER.NO-PERMISSION");
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    Optional.ofNullable(permissionMessage).orElse("You don't have permission to use this command.")));
            return true;
        }

        return handleCommand(sender, command, label, args);
    }

    public abstract boolean handleCommand(CommandSender sender, Command command, String label, String[] args);

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return aliases.map(a -> {
            List<String> completions = new ArrayList<>();
            for (String cmdAlias : a) {
                if (cmdAlias.startsWith(args[0])) {
                    completions.add(cmdAlias);
                }
            }
            return completions;
        }).orElseGet(ArrayList::new);
    }

    public String getName() {
        return name;
    }

    public Optional<String> getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = Optional.ofNullable(description);
    }

    public Optional<List<String>> getAliases() {
        return aliases;
    }

    public void setAliases(List<String> aliases) {
        this.aliases = Optional.ofNullable(aliases);
    }

    public Optional<String> getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = Optional.ofNullable(permission);
    }
}
