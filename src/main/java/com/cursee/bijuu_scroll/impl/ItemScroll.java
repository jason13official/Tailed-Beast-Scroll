package com.cursee.bijuu_scroll.impl;

import com.cursee.bijuu_scroll.BijuuScroll;
import com.cursee.bijuu_scroll.Tags;
import com.cursee.bijuu_scroll.api.IHasModel;
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

    BijuuScroll.ITEMS.add(this);
  }

  @Override
  public void registerModels() {
    BijuuScroll.proxy.registerItemModel(this, 0, "inventory");
  }

  @Override
  public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {

    playerIn.openGui(BijuuScroll.instance, GuiScrollScreenBook.GUIID, worldIn, (int) playerIn.posX, (int) playerIn.posY, (int) playerIn.posZ);

    return super.onItemRightClick(worldIn, playerIn, handIn);
  }
}
