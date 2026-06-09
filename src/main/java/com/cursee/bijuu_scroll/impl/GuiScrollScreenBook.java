package com.cursee.bijuu_scroll.impl;

import com.cursee.bijuu_scroll.BijuuScroll;
import com.example.modid.Tags;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;
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

import com.cursee.bijuu_scroll.proxy.ClientProxyExampleMod;
import java.io.IOException;

public class GuiScrollScreenBook extends GuiScreen {

  public static final int GUIID = 0;

  private static final ResourceLocation BOOK_GUI_TEXTURES = new ResourceLocation(Tags.MOD_ID, "textures/gui/background.png");
  private static final ResourceLocation[] BIJUU_ICONS = {
    new ResourceLocation(Tags.MOD_ID, "textures/gui/bijuu1_yellowed.png"),
    new ResourceLocation(Tags.MOD_ID, "textures/gui/bijuu2_yellowed.png"),
    new ResourceLocation(Tags.MOD_ID, "textures/gui/bijuu3_yellowed.png"),
    new ResourceLocation(Tags.MOD_ID, "textures/gui/bijuu4_yellowed.png"),
    new ResourceLocation(Tags.MOD_ID, "textures/gui/bijuu5_yellowed.png"),
    new ResourceLocation(Tags.MOD_ID, "textures/gui/bijuu6_yellowed.png"),
    new ResourceLocation(Tags.MOD_ID, "textures/gui/bijuu7_yellowed.png"),
    new ResourceLocation(Tags.MOD_ID, "textures/gui/bijuu8_yellowed.png"),
    new ResourceLocation(Tags.MOD_ID, "textures/gui/bijuu9_yellowed.png"),
    new ResourceLocation(Tags.MOD_ID, "textures/gui/bijuu10_yellowed.png")
  };
  private static final ResourceLocation[] BIJUU_ICONS_COLORED = {
    new ResourceLocation(Tags.MOD_ID, "textures/gui/bijuu1.png"),
    new ResourceLocation(Tags.MOD_ID, "textures/gui/bijuu2.png"),
    new ResourceLocation(Tags.MOD_ID, "textures/gui/bijuu3.png"),
    new ResourceLocation(Tags.MOD_ID, "textures/gui/bijuu4.png"),
    new ResourceLocation(Tags.MOD_ID, "textures/gui/bijuu5.png"),
    new ResourceLocation(Tags.MOD_ID, "textures/gui/bijuu6.png"),
    new ResourceLocation(Tags.MOD_ID, "textures/gui/bijuu7.png"),
    new ResourceLocation(Tags.MOD_ID, "textures/gui/bijuu8.png"),
    new ResourceLocation(Tags.MOD_ID, "textures/gui/bijuu9.png"),
    new ResourceLocation(Tags.MOD_ID, "textures/gui/bijuu10.png")
  };
  private static final int[][] BIJUU_SIZES = {
    {52, 54}, {56, 54}, {54, 54}, {58, 54}, {56, 54},
    {50, 54}, {68, 66}, {52, 54}, {52, 54}, {119, 66}
  };

  private static final String[] BEAST_NAMES = {
    "Shukaku", "Matatabi", "Isobu", "Son Goku", "Kokuo",
    "Saiken", "Chomei", "Gyuki", "Kurama", "Ten-Tails"
  };
  private static final String[] BEAST_ALT_NAMES = {
    "One-Tail", "Two-Tails", "Three-Tails", "Four-Tails", "Five-Tails",
    "Six-Tails", "Seven-Tails", "Eight-Tails", "Nine-Tails", "Ten-Tails"
  };
  private static final String[] BEAST_RATINGS = {
    "✦✧✧✧✧",                                           // 1-tail:  ✦✧✧✧✧
    "✦✦✧✧✧",                                           // 2-tails: ✦✦✧✧✧
    "✦✦✧✧✧",                                           // 3-tails: ✦✦✧✧✧
    "✦✦✦✧✧",                                           // 4-tails: ✦✦✦✧✧
    "✦✦✦✧✧",                                           // 5-tails: ✦✦✦✧✧
    "✦✦✦✧✧",                                           // 6-tails: ✦✦✦✧✧
    "✦✦✦✦✧",                                           // 7-tails: ✦✦✦✦✧
    "✦✦✦✦✦",                                           // 8-tails: ✦✦✦✦✦
    "✦✦✦✦✦",                                           // 9-tails: ✦✦✦✦✦
    "✦✦✦✦✦✦✦✦✦"                   // 10-tails: ✦✦✦✦✦✦✦✦✦
  };
  private static final int RATING_COLOR_NORMAL   = 0xB8860B; // dark goldenrod
  private static final int RATING_COLOR_TEN_TAILS = 0xCC2200; // deep red — off the charts

