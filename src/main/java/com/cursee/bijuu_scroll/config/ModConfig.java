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
  private static final double[] DEFAULT_SCALE = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1};

  public static final double[] OFFSET_X = new double[10];
  public static final double[] OFFSET_Y = new double[10];
  public static final double[] OFFSET_Z = new double[10];
  public static final double[] SCALE = new double[10];

  public static double MASTER_OFFSET_X;
  public static double MASTER_OFFSET_Y;
  public static double MASTER_OFFSET_Z;

  public static boolean RANDOMIZE_RECIPES;
  public static int RECIPE_SEED;

  private static File configFile;

  public static void init(File configFile) {
    ModConfig.configFile = configFile;
    reload();
  }

  public static void reload() {
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
      SCALE[idx] = config.get(category, "scale", DEFAULT_SCALE[idx], "Uniform scale multiplier for the preview model (applied to X, Y, and Z)", 0.1, 5.0).getDouble();
    }

    String recipesCategory = "recipes";
    config.setCategoryComment(recipesCategory, "Deterministically remap every recipe's output to a random item");
    RANDOMIZE_RECIPES = config.get(recipesCategory, "randomize_recipes", true, "If true, every crafting and furnace recipe yields a different (but deterministic) item").getBoolean();
    RECIPE_SEED = config.get(recipesCategory, "recipe_seed", 1, "Seed for the recipe output randomizer; change to reroll the mapping").getInt();

    if (config.hasChanged()) {
      config.save();
    }
  }

  public static void setRecipeSeed(int seed) {
    RECIPE_SEED = seed;

    Configuration config = new Configuration(configFile);
    config.load();
    config.get("recipes", "recipe_seed", 1, "Seed for the recipe output randomizer; change to reroll the mapping").set(seed);
    config.save();
  }
}
