package xdx.nikosiaphd.dropRandomiser;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;



public final class DropRandomiser extends JavaPlugin implements Listener {

    private final List<Material> allItems = new ArrayList<>();
    private final Random random = new Random();

    private boolean randomizeBlocks = true;
    private boolean randomizeDrops = true;

    @Override
    public void onEnable() {
        for (Material mat : Material.values()) {
            if (mat.isItem()) {
                allItems.add(mat);
            }
        }

        Bukkit.getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if (!randomizeDrops) return;

        Entity entity = event.getEntity();
        event.getDrops().clear();
        Material randomMat = allItems.get(random.nextInt(allItems.size()));
        event.getDrops().add(new ItemStack(randomMat, 1));
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (!randomizeBlocks) return;

        Block block = event.getBlock();
        Player player = event.getPlayer();
        event.setDropItems(false);

        Material randomMat = allItems.get(random.nextInt(allItems.size()));
        block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(randomMat, 1));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("randomizeBlockDrops")) {
            if (args.length == 1) {
                randomizeBlocks = Boolean.parseBoolean(args[0]);
                return true;
            }
        }
        if (command.getName().equalsIgnoreCase("randomizeMobDrops")) {
            if (args.length == 1) {
                randomizeDrops = Boolean.parseBoolean(args[0]);
                return true;
            }
        }
        return false;
    }



    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
