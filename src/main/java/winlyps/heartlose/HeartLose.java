package winlyps.heartlose;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot; // Remove if not used elsewhere
import org.bukkit.inventory.ItemStack;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.plugin.java.JavaPlugin;

public final class HeartLose extends JavaPlugin implements CommandExecutor, Listener {

    private static final int DEFAULT_MAX_HEARTS = 20;
    private static final String REDSTONE_DISPLAY_NAME = "Zycie";
    private static final String NOT_A_PLAYER_MSG = "This command can only be used by players.";
    private static final String REDSTONE_ADDED_MSG = "Redstone 'Zycie' added to your inventory.";

    @Override
    public void onEnable() {
        getCommand("zycie").setExecutor(this);
        getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic can be added here
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        // Reduce player's max health
        adjustPlayerMaxHealth(event.getEntity(), -2);
    }

    @EventHandler
    public void onPlayerInteractForLifeRedstone(PlayerInteractEvent event) {
        // Interact with 'Zycie' redstone
        interactWithLifeRedstone(event.getPlayer(), event.getItem());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            giveLifeRedstone((Player) sender);
            return true;
        }
        sender.sendMessage(NOT_A_PLAYER_MSG);
        return false;
    }

    private void adjustPlayerMaxHealth(Player player, int change) {
        double newBaseHealth = Math.max(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() + change, DEFAULT_MAX_HEARTS / 2.0);
        player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(newBaseHealth);
    }

    private void interactWithLifeRedstone(Player player, ItemStack item) {
        if (item != null && item.getType() == Material.REDSTONE && hasDisplayName(item, REDSTONE_DISPLAY_NAME)) {
            adjustPlayerMaxHealth(player, 2);
            if (item.getAmount() > 1) {
                item.setAmount(item.getAmount() - 1);
            } else {
                item.setAmount(0); // Ensure item is not left with amount 0
            }
        }
    }

    private void giveLifeRedstone(Player player) {
        player.getInventory().addItem(createLifeRedstone());
        player.sendMessage(REDSTONE_ADDED_MSG);
    }

    private ItemStack createLifeRedstone() {
        ItemStack redstone = new ItemStack(Material.REDSTONE);
        ItemMeta meta = redstone.getItemMeta();
        meta.setDisplayName(REDSTONE_DISPLAY_NAME);
        redstone.setItemMeta(meta);
        return redstone;
    }

    private boolean hasDisplayName(ItemStack item, String displayName) {
        return item.hasItemMeta() && item.getItemMeta().getDisplayName() != null && item.getItemMeta().getDisplayName().equals(displayName);
    }
}





