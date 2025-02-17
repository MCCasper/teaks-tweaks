package me.teakivy.teakstweaks.craftingtweaks.recipes;

import me.teakivy.teakstweaks.craftingtweaks.AbstractRecipe;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;

public class CoalToBlackDye extends AbstractRecipe {

     public CoalToBlackDye() {
         super("Coal To Black Dye", "coal-to-black-dye", Material.COAL, "Allows you to craft Coal into Black Dye.");
     }

    @Override
    public void registerRecipes() {
        NamespacedKey key = new NamespacedKey(main, "black_dye_coal");
        ShapelessRecipe recipe = new ShapelessRecipe(key, new ItemStack(Material.BLACK_DYE));
        recipe.addIngredient(Material.COAL);


    }
}
