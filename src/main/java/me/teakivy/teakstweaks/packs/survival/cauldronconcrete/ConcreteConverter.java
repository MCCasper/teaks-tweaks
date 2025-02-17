package me.teakivy.teakstweaks.packs.survival.cauldronconcrete;

import me.teakivy.teakstweaks.packs.BasePack;
import me.teakivy.teakstweaks.packs.PackType;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.data.Levelled;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class ConcreteConverter extends BasePack {

    public ConcreteConverter() {
        super ("Cauldron Concrete", "cauldron-concrete", PackType.SURVIVAL, Material.CAULDRON, "Right click a cauldron with concrete powder to harden it.");
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.isCancelled()) return;
        if (!hasPermission(event.getPlayer())) return;
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (event.getClickedBlock() == null) return;
        if (event.getClickedBlock().getType() != Material.WATER_CAULDRON) return;

        if (event.getItem() == null) return;
        if (!event.getItem().getType().name().contains("_CONCRETE_POWDER")) return;

        Levelled levelled = (Levelled) event.getClickedBlock().getBlockData();
        if (levelled.getLevel() == 1 && !config.getBoolean("drops-water-level")) {
            event.getClickedBlock().setType(Material.BARRIER);
            Bukkit.getScheduler().runTaskLater(main, () -> event.getClickedBlock().setType(Material.CAULDRON), 1L);

        }

        event.setCancelled(true);

        if (event.getClickedBlock().getType() == Material.WATER_CAULDRON && !config.getBoolean("drops-water-level")) {
            levelled.setLevel(levelled.getLevel() - 1);
        }

        event.getClickedBlock().setBlockData(levelled);

        event.getItem().setAmount(event.getItem().getAmount() - 1);

        ItemStack newItem = new ItemStack(Material.valueOf(event.getItem().getType().name().replace("_CONCRETE_POWDER", "_CONCRETE")));

        event.getPlayer().getInventory().addItem(newItem);
    }
}
