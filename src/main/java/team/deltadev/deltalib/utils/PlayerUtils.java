package team.deltadev.deltalib.utils;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.dejvokep.boostedyaml.dvs.versioning.BasicVersioning;
import dev.dejvokep.boostedyaml.settings.dumper.DumperSettings;
import dev.dejvokep.boostedyaml.settings.general.GeneralSettings;
import dev.dejvokep.boostedyaml.settings.loader.LoaderSettings;
import dev.dejvokep.boostedyaml.settings.updater.UpdaterSettings;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class PlayerUtils {

    /**
     * Retrieves a player by their name.
     *
     * @param name The player's name.
     * @return The Player instance, or null if not found.
     */
    public static Player getPlayerByName(String name) {
        return Bukkit.getPlayer(name);
    }

    /**
     * Retrieves a player by their UUID.
     *
     * @param uuid The player's UUID.
     * @return The Player instance, or null if not found.
     */
    public static Player getPlayerByUUID(UUID uuid) {
        return Bukkit.getPlayer(uuid);
    }

    /**
     * Checks if a player with the given name is online.
     *
     * @param name The player's name.
     * @return True if the player is online, false otherwise.
     */
    public static boolean isOnline(String name) {
        Player player = getPlayerByName(name);
        return player != null && player.isOnline();
    }

    /**
     * Sends a message to a player.
     *
     * @param player  The player to send the message to.
     * @param message The message to send.
     */
    public static void sendMessage(Player player, String message) {
        player.sendMessage(message);
    }

    /**
     * Sends a message to a collection of players.
     *
     * @param players The players to send the message to.
     * @param message The message to send.
     */
    public static void sendMessage(Collection<Player> players, String message) {
        for (Player player : players) {
            player.sendMessage(message);
        }
    }

    /**
     * Broadcasts a message to all players on the server.
     *
     * @param message The message to broadcast.
     */
    public static void broadcastMessage(String message) {
        Bukkit.broadcastMessage(message);
    }

    /**
     * Gives an item to a player.
     *
     * @param player The player to give the item to.
     * @param item   The item to give.
     */
    public static void giveItem(Player player, ItemStack item) {
        player.getInventory().addItem(item);
    }

    /**
     * Clears a player's inventory.
     *
     * @param player The player whose inventory will be cleared.
     */
    public static void clearInventory(Player player) {
        player.getInventory().clear();
    }

    /**
     * Checks if a player has a specific item in their inventory.
     *
     * @param player The player to check.
     * @param item   The item to check for.
     * @return True if the player has the item, false otherwise.
     */
    public static boolean hasItem(Player player, ItemStack item) {
        return player.getInventory().contains(item);
    }

    /**
     * Adds a potion effect to a player.
     *
     * @param player    The player to add the effect to.
     * @param type      The type of potion effect.
     * @param duration  The duration of the effect in ticks.
     * @param amplifier The amplifier of the effect.
     */
    public static void addPotionEffect(Player player, PotionEffectType type, int duration, int amplifier) {
        player.addPotionEffect(new PotionEffect(type, duration, amplifier));
    }

    /**
     * Clears all potion effects from a player.
     *
     * @param player The player whose potion effects will be cleared.
     */
    public static void clearPotionEffects(Player player) {
        for (PotionEffect effect : player.getActivePotionEffects()) {
            player.removePotionEffect(effect.getType());
        }
    }

    /**
     * Heals a player by a specified amount.
     *
     * @param player The player to heal.
     * @param amount The amount to heal.
     */
    public static void heal(Player player, double amount) {
        player.setHealth(Math.min(player.getMaxHealth(), player.getHealth() + amount));
    }

    /**
     * Feeds a player by a specified amount.
     *
     * @param player    The player to feed.
     * @param foodLevel The amount of food to add to the player's food level.
     */
    public static void feed(Player player, int foodLevel) {
        player.setFoodLevel(Math.min(20, player.getFoodLevel() + foodLevel));
    }

    /**
     * Sets a player's saturation level.
     *
     * @param player     The player whose saturation level will be set.
     * @param saturation The saturation level to set.
     */
    public static void setSaturation(Player player, float saturation) {
        player.setSaturation(saturation);
    }

    /**
     * Teleports a player to a specified location.
     *
     * @param player   The player to teleport.
     * @param location The location to teleport the player to.
     */
    public static void teleport(Player player, Location location) {
        player.teleport(location);
    }

    /**
     * Teleports a player to a specified location in a specific world.
     *
     * @param player The player to teleport.
     * @param world  The world to teleport the player to.
     * @param x      The x-coordinate.
     * @param y      The y-coordinate.
     * @param z      The z-coordinate.
     * @param yaw    The yaw rotation.
     * @param pitch  The pitch rotation.
     */
    public static void teleport(Player player, World world, double x, double y, double z, float yaw, float pitch) {
        Location location = new Location(world, x, y, z, yaw, pitch);
        player.teleport(location);
    }

    /**
     * Sets a player's game mode.
     *
     * @param player   The player whose game mode will be set.
     * @param gameMode The game mode to set.
     */
    public static void setGameMode(Player player, GameMode gameMode) {
        player.setGameMode(gameMode);
    }

    /**
     * Retrieves a player's current game mode.
     *
     * @param player The player whose game mode will be retrieved.
     * @return The player's current game mode.
     */
    public static GameMode getGameMode(Player player) {
        return player.getGameMode();
    }

    /**
     * Adds experience points to a player.
     *
     * @param player The player to add experience to.
     * @param amount The amount of experience to add.
     */
    public static void addExperience(Player player, int amount) {
        player.giveExp(amount);
    }

    /**
     * Sets a player's experience level and progress.
     *
     * @param player      The player whose experience will be set.
     * @param level       The level to set.
     * @param expProgress The experience progress to set.
     */
    public static void setExperience(Player player, int level, float expProgress) {
        player.setLevel(level);
        player.setExp(expProgress);
    }

    /**
     * Plays a visual effect for a player.
     *
     * @param player The player to play the effect for.
     * @param effect The effect to play.
     * @param data   The data value for the effect.
     */
    public static void playEffect(Player player, Effect effect, int data) {
        player.playEffect(player.getLocation(), effect, data);
    }

    /**
     * Plays a sound for a player.
     *
     * @param player The player to play the sound for.
     * @param sound  The sound to play.
     * @param volume The volume of the sound.
     * @param pitch  The pitch of the sound.
     */
    public static void playSound(Player player, Sound sound, float volume, float pitch) {
        player.playSound(player.getLocation(), sound, volume, pitch);
    }

    /**
     * Sets a player's display name.
     *
     * @param player      The player whose display name will be set.
     * @param displayName The display name to set.
     */
    public static void setDisplayName(Player player, String displayName) {
        player.setDisplayName(displayName);
    }



}
