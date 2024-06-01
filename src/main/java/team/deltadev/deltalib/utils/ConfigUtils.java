package team.deltadev.deltalib.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.dejvokep.boostedyaml.block.implementation.Section;
import dev.dejvokep.boostedyaml.dvs.versioning.BasicVersioning;
import dev.dejvokep.boostedyaml.settings.dumper.DumperSettings;
import dev.dejvokep.boostedyaml.settings.general.GeneralSettings;
import dev.dejvokep.boostedyaml.settings.loader.LoaderSettings;
import dev.dejvokep.boostedyaml.settings.updater.UpdaterSettings;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Utility class for handling the creation, loading, saving, and management of configuration files using the BoostedYAML library.
 */
public class ConfigUtils {

    /**
     * Creates and loads a YamlDocument configuration file.
     *
     * @param plugin   The plugin instance associated with this configuration handler.
     * @param fileName The name of the configuration file to manage.
     * @return The loaded YamlDocument instance.
     */
    public static YamlDocument createConfig(JavaPlugin plugin, String fileName) {
        File dataFolder = plugin.getDataFolder();
        if (!dataFolder.exists()) {
            dataFolder.mkdirs();
        }

        File configFile = new File(dataFolder, fileName);

        if (!configFile.getParentFile().exists()) {
            configFile.getParentFile().mkdirs();
        }

        try {
            if (!configFile.exists()) {
                plugin.saveResource(fileName, false);
            }

            return YamlDocument.create(configFile, plugin.getResource(fileName),
                    GeneralSettings.DEFAULT,
                    LoaderSettings.builder().setAutoUpdate(true).build(),
                    DumperSettings.DEFAULT,
                    UpdaterSettings.builder().setVersioning(new BasicVersioning("FILE-VERSION")).build());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Retrieves a list of strings from the configuration file.
     *
     * @param config The YamlDocument instance.
     * @param path The YAML path to the string list.
     * @return The list of strings, or an empty list if the path is not found.
     */
    public static List<String> getStringList(YamlDocument config, String path) {
        return config.getStringList(path, Collections.emptyList());
    }

    /**
     * Retrieves a string from the configuration file.
     *
     * @param config The YamlDocument instance.
     * @param path The YAML path to the string.
     * @return The string value, or "Invalid file path specified" if the path is not found.
     */
    public static String getString(YamlDocument config, String path) {
        return config.getString(path, "Invalid file path specified");
    }

    /**
     * Retrieves a boolean from the configuration file.
     *
     * @param config The YamlDocument instance.
     * @param path The YAML path to the boolean.
     * @return The boolean value, or false if the path is not found or not a boolean.
     */
    public static boolean getBoolean(YamlDocument config, String path) {
        return config.getBoolean(path, false);
    }

    /**
     * Retrieves an integer from the configuration file.
     *
     * @param config The YamlDocument instance.
     * @param path The YAML path to the integer.
     * @return The integer value, or 0 if the path is not found or not an integer.
     */
    public static int getInt(YamlDocument config, String path) {
        return config.getInt(path, 0);
    }

    /**
     * Retrieves a double from the configuration file.
     *
     * @param config The YamlDocument instance.
     * @param path The YAML path to the double.
     * @return The double value, or 0.0 if the path is not found or not a double.
     */
    public static double getDouble(YamlDocument config, String path) {
        return config.getDouble(path, 0D);
    }

    /**
     * Retrieves a long from the configuration file.
     *
     * @param config The YamlDocument instance.
     * @param path The YAML path to the long.
     * @return The long value, or 0L if the path is not found or not a long.
     */
    public static long getLong(YamlDocument config, String path) {
        return config.getLong(path, 0L);
    }

    /**
     * Saves the current state of the configuration file to disk.
     *
     * @param config The YamlDocument instance.
     */
    public static void save(YamlDocument config) {
        try {
            config.save();
        } catch (IOException e) {
            System.err.println("Failed to save file " + config);
        }
    }

    /**
     * Sets a value in the configuration file at the specified path.
     *
     * @param config The YamlDocument instance.
     * @param path  The YAML path where the value should be set.
     * @param value The value to set.
     */
    public static void set(YamlDocument config, String path, Object value) {
        config.set(path, value);
        save(config);
    }

    /**
     * Retrieves a specific section from the YAML configuration.
     *
     * @param config The YamlDocument instance.
     * @param path The path to the specific section.
     * @return The section if found, otherwise null.
     */
    public static Section getSection(YamlDocument config, String path) {
        return config.getSection(path);
    }
}
