package winlyps.heartlose;

import org.bukkit.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.attribute.Attribute;
import org.bukkit.plugin.java.JavaPlugin;

public final class HeartLose extends JavaPlugin implements CommandExecutor, Listener {

    private static final int MIN_MAX_HEARTS = 1;
    private static final int MAX_MAX_HEARTS = 20; // Ensure comment matches this value
    private static final String COMMAND_NAME = "life";
    private static final String RED_DYE_DISPLAY_NAME = ChatColor.RED + "Life";
    private static final String NOT_A_PLAYER_MSG = "This command can only be used by players.";
    private static final String RED_DYE_ADDED_MSG = "Red dye 'Life' added to your inventory.";

    @Override
    public void onEnable() {
        getCommand(COMMAND_NAME).setExecutor(this);
        getServer().getPluginManager().registerEvents(this, this);
    }

    // onDisable remains unchanged

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        adjustPlayerMaxHealth(event.getEntity(), -2);
    }

    @EventHandler
    public void onPlayerInteractForLifeRedDye(PlayerInteractEvent event) {
        ItemStack item = event.getItem();
        if (item != null && item.getType() == Material.RED_DYE && hasDisplayName(item, RED_DYE_DISPLAY_NAME)) {
            adjustPlayerMaxHealth(event.getPlayer(), 2);
            item.setAmount(item.getAmount() > 1 ? item.getAmount() - 1 : 0);
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            giveLifeRedDye((Player) sender);
            return true;
        }
        sender.sendMessage(NOT_A_PLAYER_MSG);
        return false;
    }

    private void adjustPlayerMaxHealth(Player player, int change) {
        double currentValue = player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
        double newBaseHealth = Math.max(MIN_MAX_HEARTS, Math.min(currentValue + change, MAX_MAX_HEARTS));
        player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(newBaseHealth);
    }

    private void giveLifeRedDye(Player player) {
        player.getInventory().addItem(createLifeRedDye());
        player.sendMessage(RED_DYE_ADDED_MSG);
    }

    private ItemStack createLifeRedDye() {
        ItemStack redDye = new ItemStack(Material.RED_DYE);
        ItemMeta meta = redDye.getItemMeta();
        meta.setDisplayName(RED_DYE_DISPLAY_NAME);
        redDye.setItemMeta(meta);
        return redDye;
    }

    private boolean hasDisplayName(ItemStack item, String displayName) {
        return item.hasItemMeta() && item.getItemMeta().getDisplayName().equals(displayName);
    }
}







