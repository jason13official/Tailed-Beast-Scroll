package com.example.modid.impl;

import com.example.modid.ExampleMod;
import com.example.modid.Tags;
import com.example.modid.api.IHasModel;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class ItemBase extends Item implements IHasModel {

  public ItemBase(String name) {
    setRegistryName(name);
    setCreativeTab(CreativeTabs.MATERIALS);
    this.setTranslationKey(Tags.MOD_ID + ".scroll");
    ExampleMod.ITEMS.add(this);
  }

  @Override
  public void registerModels() {
    ExampleMod.proxy.registerItemModel(this, 0, "inventory");
  }
}
