package me.teakivy.teakstweaks.craftingtweaks.recipes;

import me.teakivy.teakstweaks.craftingtweaks.AbstractRecipe;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;

public class CraftableBundlesRabbitHide extends AbstractRecipe {

    public CraftableBundlesRabbitHide() {
        super("Craftable Bundles Rabbit Hide", "craftable-bundles-rabbit-hide", Material.BUNDLE, "Allows you to craft 1.18's Bundles in 1.17.");
    }

    @Override
    public void registerRecipes() {
        NamespacedKey key = new NamespacedKey(main, "bundle_rabbit_hide_bundles");

        ShapedRecipe recipe = new ShapedRecipe(key, new ItemStack(Material.BUNDLE));

        recipe.shape("s#s", "# #", "###");
        recipe.setIngredient('#', Material.RABBIT_HIDE);
        recipe.setIngredient('s', Material.STRING);

        Bukkit.addRecipe(recipe);
    }
}
