package me.teakivy.teakstweaks.packs.items.armoredelytra;

import me.teakivy.teakstweaks.packs.BasePack;
import me.teakivy.teakstweaks.packs.PackType;
import me.teakivy.teakstweaks.utils.Base64Serializer;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

public class ArmoredElytras extends BasePack {

    public ArmoredElytras() {
        super("Armored Elytra", "armored-elytra", PackType.ITEMS, Material.ELYTRA, "Drop an elytra and a chestplate above an anvil to merge them into one item. Drop the item above a grindstone to separate them again.", "Enchantments are kept safely between merging and separating. Burning netherite armored elytra will separate the items.");
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        if (!hasPermission(event.getPlayer())) return;

        ItemStack item = event.getItemDrop().getItemStack();
        Item itemDrop = event.getItemDrop();
        Player player = event.getPlayer();

        if (!isChestplate(item.getType())) return;

        new BukkitRunnable() {
            @Override
            public void run() {
                Material b = getBlockBelow(itemDrop.getLocation());
                if (b != Material.ANVIL && b != Material.CHIPPED_ANVIL && b != Material.DAMAGED_ANVIL) return;

                for (Entity entity : itemDrop.getNearbyEntities(1, 1, 1)) {
                    if (entity.getType() != EntityType.DROPPED_ITEM) continue;

                    Item item = (Item) entity;
                    if (item.getItemStack().getType() != Material.ELYTRA) continue;
                    if (itemDrop.isDead() || item.isDead()) continue;

                    item.remove();
                    itemDrop.remove();
                    item.getWorld().spawnParticle(Particle.FLAME, item.getLocation(), 100, 0, 0, 0, .5);

                    player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_USE, 1, 1);

                    ItemStack newElytra = createArmoredElytra(item.getItemStack(), itemDrop.getItemStack());

                    item.getLocation().getWorld().dropItem(item.getLocation(), newElytra).setVelocity(new Vector(0, 0, 0));

                }

                if (itemDrop.isDead()) this.cancel();
            }
        }.runTaskTimer(main, 0, 20L);
    }

    private Material getBlockBelow(Location location) {
        return location.add(0, -1, 0).getBlock().getType();
    }

    @EventHandler
    public void onElytraDrop(PlayerDropItemEvent event) {
        ItemStack itemStack = event.getItemDrop().getItemStack();
        if (!itemStack.hasItemMeta()) return;
        if (!itemStack.getItemMeta().getPersistentDataContainer().has(new NamespacedKey(main, "armored_elytra"), PersistentDataType.STRING)) return;
        new BukkitRunnable() {
            @Override
            public void run() {
                if (event.getItemDrop().getLocation().add(0, -1, 0).getBlock().getType().equals(Material.GRINDSTONE)) {
                    Item item = event.getItemDrop();
                    item.remove();
                    item.getWorld().spawnParticle(Particle.FLAME, item.getLocation(), 100, 0, 0, 0, .5);
                    event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.BLOCK_GRINDSTONE_USE, 1, 1);

                    try {
                        item.getWorld().dropItem(item.getLocation(), getB64ChestplateFromArmoredElytra(itemStack)).setVelocity(new Vector(0, 0, 0));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    try {
                        item.getWorld().dropItem(item.getLocation(), getB64ElytraFromArmoredElytra(itemStack)).setVelocity(new Vector(0, 0, 0));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    item.remove();
                }
                if (event.getItemDrop().isDead()) this.cancel();
            }
        }.runTaskTimer(main, 0, 20L);
    }

    @EventHandler
    public void onBurn(EntityDamageEvent event) throws IOException {
        if (!(event.getEntity() instanceof Item)) return;

        Item item = (Item) event.getEntity();
        ItemStack itemStack = item.getItemStack();

        if (itemStack.getType() != Material.ELYTRA) return;
        if (!itemStack.hasItemMeta()) return;

        if (itemStack.getItemMeta().getPersistentDataContainer().has(new NamespacedKey(main, "armored_elytra"), PersistentDataType.STRING)) {
            item.getWorld().dropItem(item.getLocation(), getB64ChestplateFromArmoredElytra(itemStack)).setVelocity(new Vector(0, 0, 0));
            item.getWorld().dropItem(item.getLocation(), getB64ElytraFromArmoredElytra(itemStack)).setVelocity(new Vector(0, 0, 0));
            item.remove();
        }
    }

    public boolean isChestplate(Material material) {
        return material.toString().toLowerCase().contains("chestplate");
    }

    private ItemStack createArmoredElytra(ItemStack elytra, ItemStack chestplate) {
        ItemStack item = new ItemStack(Material.ELYTRA);
        ItemMeta meta = item.getItemMeta();

        HashMap<Enchantment, Integer> enchantmentStorage = new HashMap<>();

        String name = ChatColor.GOLD + "Armored Elytra";
        if (chestplate.hasItemMeta()) {
            if (chestplate.getItemMeta().hasDisplayName()) {
                name = chestplate.getItemMeta().getDisplayName();
            }
        }
        if (elytra.hasItemMeta()) {
            if (elytra.getItemMeta().hasDisplayName()) {
                name = elytra.getItemMeta().getDisplayName();
            }
        }
        meta.setDisplayName(name);

        NamespacedKey key = new NamespacedKey(main, "armored_elytra");
        meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, "true");

        NamespacedKey chestplate_storage_key = new NamespacedKey(main, "chestplate_storage");
        meta.getPersistentDataContainer().set(chestplate_storage_key, PersistentDataType.STRING, serializeItem(chestplate));

        NamespacedKey elytra_storage_key = new NamespacedKey(main, "elytra_storage");
        meta.getPersistentDataContainer().set(elytra_storage_key, PersistentDataType.STRING, serializeItem(elytra));


        chestplate.getEnchantments().forEach((enchantment, integer) -> {
            if (!enchantmentStorage.containsKey(enchantment)) {
                enchantmentStorage.put(enchantment, integer);
            } else {
                if (enchantmentStorage.get(enchantment) < integer) {
                    enchantmentStorage.put(enchantment, integer);
                }
            }
        });

        elytra.getEnchantments().forEach((enchantment, integer) -> {
            if (!enchantmentStorage.containsKey(enchantment)) {
                enchantmentStorage.put(enchantment, integer);
            } else {
                if (enchantmentStorage.get(enchantment) < integer) {
                    enchantmentStorage.put(enchantment, integer);
                }
            }
        });

        enchantmentStorage.forEach((enchantment, integer) -> {
            meta.addEnchant(enchantment, integer, true);
        });
        int armor = 0;
        int toughness = 0;
        double knockbackResistance = 0;

        if (chestplate.getType() == Material.LEATHER_CHESTPLATE) armor = 3;
        if (chestplate.getType() == Material.CHAINMAIL_CHESTPLATE) armor = 5;
        if (chestplate.getType() == Material.IRON_CHESTPLATE) armor = 6;
        if (chestplate.getType() == Material.GOLDEN_CHESTPLATE) armor = 5;
        if (chestplate.getType() == Material.DIAMOND_CHESTPLATE) {
            armor = 8;
            toughness = 2;
        }
        if (chestplate.getType() == Material.NETHERITE_CHESTPLATE) {
            armor = 8;
            toughness = 3;
            knockbackResistance = .1;
        }
        if (armor != 0) {
            AttributeModifier modifierArmor = new AttributeModifier(UUID.randomUUID(), "generic.armor", armor, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.CHEST);
            meta.addAttributeModifier(Attribute.GENERIC_ARMOR, modifierArmor);
        }
        if (toughness != 0) {
            AttributeModifier modifierToughness = new AttributeModifier(UUID.randomUUID(), "generic.armor_toughness", toughness, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.CHEST);
            meta.addAttributeModifier(Attribute.GENERIC_ARMOR_TOUGHNESS, modifierToughness);
        }
        if (knockbackResistance != 0) {
            AttributeModifier modifierKnockback = new AttributeModifier(UUID.randomUUID(), "generic.knockback_resistance", knockbackResistance, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.CHEST);
            meta.addAttributeModifier(Attribute.GENERIC_KNOCKBACK_RESISTANCE, modifierKnockback);
        }

        item.setItemMeta(meta);

        return item;
    }

    public static ItemStack getB64ChestplateFromArmoredElytra(ItemStack elytra) throws IOException {
        if (!elytra.hasItemMeta()) return null;
        if (!elytra.getItemMeta().getPersistentDataContainer().has(new NamespacedKey(main, "armored_elytra"), PersistentDataType.STRING)) return null;
        String chestplate = elytra.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(main, "chestplate_storage"), PersistentDataType.STRING);
        return deserializeItem(chestplate);
    }

    public static ItemStack getB64ElytraFromArmoredElytra(ItemStack elytra) throws IOException {
        if (!elytra.hasItemMeta()) return null;
        if (!elytra.getItemMeta().getPersistentDataContainer().has(new NamespacedKey(main, "armored_elytra"), PersistentDataType.STRING)) return null;
        String oldElytra = elytra.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(main, "elytra_storage"), PersistentDataType.STRING);
        return deserializeItem(oldElytra);
    }

    public String serializeItem(ItemStack item) {
        return Base64Serializer.itemStackArrayToBase64(new ItemStack[]{item});
    }

    public static ItemStack deserializeItem(String serialized) throws IOException {
        return Base64Serializer.itemStackArrayFromBase64(serialized)[0];
    }

}
