package team.deltadev.deltalib.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.jetbrains.annotations.NotNull;

/**
 * Utility class for handling inventory operations, including serialization
 * and deserialization of inventory data to and from Base64 strings.
 */
public class InventoryUtils {

    private static final HashMap<String, String> inventoryStorage = new HashMap<>();

    /**
     * Converts an array of ItemStacks to a Base64 encoded string representation.
     *
     * @param itemStacks the array of ItemStacks to convert
     * @return a Base64 encoded string representation of the ItemStack array
     * @throws IllegalArgumentException if the ItemStack array is null
     * @throws IllegalStateException    if there is an error converting the ItemStack array to a Base64 string
     */
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

    /**
     * Converts an Inventory object to a Base64 encoded string representation.
     *
     * @param inventory the Inventory object to convert
     * @return a Base64 encoded string representation of the inventory
     * @throws IllegalArgumentException if the inventory is null
     * @throws IllegalStateException    if there is an error converting the inventory to a Base64 string
     */
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

    /**
     * Converts a Base64 encoded string representation of an inventory to an Inventory object.
     *
     * @param data the Base64 encoded string representation of an inventory
     * @return an Inventory object representing the decoded inventory data
     * @throws IllegalArgumentException if the data string is null
     * @throws IOException              if there is an error decoding the inventory data
     */
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

    /**
     * Converts a Base64 encoded string representation of an array of ItemStacks to an ItemStack array.
     *
     * @param data the Base64 encoded string representation of an array of ItemStacks
     * @return an ItemStack array representing the decoded data
     * @throws IllegalArgumentException if the data string is null
     * @throws IOException              if there is an error decoding the data
     */
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
