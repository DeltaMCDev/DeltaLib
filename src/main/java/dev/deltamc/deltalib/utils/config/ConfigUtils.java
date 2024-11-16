package dev.deltamc.deltalib.utils.config;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.dejvokep.boostedyaml.block.implementation.Section;
import dev.dejvokep.boostedyaml.dvs.versioning.BasicVersioning;
import dev.dejvokep.boostedyaml.settings.dumper.DumperSettings;
import dev.dejvokep.boostedyaml.settings.general.GeneralSettings;
import dev.dejvokep.boostedyaml.settings.loader.LoaderSettings;
import dev.dejvokep.boostedyaml.settings.updater.UpdaterSettings;
import dev.deltamc.deltalib.utils.chat.ChatUtils;
import org.bukkit.plugin.java.JavaPlugin;

public class ConfigUtils {

    public static YamlDocument createConfig(JavaPlugin plugin, String fileName) {
        File dataFolder = plugin.getDataFolder();
        if (!dataFolder.exists()) {
            dataFolder.mkdirs();
        }
        File configFile = new File(dataFolder, fileName);

        if (!configFile.exists() || configFile.length() == 0) {
            try (java.io.InputStream inputStream = plugin.getResource(fileName)) {
                if (inputStream == null) {
                    ChatUtils.send(Level.SEVERE, "Resource " + fileName + " not found in plugin JAR.");
                    return null;
                }

                java.nio.file.Files.copy(inputStream, configFile.toPath(),
                        java.nio.file.StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                ChatUtils.send(Level.SEVERE, "Failed to copy resource " + fileName + " to file.");
                e.printStackTrace();
                return null;
            }
        }

        try {
            return YamlDocument.create(configFile, plugin.getResource(fileName),
                    GeneralSettings.DEFAULT,
                    LoaderSettings.builder().setAutoUpdate(true).build(),
                    DumperSettings.DEFAULT,
                    UpdaterSettings.builder().setVersioning(new BasicVersioning("FILE-VERSION")).build());
        } catch (IOException e) {
            ChatUtils.send(Level.SEVERE, "Failed to create or load config: " + configFile.getAbsolutePath());
            e.printStackTrace();
            return null;
        }
    }


    public static List<String> getStringList(YamlDocument config, String path) {
        return config.getStringList(path, Collections.emptyList());
    }

    public static String getString(YamlDocument config, String path) {
        return config.getString(path, "Invalid file path specified");
    }

    public static boolean getBoolean(YamlDocument config, String path) {
        return config.getBoolean(path, false);
    }

    public static int getInt(YamlDocument config, String path) {
        return config.getInt(path, 0);
    }

    public static double getDouble(YamlDocument config, String path) {
        return config.getDouble(path, 0D);
    }

    public static long getLong(YamlDocument config, String path) {
        return config.getLong(path, 0L);
    }

    public static void save(YamlDocument config) {
        try {
            config.save();
        } catch (IOException e) {
            System.err.println("Failed to save file " + config);
        }
    }

    public static void set(YamlDocument config, String path, Object value) {
        config.set(path, value);
        save(config);
    }

    public static Section getSection(YamlDocument config, String path) {
        return config.getSection(path);
    }
}
