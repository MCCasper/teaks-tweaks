package me.teakivy.teakstweaks.packs.mobs.anticreepergrief;

import me.teakivy.teakstweaks.packs.BasePack;
import me.teakivy.teakstweaks.packs.PackType;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

public class AntiCreeper extends BasePack {

    public AntiCreeper() {
        super("Anti Creeper Grief", "anti-creeper-grief", PackType.MOBS, Material.CREEPER_SPAWN_EGG, "Prevents creepers from blowing up, blocks and players do not take damage.");
    }

    @EventHandler
    public void onExplosion(EntityExplodeEvent event) {
        Entity entity = event.getEntity();
        if (entity.getType().equals(EntityType.CREEPER) && !config.getBoolean("do-block-damage")) {
            event.blockList().clear();
        }
    }

    @EventHandler
    public void onExplosionDamage(EntityDamageByEntityEvent event) {
        EntityDamageEvent.DamageCause damageCause = event.getCause();

        if (damageCause == EntityDamageEvent.DamageCause.ENTITY_EXPLOSION && !config.getBoolean("do-entity-damage")) {
            if (event.getDamager().getType().equals(EntityType.CREEPER)) {
                event.setCancelled(true);
            }
        }
    }

}
