package team.deltadev.deltalib.builders;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import team.deltadev.deltalib.utils.ItemUtils;

import java.util.*;
import java.util.function.Consumer;

public class GUIBuilder implements Listener {

    private final String title;
    private final InventoryType inventoryType;
    private int size;
    private final Map<Integer, Consumer<InventoryClickEvent>> actions;
    private final Map<Integer, ItemStack> items;
    private Consumer<InventoryCloseEvent> closeAction;
    private final List<List<ItemStack>> pages;
    private int currentPage;
    private ItemStack nextPageItem;
    private ItemStack previousPageItem;
    private Consumer<InventoryClickEvent> nextPageAction;
    private Consumer<InventoryClickEvent> previousPageAction;

    public GUIBuilder(String title, int size) {
        this.title = ChatColor.translateAlternateColorCodes('&', title);
        this.size = size;
        this.inventoryType = null;
        this.actions = new HashMap<>();
        this.items = new HashMap<>();
        this.pages = new ArrayList<>();
        this.currentPage = 0;
    }

    public GUIBuilder(String title, InventoryType inventoryType) {
        this.title = ChatColor.translateAlternateColorCodes('&', title);
        this.inventoryType = inventoryType;
        this.size = inventoryType.getDefaultSize();
        this.actions = new HashMap<>();
        this.items = new HashMap<>();
        this.pages = new ArrayList<>();
        this.currentPage = 0;
    }

    /**
     * Sets an item in the GUI at a specific slot.
     *
     * @param slot    The slot to place the item.
     * @param item    The item to place.
     * @param action  The action to perform when the item is clicked.
     * @return This GUIBuilder instance.
     */
    public GUIBuilder setItem(int slot, ItemStack item, Consumer<InventoryClickEvent> action) {
        items.put(slot, item);
        actions.put(slot, action);
        return this;
    }

    /**
     * Sets an item in the GUI at a specific slot without an action.
     *
     * @param slot The slot to place the item.
     * @param item The item to place.
     * @return This GUIBuilder instance.
     */
    public GUIBuilder setItem(int slot, ItemStack item) {
        items.put(slot, item);
        return this;
    }

    /**
     * Sets the action to perform when the inventory is closed.
     *
     * @param action The close action.
     * @return This GUIBuilder instance.
     */
    public GUIBuilder onClose(Consumer<InventoryCloseEvent> action) {
        this.closeAction = action;
        return this;
    }

    /**
     * Opens the GUI for a player.
     *
     * @param player The player to open the GUI for.
     */
    public void open(Player player) {
        Inventory inventory = (inventoryType == null) ? Bukkit.createInventory(null, size, title) :
                Bukkit.createInventory(null, inventoryType, title);
        if (!pages.isEmpty()) {
            displayPage(currentPage, inventory);
        } else {
            items.forEach(inventory::setItem);
        }
        player.openInventory(inventory);
    }

    /**
     * Handles inventory click events.
     *
     * @param event The inventory click event.
     */
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!event.getView().getTitle().equals(title)) return;
        event.setCancelled(true);
        Consumer<InventoryClickEvent> action = actions.get(event.getSlot());
        if (action != null) {
            action.accept(event);
        }
        if (pages.size() > 1) {
            handlePageChange(event);
        }
    }

    /**
     * Handles inventory close events.
     *
     * @param event The inventory close event.
     */
    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (!event.getView().getTitle().equals(title)) return;
        if (closeAction != null) {
            closeAction.accept(event);
        }
    }

    /**
     * Registers the GUI builder as an event listener.
     *
     * @param plugin The plugin instance.
     */
    public void register(JavaPlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    /**
     * Creates a simple item stack with a name and lore using ItemUtils.
     *
     * @param material The material of the item.
     * @param name     The name of the item.
     * @param lore     The lore of the item.
     * @return The created item stack.
     */
    public static ItemStack createItem(Material material, String name, String... lore) {
        return ItemUtils.createItem(material, ChatColor.translateAlternateColorCodes('&', name), Arrays.asList(lore));
    }

    /**
     * Adjusts the size of the inventory.
     *
     * @param size The new size.
     * @return This GUIBuilder instance.
     */
    public GUIBuilder setSize(int size) {
        if (inventoryType == null) {
            this.size = size;
        }
        return this;
    }

    /**
     * Paginates the GUI items.
     *
     * @param items     The items to paginate.
     * @param pageSize  The number of items per page.
     * @param nextItem  The item for the next page button.
     * @param prevItem  The item for the previous page button.
     * @param nextAction The action for the next page button.
     * @param prevAction The action for the previous page button.
     * @return This GUIBuilder instance.
     */
    public GUIBuilder paginate(List<ItemStack> items, int pageSize, ItemStack nextItem, ItemStack prevItem,
                               Consumer<InventoryClickEvent> nextAction, Consumer<InventoryClickEvent> prevAction) {
        for (int i = 0; i < items.size(); i += pageSize) {
            pages.add(items.subList(i, Math.min(i + pageSize, items.size())));
        }
        this.nextPageItem = nextItem;
        this.previousPageItem = prevItem;
        this.nextPageAction = nextAction;
        this.previousPageAction = prevAction;
        return this;
    }

    /**
     * Handles the logic for changing pages in a paginated GUI.
     * This method is called when an item in the GUI is clicked and determines whether to move to the next or previous page.
     *
     * @param event The InventoryClickEvent triggered when an item in the inventory is clicked.
     */
    private void handlePageChange(InventoryClickEvent event) {
        if (event.getCurrentItem() != null) {
            if (event.getCurrentItem().equals(nextPageItem) && currentPage < pages.size() - 1) {
                currentPage++;
                displayPage(currentPage, event.getInventory());
                if (nextPageAction != null) nextPageAction.accept(event);
            } else if (event.getCurrentItem().equals(previousPageItem) && currentPage > 0) {
                currentPage--;
                displayPage(currentPage, event.getInventory());
                if (previousPageAction != null) previousPageAction.accept(event);
            }
        }
    }

    /**
     * Displays a specific page of items in the inventory.
     * This method clears the current inventory and sets the items for the specified page.
     *
     * @param page      The page number to display.
     * @param inventory The Inventory object where the items will be displayed.
     */
    private void displayPage(int page, Inventory inventory) {
        inventory.clear();
        List<ItemStack> pageItems = pages.get(page);
        for (int i = 0; i < pageItems.size(); i++) {
            inventory.setItem(i, pageItems.get(i));
        }
        if (page < pages.size() - 1) {
            inventory.setItem(inventory.getSize() - 1, nextPageItem);
        }
        if (page > 0) {
            inventory.setItem(inventory.getSize() - 2, previousPageItem);
        }
    }

}
