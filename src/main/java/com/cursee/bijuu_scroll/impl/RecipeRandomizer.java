package com.cursee.bijuu_scroll.impl;

import com.cursee.bijuu_scroll.BijuuScroll;
import com.cursee.bijuu_scroll.config.ModConfig;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

/**
 * Deterministically remaps a recipe's vanilla output to a different registered
 * item, keyed off the recipe's registry name (or the smelting output) plus
 * {@link ModConfig#RECIPE_SEED}. Same seed always produces the same mapping,
 * regardless of mod load order or server restarts.
 */
public final class RecipeRandomizer {

  private static final long FNV_OFFSET_BASIS = -3750763034362895579L;
  private static final long FNV_PRIME = 1099511628211L;

  private static List<Item> pool;

  private RecipeRandomizer() {
  }

  public static synchronized void invalidatePool() {
    pool = null;
  }

  public static ItemStack remapCraftingResult(IRecipe recipe, ItemStack vanilla) {
    if (!ModConfig.RANDOMIZE_RECIPES || vanilla.isEmpty()) {
      return vanilla;
    }

    ResourceLocation name = recipe.getRegistryName();
    String key = name != null ? name.toString() : recipe.getClass().getName();

    return pickDifferent(vanilla, key, vanilla.getCount());
  }

  public static ItemStack remapSmeltingResult(ItemStack vanillaOutput) {
    if (!ModConfig.RANDOMIZE_RECIPES || vanillaOutput.isEmpty()) {
      return vanillaOutput;
    }

    String key = "furnace:" + ForgeRegistries.ITEMS.getKey(vanillaOutput.getItem()) + "#" + vanillaOutput.getMetadata();

    return pickDifferent(vanillaOutput, key, vanillaOutput.getCount());
  }

  private static ItemStack pickDifferent(ItemStack vanilla, String key, int count) {
    List<Item> items = pool();
    int idx = positiveMod(hash64(key), items.size());

    for (int i = 0; i < items.size(); i++) {
      Item candidate = items.get((idx + i) % items.size());
      if (candidate != vanilla.getItem()) {
        return new ItemStack(candidate, Math.max(1, count));
      }
    }

    return vanilla.copy();
  }

  private static long hash64(String key) {
    long h = FNV_OFFSET_BASIS;
    h ^= ModConfig.RECIPE_SEED;
    h *= FNV_PRIME;

    for (int i = 0; i < key.length(); i++) {
      h ^= key.charAt(i);
      h *= FNV_PRIME;
    }

    return h;
  }

  private static int positiveMod(long v, int m) {
    long r = v % m;
    return (int) (r < 0 ? r + m : r);
  }

  private static synchronized List<Item> pool() {
    if (pool == null) {
      List<Item> built = new ArrayList<>();

      for (Item item : ForgeRegistries.ITEMS.getValuesCollection()) {
        if (item == Items.AIR || item == BijuuScroll.SCROLL) {
          continue;
        }

        built.add(item);
      }

      built.sort(Comparator.comparing(item -> ForgeRegistries.ITEMS.getKey(item).toString()));
      pool = built;
    }

    return pool;
  }
}
