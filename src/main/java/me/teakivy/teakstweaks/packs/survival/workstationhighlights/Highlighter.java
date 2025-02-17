package me.teakivy.teakstweaks.packs.survival.workstationhighlights;

import me.teakivy.teakstweaks.packs.BasePack;
import me.teakivy.teakstweaks.packs.PackType;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Highlighter extends BasePack {

    public Highlighter() {
        super("Workstation Highlights", "workstation-highlights", PackType.SURVIVAL, Material.SMITHING_TABLE, "Allows you to find the workstation of any villager.");
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (event.getEntity().getScoreboardTags().contains("glowing")) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onDeath(EntityDeathEvent event) {
        if (event.getEntity().getScoreboardTags().contains("glowing")) {
            event.getDrops().clear();
        }
    }

    public static void glowingBlock(Location loc, int glowLength) {
        LivingEntity entity = (LivingEntity) loc.getWorld().spawnEntity(loc, EntityType.SHULKER);
        entity.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, glowLength, 0, true, false));
        entity.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, glowLength, 0, true, false));
        entity.setAI(false);
        entity.setInvulnerable(true);
        entity.addScoreboardTag("glowing");


        Bukkit.getScheduler().scheduleSyncDelayedTask(main, () -> {
            entity.remove();
        }, glowLength);
    }

}
