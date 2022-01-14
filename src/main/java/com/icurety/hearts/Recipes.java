package com.icurety.hearts;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class Recipes {

    public static void load(Hearts hearts) {
        loadHeartContainer(hearts);
    }

    public static ItemStack getHeartPieceItemStack() {
        ItemStack stack = new ItemStack(Material.FEATHER, 1);
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(ChatColor.RED + "Heart Piece");

        List<String> lore = new ArrayList<String>();
        lore.add("1 / 4");
        meta.setLore(lore);

        meta.setCustomModelData(19);
        stack.setItemMeta(meta);
        return stack;
    }

    public static ItemStack getHeartContainerStack() {
        ItemStack stack = new ItemStack(Material.APPLE, 1);
        List<String> lore = new ArrayList<String>();
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(ChatColor.RED + "Heart Container");
        lore.add("+1 heart");
        meta.setLore(lore);
        meta.setCustomModelData(20);
        stack.setItemMeta(meta);
        return stack;
    }

    private static void loadHeartContainer(Hearts hearts) {
        ItemStack stack = getHeartContainerStack();
        NamespacedKey key = new NamespacedKey(hearts, "heart_container");
        ShapedRecipe recipe = new ShapedRecipe(key, stack);
        recipe.shape("pp", "pp");

        ItemStack heartPiece = getHeartPieceItemStack();
        recipe.setIngredient('p', new RecipeChoice.ExactChoice(heartPiece));

        Bukkit.addRecipe(recipe);
    }

}
