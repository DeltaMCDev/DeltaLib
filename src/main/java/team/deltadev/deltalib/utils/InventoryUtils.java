package team.deltadev.deltalib.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Base64;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 * Utility class for handling inventory operations, including serialization
 * and deserialization of inventory data to and from Base64 strings.
 */
public class InventoryUtils {

    /**
     * Serializes an {@link Inventory} to a Base64 encoded string.
     *
     * @param inventory The inventory to be serialized.
     * @return A Base64 encoded string representing the serialized inventory.
     * @throws IllegalArgumentException If the inventory is null.
     * @throws IOException If an I/O error occurs during serialization.
     */
    public static String saveInventoryToBase64(Inventory inventory) throws IllegalArgumentException, IOException {
        if (inventory == null) {
            throw new IllegalArgumentException("Inventory cannot be null");
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(inventory.getContents());
        }
        return Base64.getEncoder().encodeToString(baos.toByteArray());
    }

    /**
     * Deserializes an {@link Inventory} from a Base64 encoded string.
     *
     * @param base64 The Base64 encoded string representing the serialized inventory.
     * @param size The size of the inventory.
     * @return The deserialized inventory.
     * @throws IllegalArgumentException If the Base64 string is invalid or if the inventory size is invalid.
     * @throws IOException If an I/O error occurs during deserialization.
     * @throws ClassNotFoundException If the class of a serialized object cannot be found.
     */
    public static Inventory loadInventoryFromBase64(String base64, int size) throws IllegalArgumentException, IOException, ClassNotFoundException {
        if (base64 == null || base64.isEmpty()) {
            throw new IllegalArgumentException("Base64 string cannot be null or empty");
        }
        if (size <= 0) {
            throw new IllegalArgumentException("Invalid inventory size");
        }
        byte[] data = Base64.getDecoder().decode(base64);
        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        ItemStack[] items;
        try (ObjectInputStream ois = new ObjectInputStream(bais)) {
            items = (ItemStack[]) ois.readObject();
        }
        Inventory inventory = org.bukkit.Bukkit.createInventory(null, size);
        inventory.setContents(items);
        return inventory;
    }
}
