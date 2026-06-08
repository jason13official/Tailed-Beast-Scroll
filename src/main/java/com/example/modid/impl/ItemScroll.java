package com.example.modid.impl;

import com.example.modid.ExampleMod;
import com.example.modid.Tags;
import com.example.modid.api.IHasModel;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class ItemScroll extends Item implements IHasModel {

  public ItemScroll(String name) {
    this.maxStackSize = 1;

    setRegistryName(name);
    setTranslationKey(Tags.MOD_ID + ".scroll");
    setCreativeTab(CreativeTabs.MATERIALS);

    ExampleMod.ITEMS.add(this);
  }

  @Override
  public void registerModels() {
    ExampleMod.proxy.registerItemModel(this, 0, "inventory");
  }

  @Override
  public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {

    playerIn.openGui(ExampleMod.instance, GuiScrollScreenBook.GUIID, worldIn, (int) playerIn.posX, (int) playerIn.posY, (int) playerIn.posZ);

    return super.onItemRightClick(worldIn, playerIn, handIn);
  }
}
