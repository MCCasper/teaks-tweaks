package me.teakivy.teakstweaks.craftingtweaks.recipes;

import me.teakivy.teakstweaks.craftingtweaks.AbstractRecipe;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;

import java.util.ArrayList;
import java.util.List;

public class CraftableSporeBlossoms extends AbstractRecipe {

    public CraftableSporeBlossoms() {
        super("Craftable Spore Blossoms", "craftable-spore-blossoms", Material.SPORE_BLOSSOM, "Allows you to craft Spore Blossoms using Lilacs, Azalea Leaves, and Honeycombs");
    }

    @Override
    public void registerRecipes() {
        NamespacedKey key = new NamespacedKey(main, "craftable_spore_blossom");

        ShapedRecipe recipe = new ShapedRecipe(key, new ItemStack(Material.SPORE_BLOSSOM, 4));

        List<Material> leavesList = new ArrayList<Material>();
        leavesList.add(Material.FLOWERING_AZALEA_LEAVES);
        leavesList.add(Material.AZALEA_LEAVES);

        RecipeChoice leaves = new RecipeChoice.MaterialChoice(leavesList);

        recipe.shape("xsx", "sos", "xsx");
        recipe.setIngredient('x', Material.LILAC);
        recipe.setIngredient('s', leaves);
        recipe.setIngredient('o', Material.HONEYCOMB);

        Bukkit.addRecipe(recipe);
    }
}
