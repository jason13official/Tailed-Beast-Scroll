package com.example.modid.impl;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.narutomod.NarutomodMod;
import net.narutomod.entity.EntityEightTails;
import net.narutomod.entity.EntityFiveTails;
import net.narutomod.entity.EntityFourTails;
import net.narutomod.entity.EntityNineTails;
import net.narutomod.entity.EntityOneTail;
import net.narutomod.entity.EntitySevenTails;
import net.narutomod.entity.EntitySixTails;
import net.narutomod.entity.EntityTailedBeast;
import net.narutomod.entity.EntityTenTails;
import net.narutomod.entity.EntityThreeTails;
import net.narutomod.entity.EntityTwoTails;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.io.IOException;

public class GuiScrollScreenBook extends GuiScreen {

  public static final int GUIID = 0;

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

  private EntityTailedBeast.Base[] tailedBeasts;
  private int selectedBeastIndex = 0;
  private static final int PREVIEW_TARGET_HEIGHT = 50;

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

    if (this.tailedBeasts == null) {
      World world = Minecraft.getMinecraft().world;
      this.tailedBeasts = world == null ? new EntityTailedBeast.Base[0] : new EntityTailedBeast.Base[]{
        new EntityOneTail.EntityCustom(world),
        new EntityTwoTails.EntityCustom(world),
        new EntityThreeTails.EntityCustom(world),
        new EntityFourTails.EntityCustom(world),
        new EntityFiveTails.EntityCustom(world),
        new EntitySixTails.EntityCustom(world),
        new EntitySevenTails.EntityCustom(world),
        new EntityEightTails.EntityCustom(world),
        new EntityNineTails.EntityCustom(world),
        new EntityTenTails.EntityCustom(world)
      };
    }

    this.buttonList.clear();
    this.buttonList.add(new GuiButton(0, this.guiLeft + 30, this.guiTop + 75, 16, 20, "<"));
    this.buttonList.add(new GuiButton(1, this.guiLeft + 130, this.guiTop + 75, 16, 20, ">"));
  }

  public void onGuiClosed() {
    Keyboard.enableRepeatEvents(false);
  }

  @Override
  protected void actionPerformed(GuiButton button) {
    if (this.tailedBeasts.length == 0) return;
    if (button.id == 0) {
      this.selectedBeastIndex = (this.selectedBeastIndex - 1 + this.tailedBeasts.length) % this.tailedBeasts.length;
    } else if (button.id == 1) {
      this.selectedBeastIndex = (this.selectedBeastIndex + 1) % this.tailedBeasts.length;
    }
  }

  @Override
  public void handleMouseInput() throws IOException {
    super.handleMouseInput();
    if (this.tailedBeasts.length > 0) {
      int wheel = Mouse.getEventDWheel();
      if (wheel > 0) {
        this.selectedBeastIndex = (this.selectedBeastIndex - 1 + this.tailedBeasts.length) % this.tailedBeasts.length;
      } else if (wheel < 0) {
        this.selectedBeastIndex = (this.selectedBeastIndex + 1) % this.tailedBeasts.length;
      }
    }
  }

  @Override
  public void drawScreen(int mouseX, int mouseY, float partialTicks) {

    int i = this.guiLeft;
    int j = this.guiTop;

    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    this.mc.getTextureManager().bindTexture(BOOK_GUI_TEXTURES);
    this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);

    if (this.tailedBeasts.length > 0) {
      EntityTailedBeast.Base beast = this.tailedBeasts[this.selectedBeastIndex];
      int scale = computePreviewScale(beast);
      GuiInventory.drawEntityOnScreen(i + 51, j + 75, scale, (float)(i + 51) - this.oldMouseX, (float)(j + 75 - 50) - this.oldMouseY, beast);
    }

    super.drawScreen(mouseX, mouseY, partialTicks);

    this.oldMouseX = (float)mouseX;
    this.oldMouseY = (float)mouseY;
  }

  private int computePreviewScale(EntityTailedBeast.Base beast) {
    float height = beast.height > 0 ? beast.height : 1.0F;
    int scale = (int)(PREVIEW_TARGET_HEIGHT / height);
    return Math.max(2, Math.min(scale, 60));
  }
}
