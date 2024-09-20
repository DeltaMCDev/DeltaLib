package dev.deltamc.deltalib.integrations;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import dev.deltamc.deltalib.utils.chat.ChatUtils;

import java.util.logging.Level;

public class VaultIntegration {

    private static Economy economy;

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

    public static void deposit(String playerName, double amount) {
        if (economy != null) {
            economy.depositPlayer(playerName, amount);
        }
    }

    public static void withdraw(String playerName, double amount) {
        if (economy != null) {
            economy.withdrawPlayer(playerName, amount);
        }
    }

    public static double getBalance(String playerName) {
        if (economy != null) {
            return economy.getBalance(playerName);
        }
        return 0.0;
    }

    public static boolean hasEnough(String playerName, double amount) {
        if (economy != null) {
            return economy.getBalance(playerName) >= amount;
        }
        return false;
    }

    public static String format(double amount) {
        if (economy != null) {
            return economy.format(amount);
        }
        return String.format("$%.2f", amount);
    }
}
