package com.cursee.bijuu_scroll.command;

import com.cursee.bijuu_scroll.config.ModConfig;
import com.cursee.bijuu_scroll.impl.RandomCraftHandler;
import com.cursee.bijuu_scroll.impl.RecipeRandomizer;
import java.util.Random;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

public class CommandRandomLoot extends CommandBase {

  @Override
  public String getName() {
    return "randomloot";
  }

  @Override
  public String getUsage(ICommandSender sender) {
    return "/randomloot";
  }

  @Override
  public int getRequiredPermissionLevel() {
    return 2;
  }

  @Override
  public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
    int seed = new Random().nextInt();

    ModConfig.setRecipeSeed(seed);
    RecipeRandomizer.invalidatePool();
    RandomCraftHandler.clearPreviewCache();

    sender.sendMessage(new TextComponentString("Recipe output seed set to " + seed));
  }
}
