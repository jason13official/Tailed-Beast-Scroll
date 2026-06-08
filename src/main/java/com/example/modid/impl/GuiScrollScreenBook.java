package com.example.modid.impl;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.narutomod.NarutomodMod;
import net.narutomod.entity.EntityTailedBeast;
import org.lwjgl.input.Keyboard;

public class GuiScrollScreenBook extends GuiScreen {

  private static final ResourceLocation BOOK_GUI_TEXTURES = new ResourceLocation("textures/gui/book.png");

  /** The X size of the inventory window in pixels. */
  protected int xSize = 176;
  /** The Y size of the inventory window in pixels. */
  protected int ySize = 166;
  /** Starting X position for the Gui. Inconsistent use for Gui backgrounds. */
  protected int guiLeft;
  /** Starting Y position for the Gui. Inconsistent use for Gui backgrounds. */
  protected int guiTop;
  /** The old x position of the mouse pointer */
  private float oldMouseX;
  /** The old y position of the mouse pointer */
  private float oldMouseY;

  EntityTailedBeast[] tailedBeasts = new EntityTailedBeast[]{};

  private final EntityPlayer editingPlayer;
  private final ItemStack scroll;

  public GuiScrollScreenBook(EntityPlayer player, ItemStack scroll) {
    this.editingPlayer = player;
    this.scroll = scroll;
  }

  @Override
  public void initGui() {
    super.initGui();
    this.guiLeft = (this.width - this.xSize) / 2;
    this.guiTop = (this.height - this.ySize) / 2;
  }

  public void onGuiClosed() {
    Keyboard.enableRepeatEvents(false);
  }

  @Override
  public void drawScreen(int mouseX, int mouseY, float partialTicks) {

    int i = this.guiLeft;
    int j = this.guiTop;

    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    this.mc.getTextureManager().bindTexture(BOOK_GUI_TEXTURES);

    super.drawScreen(mouseX, mouseY, partialTicks);

    this.oldMouseX = (float)mouseX;
    this.oldMouseY = (float)mouseY;
  }

  protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
  {
    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    this.mc.getTextureManager().bindTexture(BOOK_GUI_TEXTURES);
    int i = this.guiLeft;
    int j = this.guiTop;
    this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);
    GuiInventory.drawEntityOnScreen(i + 51, j + 75, 30, (float)(i + 51) - this.oldMouseX, (float)(j + 75 - 50) - this.oldMouseY, this.mc.player);
  }
}
