package team.deltadev.deltalib.integrations;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import team.deltadev.deltalib.utils.ChatUtils;

import java.util.logging.Level;

public class VaultIntegration {

    private static Economy economy;

    /**
     * Initializes the economy system.
     *
     * @param plugin The plugin instance associated with this integration.
     * @return True if the economy system was initialized successfully, false otherwise.
     */
    public static boolean setupEconomy(JavaPlugin plugin) {
        if (Bukkit.getServer().getPluginManager().getPlugin("Vault") == null) {
            ChatUtils.send(Level.SEVERE, "Vault is not installed!");
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = Bukkit.getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            ChatUtils.send(Level.SEVERE,"No economy provider found!");
            return false;
        }
        economy = rsp.getProvider();
        return economy != null;
    }

    /**
     * Deposits money into a player's account.
     *
     * @param playerName The player's name.
     * @param amount     The amount to deposit.
     */
    public static void deposit(String playerName, double amount) {
        if (economy != null) {
            economy.depositPlayer(playerName, amount);
        }
    }

    /**
     * Withdraws money from a player's account.
     *
     * @param playerName The player's name.
     * @param amount     The amount to withdraw.
     */
    public static void withdraw(String playerName, double amount) {
        if (economy != null) {
            economy.withdrawPlayer(playerName, amount);
        }
    }

    /**
     * Gets the balance of a player's account.
     *
     * @param playerName The player's name.
     * @return The balance of the player's account.
     */
    public static double getBalance(String playerName) {
        if (economy != null) {
            return economy.getBalance(playerName);
        }
        return 0.0;
    }

    /**
     * Checks if the player has at least the specified amount of money.
     *
     * @param playerName The player's name.
     * @param amount     The amount to check.
     * @return True if the player has at least the specified amount, false otherwise.
     */
    public static boolean hasEnough(String playerName, double amount) {
        if (economy != null) {
            return economy.getBalance(playerName) >= amount;
        }
        return false;
    }

    /**
     * Formats an amount of money into a human-readable format.
     *
     * @param amount The amount to format.
     * @return The formatted amount.
     */
    public static String format(double amount) {
        if (economy != null) {
            return economy.format(amount);
        }
        return String.format("$%.2f", amount);
    }
}
