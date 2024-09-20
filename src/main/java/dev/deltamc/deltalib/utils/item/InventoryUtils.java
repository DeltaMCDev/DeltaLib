package dev.deltamc.deltalib.utils.item;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.jetbrains.annotations.NotNull;

public class InventoryUtils {

    private static final HashMap<String, String> inventoryStorage = new HashMap<>();

    public static String itemStackArrayToBase64(@NotNull final ItemStack[] itemStacks) throws IllegalArgumentException, IllegalStateException {
        if (itemStacks == null) {
            throw new IllegalArgumentException("ItemStack array cannot be null.");
        }

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
             BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream)) {

            dataOutput.writeInt(itemStacks.length);

            for (ItemStack itemStack : itemStacks) {
                dataOutput.writeObject(itemStack);
            }

            byte[] bytes = outputStream.toByteArray();
            return Base64.getEncoder().encodeToString(bytes);

        } catch (IOException e) {
            throw new IllegalStateException("Unable to save items stacks.", e);
        }
    }

    public static String inventoryToBase64(@NotNull final Inventory inventory) throws IllegalArgumentException, IllegalStateException {

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
             BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream)) {

            dataOutput.writeInt(inventory.getSize());

            for (int i = 0; i < inventory.getSize(); i++) {
                dataOutput.writeObject(inventory.getItem(i));
            }

            byte[] bytes = outputStream.toByteArray();
            return Base64.getEncoder().encodeToString(bytes);

        } catch (IOException e) {
            throw new IllegalStateException("Unable to save inventory.", e);
        }
    }

    public static Inventory inventoryFromBase64(@NotNull final String data) throws IllegalArgumentException, IOException {

        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64.getDecoder().decode(data));
             BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream)) {

            Inventory inventory = Bukkit.createInventory(null, dataInput.readInt());

            for (int i = 0; i < inventory.getSize(); i++) {
                inventory.setItem(i, (ItemStack) dataInput.readObject());
            }

            return inventory;

        } catch (ClassNotFoundException e) {
            throw new IOException("Unable to decode class type.", e);
        }
    }

    public static ItemStack[] itemStackArrayFromBase64(@NotNull final String data) throws IllegalArgumentException, IOException {

        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64.getDecoder().decode(data));
             BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream)) {

            ItemStack[] items = new ItemStack[dataInput.readInt()];

            for (int i = 0; i < items.length; i++) {
                items[i] = (ItemStack) dataInput.readObject();
            }

            return items;

        } catch (ClassNotFoundException e) {
            throw new IOException("Unable to decode class type.", e);
        }
    }

}
