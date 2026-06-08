package com.example.modid.proxy;

import net.minecraft.item.Item;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

public interface CommonProxy {

  void preInit(FMLPreInitializationEvent var1);

  void init(FMLInitializationEvent var1);

  void postInit(FMLPostInitializationEvent var1);

  void serverLoad(FMLServerStartingEvent var1);

  void registerItemModel(Item item, int meta, String id);
}
