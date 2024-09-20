package dev.deltamc.deltalib.builders;

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
import dev.deltamc.deltalib.utils.item.ItemUtils;

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
    private static final Map<Player, GUIBuilder> activeGUIs = new HashMap<>();

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

    public GUIBuilder setItem(int slot, ItemStack item, Consumer<InventoryClickEvent> action) {
        items.put(slot, item);
        actions.put(slot, action);
        return this;
    }

    public GUIBuilder setItem(int slot, ItemStack item) {
        items.put(slot, item);
        return this;
    }

    public GUIBuilder onClose(Consumer<InventoryCloseEvent> action) {
        this.closeAction = action;
        return this;
    }

    public void open(Player player) {
        Inventory inventory = createInventory();
        player.openInventory(inventory);
        activeGUIs.put(player, this);
    }

    private Inventory createInventory() {
        Inventory inventory = (inventoryType == null) ? Bukkit.createInventory(null, size, title) :
                Bukkit.createInventory(null, inventoryType, title);
        if (!pages.isEmpty()) {
            displayPage(currentPage, inventory);
        } else {
            items.forEach(inventory::setItem);
        }
        return inventory;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;

        Player player = (Player) event.getWhoClicked();
        GUIBuilder guiBuilder = activeGUIs.get(player);
        if (guiBuilder == null || !event.getView().getTitle().equals(guiBuilder.title)) return;

        event.setCancelled(true);
        Consumer<InventoryClickEvent> action = guiBuilder.actions.get(event.getSlot());
        if (action != null) {
            action.accept(event);
        }
        if (guiBuilder.pages.size() > 1) {
            guiBuilder.handlePageChange(event);
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (!(event.getPlayer() instanceof Player)) return;

        Player player = (Player) event.getPlayer();
        GUIBuilder guiBuilder = activeGUIs.get(player);
        if (guiBuilder == null || !event.getView().getTitle().equals(guiBuilder.title)) return;

        if (guiBuilder.closeAction != null) {
            guiBuilder.closeAction.accept(event);
        }

        activeGUIs.remove(player);
    }

    public void register(JavaPlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    public static ItemStack createItem(Material material, String name, String... lore) {
        return ItemUtils.createItem(material, ChatColor.translateAlternateColorCodes('&', name), Arrays.asList(lore));
    }

    public GUIBuilder setSize(int size) {
        if (inventoryType == null) {
            this.size = size;
        }
        return this;
    }

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