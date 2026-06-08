package com.example.modid.impl;

import com.example.modid.Tags;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
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

  private static final ResourceLocation BOOK_GUI_TEXTURES = new ResourceLocation(Tags.MOD_ID, "textures/gui/background.png");

  protected int xSize = 320;
  protected int ySize = 180;
  protected int guiLeft;
  protected int guiTop;
  private float oldMouseX;
  private float oldMouseY;

  private static final int ICON_SIZE = 28;
  private static final int ICON_GAP = 3;
  private static final int ICON_START_X = 171;
  private static final int ICON_START_Y = 34;

  private EntityTailedBeast.Base[] tailedBeasts;
  private int selectedBeastIndex = 0;
  private static final int PREVIEW_TARGET_HEIGHT = 80;

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
  }

  public void onGuiClosed() {
    Keyboard.enableRepeatEvents(false);
  }

  private int[] getSlotPos(int index) {
    int row = index < 9 ? index / 3 : 3;
    int col = index < 9 ? index % 3 : 1;
    return new int[]{ guiLeft + ICON_START_X + col * (ICON_SIZE + ICON_GAP),
                      guiTop  + ICON_START_Y + row * (ICON_SIZE + ICON_GAP) };
  }

  @Override
  protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
    super.mouseClicked(mouseX, mouseY, mouseButton);
    if (mouseButton == 0 && this.tailedBeasts != null && this.tailedBeasts.length > 0) {
      for (int idx = 0; idx < this.tailedBeasts.length; idx++) {
        int[] pos = getSlotPos(idx);
        int sx = pos[0], sy = pos[1];
        if (mouseX >= sx && mouseX < sx + ICON_SIZE && mouseY >= sy && mouseY < sy + ICON_SIZE) {
          this.selectedBeastIndex = idx;
          break;
        }
      }
    }
  }

  @Override
  public void handleMouseInput() throws IOException {
    super.handleMouseInput();
    if (this.tailedBeasts != null && this.tailedBeasts.length > 0) {
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
    this.drawScaledCustomSizeModalRect(i, j, 0, 0, 480, 270, this.xSize, this.ySize, 480, 270);

    if (this.tailedBeasts != null && this.tailedBeasts.length > 0) {
      // Large entity preview on left page
      EntityTailedBeast.Base beast = this.tailedBeasts[this.selectedBeastIndex];
      int scale = computePreviewScale(beast);
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      GuiInventory.drawEntityOnScreen(i + 80, j + 130, scale,
          (float)(i + 80) - this.oldMouseX, (float)(j + 80) - this.oldMouseY, beast);

      // 10 icon slots on right page
      for (int idx = 0; idx < this.tailedBeasts.length; idx++) {
        int[] pos = getSlotPos(idx);
        int sx = pos[0], sy = pos[1];

        if (idx == this.selectedBeastIndex) {
          drawRect(sx - 2, sy - 2, sx + ICON_SIZE + 2, sy + ICON_SIZE + 2, 0xFFFFDD00);
        }
        drawRect(sx - 1, sy - 1, sx + ICON_SIZE + 1, sy + ICON_SIZE + 1, 0xFF333333);
        drawRect(sx, sy, sx + ICON_SIZE, sy + ICON_SIZE, 0x55000000);

        EntityTailedBeast.Base thumb = this.tailedBeasts[idx];
        int thumbScale = Math.max(1, (int)((ICON_SIZE - 8) / (thumb.height > 0 ? thumb.height : 1.0F)));
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GuiInventory.drawEntityOnScreen(sx + ICON_SIZE / 2, sy + ICON_SIZE - 2, thumbScale,
            (float)(sx + ICON_SIZE / 2) - this.oldMouseX,
            (float)(sy + ICON_SIZE - 2 - 10) - this.oldMouseY, thumb);
      }
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
