package com.cursee.bijuu_scroll.config;

import java.io.File;
import net.minecraftforge.common.config.Configuration;

public class ModConfig {

  private static final String[] BEAST_NAMES = {
    "Shukaku (1-Tail)", "Matatabi (2-Tails)", "Isobu (3-Tails)", "Son Goku (4-Tails)", "Kokuo (5-Tails)",
    "Saiken (6-Tails)", "Chomei (7-Tails)", "Gyuki (8-Tails)", "Kurama (9-Tails)", "Ten-Tails (10-Tails)"
  };

  private static final double[] DEFAULT_OFFSET_X = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
  private static final double[] DEFAULT_OFFSET_Y = {0, 0, 0, -10, 0, 0, 0, -10, 0, -5};
  private static final double[] DEFAULT_OFFSET_Z = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

  public static final double[] OFFSET_X = new double[10];
  public static final double[] OFFSET_Y = new double[10];
  public static final double[] OFFSET_Z = new double[10];

  public static double MASTER_OFFSET_X;
  public static double MASTER_OFFSET_Y;
  public static double MASTER_OFFSET_Z;

  public static void init(File configFile) {
    Configuration config = new Configuration(configFile);
    config.load();

    String masterCategory = "offsets.master";
    config.setCategoryComment(masterCategory, "Base preview offset applied to all Bijuu");
    MASTER_OFFSET_X = config.get(masterCategory, "offset_x", 60.0, "X offset for the preview model", -200.0, 200.0).getDouble();
    MASTER_OFFSET_Y = config.get(masterCategory, "offset_y", 10.0, "Y offset for the preview model", -200.0, 200.0).getDouble();
    MASTER_OFFSET_Z = config.get(masterCategory, "offset_z", 20.0, "Z offset for the preview model", -200.0, 200.0).getDouble();

    for (int idx = 0; idx < 10; idx++) {
      String category = "offsets.bijuu" + (idx + 1);
      config.setCategoryComment(category, "Preview offsets for " + BEAST_NAMES[idx]);

      OFFSET_X[idx] = config.get(category, "offset_x", DEFAULT_OFFSET_X[idx], "X offset for the preview model", -50.0, 50.0).getDouble();
      OFFSET_Y[idx] = config.get(category, "offset_y", DEFAULT_OFFSET_Y[idx], "Y offset for the preview model", -50.0, 50.0).getDouble();
      OFFSET_Z[idx] = config.get(category, "offset_z", DEFAULT_OFFSET_Z[idx], "Z offset for the preview model", -50.0, 50.0).getDouble();
    }

    if (config.hasChanged()) {
      config.save();
    }
  }
}