  private static final String[] BEAST_LORE = {
    "A massive one-tailed tanuki made of living sand with dark markings and star-shaped eyes.",
    "A graceful two-tailed cat formed from brilliant blue flames with glowing yellow eyes.",
    "A gigantic three-tailed turtle with a spiked shell, horned face, and armored body.",
    "A towering four-tailed gorilla with crimson fur, green skin, and curved horns.",
    "A majestic five-tailed beast resembling a white horse crossed with a dolphin, with blue horns.",
    "An enormous six-tailed white slug with blue markings and long antennae.",
    "A vibrant seven-tailed insect resembling a rhinoceros beetle with dragonfly wings.",
    "A colossal eight-tailed ox-octopus with four horns and powerful tentacles.",
    "A gigantic nine-tailed orange fox with crimson eyes and black facial markings.",
    "An immense, otherworldly beast with a single eye, ten tails, and a monstrous, tree-like form."
  };

  protected int xSize = 320;
  protected int ySize = 180;
  protected int guiLeft;
  protected int guiTop;
  private float oldMouseX;
  private float oldMouseY;

  private static final int ICON_SIZE = 28;
  private static final int ICON_GAP = 3;
  private static final int ICON_START_X = 171 - 4;
  private static final int ICON_START_Y = 34;
  private static final int ICON_10_W = 50; // bijuu10 wider slot (119:66 ratio → ~50px at ICON_SIZE height)

