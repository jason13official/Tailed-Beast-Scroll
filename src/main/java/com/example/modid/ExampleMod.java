package com.example.modid;

import com.example.modid.api.IHasModel;
import com.example.modid.impl.ItemBase;
import com.example.modid.proxy.ClientProxyExampleMod;
import com.example.modid.proxy.CommonProxy;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLLoadCompleteEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerRespawnEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = Tags.MOD_ID, name = Tags.MOD_NAME, version = Tags.VERSION)
public class ExampleMod {

  public static final Logger LOGGER = LogManager.getLogger(Tags.MOD_NAME);
  public static final List<Item> ITEMS = new ArrayList<>();
  public static final Item SCROLL = new ItemBase("scroll");

  @Instance
  public static ExampleMod instance;

  @SidedProxy(
      clientSide = "com.example.modid.proxy.ClientProxyExampleMod",
      serverSide = "com.example.modid.proxy.ServerProxyExampleMod"
  )
  public static CommonProxy proxy;

  /// [CleanRoom Events Overview](https://cleanroommc.com/wiki/forge-mod-development/event#overview)
  @Mod.EventHandler
  public void preInit(FMLPreInitializationEvent event) {
    MinecraftForge.EVENT_BUS.register(this);
  }

  @Mod.EventHandler
  public void onLoadComplete(FMLLoadCompleteEvent event) {
  }

  @SubscribeEvent
  public void onRegisterItems(RegistryEvent.Register<Item> event) {
    event.getRegistry().register(SCROLL);
  }

  @SubscribeEvent
  public void onRegisterModels(ModelRegistryEvent event) {
    ITEMS.forEach(item -> {
      if (item instanceof IHasModel) {
        ((IHasModel) item).registerModels();
      }
    });
  }

  @SubscribeEvent
  public void onPlayerEvent(PlayerLoggedInEvent event) {
    if (event.player instanceof EntityPlayerMP) {
      EntityPlayerMP serverPlayer = (EntityPlayerMP) event.player;

      // player spawns with scroll in inventory
      if (!serverPlayer.getTags().contains("tailed_beast_scroll_given")) {

        serverPlayer.addItemStackToInventory(new ItemStack(SCROLL));

        serverPlayer.addTag("tailed_beast_scroll_given");
      }
    }
  }
}
