package dev.deltamc.deltalib.updater;

import dev.deltamc.deltalib.utils.chat.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.Scanner;

public class AutoUpdater {

    private final JavaPlugin plugin;
    private static final String LATEST_RELEASE_API_URL = "https://api.github.com/repos/DeltaMCDev/DeltaLib/releases/latest";
    private static final String PLUGIN_FOLDER = "plugins/";

    public AutoUpdater(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void checkForUpdates() {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                String latestVersion = getLatestVersion();
                String currentVersion = plugin.getDescription().getVersion();

                if (isNewVersionAvailable(currentVersion, latestVersion)) {
                    ChatUtils.send(Level.INFO, "New version " + latestVersion + " found! Updating...");

                    String downloadUrl = getDownloadUrl();
                    String downloadPath = PLUGIN_FOLDER + "DeltaLib-" + latestVersion + ".jar";

                    if (downloadUpdate(downloadUrl, downloadPath)) {
                        ChatUtils.send(Level.INFO, "Update downloaded successfully as " + downloadPath);

                    } else {
                        ChatUtils.send(Level.WARNING, "Failed to download the latest update.");
                    }
                } else {
                    ChatUtils.send(Level.INFO, "You are running the latest version of DeltaLib.");
                }
            } catch (Exception e) {
                ChatUtils.send(Level.SEVERE, "Failed to check for updates: " + e.getMessage());
            }
        });
    }

    private String getLatestVersion() throws Exception {
        URL url = new URL(LATEST_RELEASE_API_URL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept", "application/json");

        if (connection.getResponseCode() != 200) {
            throw new Exception("Failed to fetch latest release info. Response code: " + connection.getResponseCode());
        }

        InputStream responseStream = connection.getInputStream();
        Scanner scanner = new Scanner(responseStream).useDelimiter("\\A");
        String responseBody = scanner.hasNext() ? scanner.next() : "";

        String tag = responseBody.split("\"tag_name\":\"")[1].split("\"")[0];
        return tag;
    }

    private boolean isNewVersionAvailable(String currentVersion, String latestVersion) {
        return !currentVersion.equals(latestVersion);
    }

    private String getDownloadUrl() throws Exception {
        URL url = new URL(LATEST_RELEASE_API_URL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept", "application/json");

        if (connection.getResponseCode() != 200) {
            throw new Exception("Failed to fetch release download URL.");
        }

        InputStream responseStream = connection.getInputStream();
        Scanner scanner = new Scanner(responseStream).useDelimiter("\\A");
        String responseBody = scanner.hasNext() ? scanner.next() : "";

        return responseBody.split("\"browser_download_url\":\"")[1].split("\"")[0];
    }

    private boolean downloadUpdate(String downloadUrl, String downloadPath) {
        try {
            URL url = new URL(downloadUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            Path outputPath = Paths.get(downloadPath);

            try (InputStream in = new BufferedInputStream(connection.getInputStream());
                 FileOutputStream out = new FileOutputStream(outputPath.toFile())) {

                byte[] buffer = new byte[1024];
                int count;
                while ((count = in.read(buffer, 0, 1024)) != -1) {
                    out.write(buffer, 0, count);
                }
            }

            return Files.exists(outputPath);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
