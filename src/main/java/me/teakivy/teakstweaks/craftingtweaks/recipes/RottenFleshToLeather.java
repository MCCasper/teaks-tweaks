package me.teakivy.teakstweaks.craftingtweaks.recipes;

import me.teakivy.teakstweaks.craftingtweaks.AbstractRecipe;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemStack;

public class RottenFleshToLeather extends AbstractRecipe {

    public RottenFleshToLeather() {
        super("Rotten Flesh To Leather", "rotten-flesh-to-leather", Material.LEATHER, "Allows you to smelt Rotten Flesh into Leather.");
    }

    @Override
    public void registerRecipes() {
        FurnaceRecipe recipe = new FurnaceRecipe(new NamespacedKey(main, "rotten_flesh_to_leather"), new ItemStack(Material.LEATHER), Material.ROTTEN_FLESH, 10, 100);
        Bukkit.addRecipe(recipe);
    }

}
