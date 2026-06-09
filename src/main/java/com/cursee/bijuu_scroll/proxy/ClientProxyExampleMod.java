package com.cursee.bijuu_scroll.proxy;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

public class ClientProxyExampleMod implements CommonProxy {

  public static final Map<Integer, Boolean> BEAST_UNLOCKED = new HashMap<>();

  @Override
  public void preInit(FMLPreInitializationEvent fmlPreInitializationEvent) {

  }

  @Override
  public void init(FMLInitializationEvent fmlInitializationEvent) {

  }

  @Override
  public void postInit(FMLPostInitializationEvent fmlPostInitializationEvent) {

  }

  @Override
  public void serverLoad(FMLServerStartingEvent fmlServerStartingEvent) {

  }

  @Override
  public void registerItemModel(Item item, int meta, String id) {
    ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(item.getRegistryName(), id));
  }
}