  private static final int ENTITY_WINDOW_X = 60;
  private static final int ENTITY_WINDOW_Y = 20;
  private static final int ENTITY_WINDOW_W = 100;
  private static final int ENTITY_WINDOW_H = 60;
  private static final int ENTITY_WINDOW_COLOR = 0xFF9A8878;

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
    if (index == 9) {
      int gridWidth = 3 * ICON_SIZE + 2 * ICON_GAP;
      return new int[]{ guiLeft + ICON_START_X + (gridWidth - ICON_10_W) / 2,
                        guiTop  + ICON_START_Y + 3 * (ICON_SIZE + ICON_GAP) };
    }
    int row = index / 3;
    int col = index % 3;
    return new int[]{ guiLeft + ICON_START_X + col * (ICON_SIZE + ICON_GAP),
                      guiTop  + ICON_START_Y + row * (ICON_SIZE + ICON_GAP) };
  }

  @Override
  protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
    super.mouseClicked(mouseX, mouseY, mouseButton);
    if (this.tailedBeasts == null || this.tailedBeasts.length == 0) return;
    for (int idx = 0; idx < this.tailedBeasts.length; idx++) {
      int[] pos = getSlotPos(idx);
      int sx = pos[0], sy = pos[1];
      int slotW = (idx == 9) ? ICON_10_W : ICON_SIZE;
      if (mouseX >= sx && mouseX < sx + slotW && mouseY >= sy && mouseY < sy + ICON_SIZE) {
        if (mouseButton == 0) {
          this.selectedBeastIndex = idx;
        } else if (mouseButton == 1) {
          if (idx < 9) {
            ClientProxyExampleMod.BEAST_UNLOCKED.put(idx, true);
          } else {
            boolean allUnlocked = true;
            for (int k = 0; k < 9; k++) {
              if (!ClientProxyExampleMod.BEAST_UNLOCKED.getOrDefault(k, false)) {
                allUnlocked = false;
                break;
              }
            }
            if (allUnlocked) {
              ClientProxyExampleMod.BEAST_UNLOCKED.put(9, true);
              BijuuScroll.TEN_TAILS_VIEWABLE.set(true);
            }
          }
        }
        break;
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

    doDrawScreen(this, mouseX, mouseY, partialTicks);
    super.drawScreen(mouseX, mouseY, partialTicks);
    this.oldMouseX = (float) mouseX;
    this.oldMouseY = (float) mouseY;
  }

  private static void doDrawScreen(GuiScrollScreenBook guiScrollScreenBook, int mouseX, int mouseY, float partialTicks) {
    int i = guiScrollScreenBook.guiLeft;
    int j = guiScrollScreenBook.guiTop;

    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    guiScrollScreenBook.mc.getTextureManager().bindTexture(BOOK_GUI_TEXTURES);
    guiScrollScreenBook.drawScaledCustomSizeModalRect(i - 20, j - 20, 0, 0, 960, 540, guiScrollScreenBook.xSize + 40, guiScrollScreenBook.ySize + 40, 960, 540);

    if (guiScrollScreenBook.tailedBeasts != null && guiScrollScreenBook.tailedBeasts.length > 0) {
      // Large entity preview on left page
      EntityTailedBeast.Base beast = guiScrollScreenBook.tailedBeasts[guiScrollScreenBook.selectedBeastIndex];
      int scale = guiScrollScreenBook.selectedBeastIndex == 9 ? 1 : 2; // guiScrollScreenBook.computePreviewScale(beast);

      drawRect(i + ENTITY_WINDOW_X, j + ENTITY_WINDOW_Y,
               i + ENTITY_WINDOW_X + ENTITY_WINDOW_W,
               j + ENTITY_WINDOW_Y + ENTITY_WINDOW_H,
          0x889A8878);

      drawRect(i + ENTITY_WINDOW_X, j + ENTITY_WINDOW_Y + ENTITY_WINDOW_H - 1,
          i + ENTITY_WINDOW_X + ENTITY_WINDOW_W,
          j + ENTITY_WINDOW_Y + ENTITY_WINDOW_H + 1,
          0xAA000000
          );

//      drawRect(i + ENTITY_WINDOW_X, j + ENTITY_WINDOW_Y + ENTITY_WINDOW_H - 1,
//          i + ENTITY_WINDOW_X + ENTITY_WINDOW_W,
//          j + ENTITY_WINDOW_Y + ENTITY_WINDOW_H + 1,
//          ENTITY_WINDOW_COLOR
//          );

      Minecraft mc = guiScrollScreenBook.mc;
      ScaledResolution sr = new ScaledResolution(mc);
      double scaleW = (double) mc.displayWidth  / sr.getScaledWidth_double();
      double scaleH = (double) mc.displayHeight / sr.getScaledHeight_double();
      GL11.glEnable(GL11.GL_SCISSOR_TEST);
      GL11.glScissor(
          (int)((i + ENTITY_WINDOW_X) * scaleW),
          (int)(mc.displayHeight - (j + ENTITY_WINDOW_Y + ENTITY_WINDOW_H) * scaleH),
          (int)(ENTITY_WINDOW_W * scaleW),
          (int)(ENTITY_WINDOW_H * scaleH)
      );

      if (guiScrollScreenBook.selectedBeastIndex != 9 || BijuuScroll.TEN_TAILS_VIEWABLE.get()) {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      } else {
        GlStateManager.color(0.0f, 0.0f, 0.0f, 1.0F);
      }

      int xoffset = 80 + 30;
      int yoffset = 80 + 50 - 20 + (guiScrollScreenBook.selectedBeastIndex == 9 ? -10 : -20); // offset further upwards


      GlStateManager.pushMatrix();

      // normal offsets
      GlStateManager.translate(30, 10, 20);
      GlStateManager.scale(0.8, 0.8, 0.8);

      switch (guiScrollScreenBook.selectedBeastIndex) {
        case 0: GlStateManager.translate(0, 2, 0);
        case 1: ;
        case 2: ;
        case 3: GlStateManager.translate(0, -5, 0);
        case 4: ;
        case 5: ;
        case 6: GlStateManager.translate(0, -5, 0);
        case 7: ;
        case 8: GlStateManager.translate(0, -5, 0);
        case 9: GlStateManager.translate(0, -5, 0);
      }

      GuiInventory.drawEntityOnScreen(i + xoffset, j + yoffset, scale,
          (float)(i + xoffset) - guiScrollScreenBook.oldMouseX, (float)(j + xoffset) - guiScrollScreenBook.oldMouseY, beast);
      GlStateManager.popMatrix();

      GL11.glDisable(GL11.GL_SCISSOR_TEST);

      boolean tenTailsVisible = guiScrollScreenBook.selectedBeastIndex != 9
          || BijuuScroll.TEN_TAILS_VIEWABLE.get();

      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      String loreText = tenTailsVisible ? BEAST_LORE[guiScrollScreenBook.selectedBeastIndex] : "Unknown appearance.";
      int loreX = i + 8 + 53;
      int loreY = j + 93 - 10;
      java.util.List<String> loreLines = guiScrollScreenBook.fontRenderer.listFormattedStringToWidth(loreText, 100);
      for (String line : loreLines) {
        guiScrollScreenBook.fontRenderer.drawString(line, loreX, loreY, 0x5C3A1E, false);
        loreY += guiScrollScreenBook.fontRenderer.FONT_HEIGHT;
      }

      String properName = BEAST_NAMES[guiScrollScreenBook.selectedBeastIndex];
      String altName = BEAST_ALT_NAMES[guiScrollScreenBook.selectedBeastIndex];
      int centerX = (i + guiScrollScreenBook.xSize / 4) - 19;
      int nameY = j + guiScrollScreenBook.ySize - 38;
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

      String unknownText = "?????";
      // guiScrollScreenBook.fontRenderer.drawString(tenTailsVisible ? properName : "?????", centerX, nameY, 0x5C3A1E, false);
      // guiScrollScreenBook.fontRenderer.drawString(tenTailsVisible ? altName : "?????", centerX, nameY + 10, 0x8B6914, false);

      guiScrollScreenBook.fontRenderer.drawString(tenTailsVisible ? properName + ":" + altName : "?????", centerX, nameY, 0x5C3A1E, false);

      boolean isTenTails = guiScrollScreenBook.selectedBeastIndex == 9;
      String rating = tenTailsVisible ? BEAST_RATINGS[guiScrollScreenBook.selectedBeastIndex] : "?????";
      int ratingColor = isTenTails ? RATING_COLOR_TEN_TAILS : RATING_COLOR_NORMAL;
      // guiScrollScreenBook.fontRenderer.drawString(rating, centerX, nameY + 20, ratingColor, false);
      guiScrollScreenBook.fontRenderer.drawString(rating, centerX, nameY + 10, ratingColor, false);

      // 10 icon slots on right page
      for (int idx = 0; idx < guiScrollScreenBook.tailedBeasts.length; idx++) {
        int[] pos = guiScrollScreenBook.getSlotPos(idx);
        int sx = pos[0], sy = pos[1];

        int slotW = (idx == 9) ? ICON_10_W : ICON_SIZE;

        if (idx == guiScrollScreenBook.selectedBeastIndex) {
          drawRect(sx - 2, sy - 2, sx + slotW + 2, sy + ICON_SIZE + 2, 0xFFFFDD00);
        }
        // drawRect(sx - 1, sy - 1, sx + slotW + 1, sy + ICON_SIZE + 1, 0xFF333333);
        // drawRect(sx, sy, sx + slotW, sy + ICON_SIZE, 0x55000000);

        // Entity thumbnail (commented out — replaced with placeholder textures)
        // EntityTailedBeast.Base thumb = this.tailedBeasts[idx];
        // int thumbScale = Math.max(1, (int)((ICON_SIZE - 8) / (thumb.height > 0 ? thumb.height : 1.0F)));
        // GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        // GuiInventory.drawEntityOnScreen(sx + ICON_SIZE / 2, sy + ICON_SIZE - 2, thumbScale,
        //     (float)(sx + ICON_SIZE / 2) - this.oldMouseX,
        //     (float)(sy + ICON_SIZE - 2 - 10) - this.oldMouseY, thumb);

        boolean unlocked = ClientProxyExampleMod.BEAST_UNLOCKED.getOrDefault(idx, false);
        if (unlocked) {
          GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        } else if (idx != 9 || BijuuScroll.TEN_TAILS_VIEWABLE.get()) {
          GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        } else {
          GlStateManager.color(0.0f, 0.0f, 0.0f, 1.0F);
        }
        ResourceLocation icon = unlocked ? BIJUU_ICONS_COLORED[idx] : BIJUU_ICONS[idx];
        guiScrollScreenBook.mc.getTextureManager().bindTexture(icon);
        int iconW = BIJUU_SIZES[idx][0], iconH = BIJUU_SIZES[idx][1];
        guiScrollScreenBook.drawScaledCustomSizeModalRect(sx, sy, 0, 0, iconW, iconH, slotW, ICON_SIZE, iconW, iconH);
      }
    }
  }

  private int computePreviewScale(EntityTailedBeast.Base beast) {
    float height = beast.height > 0 ? beast.height : 1.0F;
    int scale = (int)(PREVIEW_TARGET_HEIGHT / height);
    return Math.max(2, Math.min(scale, 60));
  }
}
