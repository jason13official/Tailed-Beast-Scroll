package com.cursee.bijuu_scroll.impl;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.Nullable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerFurnace;
import net.minecraft.inventory.ContainerPlayer;
import net.minecraft.inventory.ContainerWorkbench;
import net.minecraft.inventory.InventoryCraftResult;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

/**
 * Patches the crafting-result preview slot to show {@link RecipeRandomizer}'s
 * deterministic replacement, then swaps the vanilla item the player actually
 * receives for that same replacement on craft/smelt.
 */
public class RandomCraftHandler {

  private static final String REMAPPED_TAG = "bijuu_scroll_remapped";

  private static final Map<UUID, ItemStack> lastPreviewShown = new ConcurrentHashMap<>();

  public static void clearPreviewCache() {
    lastPreviewShown.clear();
  }

  @SubscribeEvent
  public void onPlayerTick(TickEvent.PlayerTickEvent event) {
    if (event.phase != TickEvent.Phase.END) {
      return;
    }

    EntityPlayer player = event.player;
    Container container = player.openContainer;

    if (container instanceof ContainerPlayer) {
      ContainerPlayer cp = (ContainerPlayer) container;
      updateCraftPreview(player, cp.craftMatrix, cp.craftResult);
    } else if (container instanceof ContainerWorkbench) {
      ContainerWorkbench cw = (ContainerWorkbench) container;
      updateCraftPreview(player, cw.craftMatrix, cw.craftResult);
    } else if (container instanceof ContainerFurnace) {
      updateFurnacePreview(container);
    }
  }

  private static void updateCraftPreview(EntityPlayer player, InventoryCrafting inv, InventoryCraftResult out) {
    IRecipe match = findMatchingRecipeSafe(inv, player.world);
    if (match == null) {
      lastPreviewShown.remove(player.getUniqueID());
      return;
    }

    ItemStack vanilla = safeCraftingResult(match, inv);
    if (vanilla.isEmpty()) {
      vanilla = match.getRecipeOutput().copy();
    }
    if (vanilla.isEmpty()) {
      return;
    }

    ItemStack replacement = RecipeRandomizer.remapCraftingResult(match, vanilla);
    if (replacement.isEmpty()) {
      return;
    }

    replacement = replacement.copy();
    replacement.setCount(Math.max(1, vanilla.getCount()));

    ItemStack current = out.getStackInSlot(0);
    boolean same = current.getItem() == replacement.getItem()
        && current.getCount() == replacement.getCount()
        && ItemStack.areItemStacksEqual(current, replacement);

    if (!same) {
      out.setInventorySlotContents(0, replacement.copy());
      player.openContainer.detectAndSendChanges();
    }

    if (!player.world.isRemote) {
      lastPreviewShown.put(player.getUniqueID(), replacement.copy());
    }
  }

  @Nullable
  private static IRecipe findMatchingRecipeSafe(InventoryCrafting inv, World world) {
    try {
      return CraftingManager.findMatchingRecipe(inv, world);
    } catch (Throwable t) {
      return null;
    }
  }

  private static ItemStack safeCraftingResult(IRecipe recipe, InventoryCrafting inv) {
    try {
      ItemStack result = recipe.getCraftingResult(inv);
      return result == null ? ItemStack.EMPTY : result.copy();
    } catch (Throwable t) {
      return ItemStack.EMPTY;
    }
  }

  private static void updateFurnacePreview(Container container) {
    Slot outputSlot = container.getSlot(2);
    ItemStack output = outputSlot.getStack();
    if (output.isEmpty() || isRemapped(output)) {
      return;
    }

    ItemStack replacement = RecipeRandomizer.remapSmeltingResult(output);
    if (replacement.isEmpty() || replacement.getItem() == output.getItem()) {
      return;
    }

    replacement = replacement.copy();
    replacement.setCount(output.getCount());
    markRemapped(replacement);
    outputSlot.putStack(replacement);
    container.detectAndSendChanges();
  }

  private static boolean isRemapped(ItemStack stack) {
    return stack.hasTagCompound() && stack.getTagCompound().getBoolean(REMAPPED_TAG);
  }

  private static void markRemapped(ItemStack stack) {
    NBTTagCompound tag = stack.hasTagCompound() ? stack.getTagCompound() : new NBTTagCompound();
    tag.setBoolean(REMAPPED_TAG, true);
    stack.setTagCompound(tag);
  }

  @SubscribeEvent
  public void onCraft(PlayerEvent.ItemCraftedEvent event) {
    EntityPlayer player = event.player;
    if (player.world.isRemote) {
      return;
    }

    ItemStack vanilla = event.crafting.copy();
    if (vanilla.isEmpty()) {
      return;
    }

    ItemStack replacement = lastPreviewShown.get(player.getUniqueID());
    if (replacement == null || replacement.isEmpty()) {
      IRecipe match = event.craftMatrix instanceof InventoryCrafting
          ? findMatchingRecipeSafe((InventoryCrafting) event.craftMatrix, player.world) : null;
      if (match == null) {
        return;
      }

      replacement = RecipeRandomizer.remapCraftingResult(match, vanilla);
      if (replacement.isEmpty()) {
        return;
      }
    }

    replacement = replacement.copy();
    replacement.setCount(Math.max(1, vanilla.getCount()));
    forceSwapResult(player, vanilla, replacement);
  }

  @SubscribeEvent
  public void onSmelted(PlayerEvent.ItemSmeltedEvent event) {
    // item was already remapped in-place by updateFurnacePreview(); just strip the marker tag.
    ItemStack stack = event.smelting;
    if (stack.hasTagCompound() && stack.getTagCompound().hasKey(REMAPPED_TAG)) {
      stack.getTagCompound().removeTag(REMAPPED_TAG);
      if (stack.getTagCompound().isEmpty()) {
        stack.setTagCompound(null);
      }
    }
  }

  private static void forceSwapResult(EntityPlayer player, ItemStack vanillaResult, ItemStack replacement) {
    ItemStack cursor = player.inventory.getItemStack();

    if (!cursor.isEmpty()
        && cursor.getItem() == vanillaResult.getItem()
        && cursor.getCount() >= vanillaResult.getCount()
        && ItemStack.areItemStacksEqual(cursor, vanillaResult)) {

      if (cursor.getCount() == vanillaResult.getCount()) {
        player.inventory.setItemStack(replacement.copy());
      } else {
        cursor.shrink(vanillaResult.getCount());
        player.inventory.setItemStack(cursor);

        ItemStack left = replacement.copy();
        if (!player.inventory.addItemStackToInventory(left)) {
          player.dropItem(left, false);
        }
      }
    } else {
      int toRemove = vanillaResult.getCount();
      int removed = player.inventory.clearMatchingItems(
          vanillaResult.getItem(), vanillaResult.getMetadata(), toRemove, vanillaResult.getTagCompound());

      if (removed < toRemove) {
        int need = toRemove - removed;

        for (int i = 0; i < player.inventory.getSizeInventory() && need > 0; i++) {
          ItemStack stack = player.inventory.getStackInSlot(i);
          if (!stack.isEmpty() && stack.getItem() == vanillaResult.getItem()) {
            int take = Math.min(need, stack.getCount());
            stack.shrink(take);
            player.inventory.setInventorySlotContents(i, stack.getCount() <= 0 ? ItemStack.EMPTY : stack);
            need -= take;
          }
        }
      }

      ItemStack give = replacement.copy();
      if (!player.inventory.addItemStackToInventory(give)) {
        player.dropItem(give, false);
      }
    }

    player.openContainer.detectAndSendChanges();
  }
}
