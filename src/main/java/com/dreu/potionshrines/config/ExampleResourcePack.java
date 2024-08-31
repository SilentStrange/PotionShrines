package com.dreu.potionshrines.config;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ExampleResourcePack {
    public static void generate() {
        if (new File("resourcepacks/Potion Shrine Icons").mkdirs()) {
            try {
                FileWriter packMcMeta = new FileWriter("resourcepacks/Potion Shrine Icons/pack.mcmeta");
                packMcMeta.write("""
                        {
                          "pack": {
                            "pack_format": 9,
                            "description": "Example resource pack for custom Potion Shrine icons"
                          }
                        }""");
                packMcMeta.close();
            } catch (Exception ignored) {}
            try {
                FileWriter readMe = new FileWriter("resourcepacks/Potion Shrine Icons/README.txt");
                readMe.write("""
                        This pack is structured as follows:
                        [Potion Shrine Icons]
                           -[assets]
                              -[potion_shrines]
                                 -[textures]
                                 -[models]
                           - pack.mcmeta

                        Follow these steps to add a custom icon that can then be used* for shrines in the config:
                        Step 1: In the "models" folder, create a json file with the same name as the icon png (i.e. - "example.png" and "example.json") [MUST BE SAME NAME]
                            the json file should look like this:
                                {
                                   "parent":  "item/generated",
                                   "textures": {
                                      "layer0": "potion_shrines:example"
                                   }
                                }
                                
                        Step 2: In the "textures" folder put the desired png for the custom icon (nothing more than 64x64 is recommended)

                        It should look like this when you're done:
                        [Potion Shrine Icons]
                           -[assets]
                              -[potion_shrines]
                                 -[models]
                                    - example.json
                                 -[textures]
                                    - example.png
                           - pack.mcmeta

                        *In order for these to be usable the game MUST be restarted.
                        """);
                readMe.close();
            } catch (Exception ignored) {}
            if (new File("resourcepacks/Potion Shrine Icons/assets/potion_shrines/models").mkdirs()) {
                try {
                    FileWriter exampleModelJson = new FileWriter("resourcepacks/Potion Shrine Icons/assets/potion_shrines/models/example.json");
                    exampleModelJson.write("""
                            {
                              "parent":  "item/generated",
                              "textures": {
                                "layer0": "potion_shrines:example"
                              }
                            }""");
                    exampleModelJson.close();
                } catch (Exception ignored) {
                }
            }
            if (new File("resourcepacks/Potion Shrine Icons/assets/potion_shrines/textures").mkdirs()){
                BufferedImage image = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g2d = image.createGraphics();

                // Fill the entire image with transparent pixels
                g2d.setColor(new Color(0, 0, 0, 0));
                g2d.fillRect(0, 0, 16, 16);

                g2d.setColor(new Color(255, 233, 39, 255));
                g2d.fillRect(8, 2, 1, 1);
                g2d.fillRect(9, 2, 1, 1);
                g2d.fillRect(8, 4, 1, 1);
                g2d.fillRect(9, 4, 1, 1);
                g2d.fillRect(8, 5, 1, 1);
                g2d.fillRect(9, 5, 1, 1);
                g2d.fillRect(8, 7, 1, 1);
                g2d.fillRect(9, 7, 1, 1);
                g2d.fillRect(2, 8, 1, 1);
                g2d.fillRect(3, 8, 1, 1);
                g2d.fillRect(4, 8, 1, 1);
                g2d.fillRect(5, 8, 1, 1);
                g2d.fillRect(7, 8, 1, 1);
                g2d.fillRect(8, 8, 1, 1);
                g2d.fillRect(9, 8, 1, 1);
                g2d.fillRect(10, 8, 1, 1);
                g2d.fillRect(12, 8, 1, 1);
                g2d.fillRect(13, 8, 1, 1);
                g2d.fillRect(14, 8, 1, 1);
                g2d.fillRect(15, 8, 1, 1);
                g2d.fillRect(2, 9, 1, 1);
                g2d.fillRect(3, 9, 1, 1);
                g2d.fillRect(4, 9, 1, 1);
                g2d.fillRect(5, 9, 1, 1);
                g2d.fillRect(7, 9, 1, 1);
                g2d.fillRect(8, 9, 1, 1);
                g2d.fillRect(9, 9, 1, 1);
                g2d.fillRect(10, 9, 1, 1);
                g2d.fillRect(12, 9, 1, 1);
                g2d.fillRect(13, 9, 1, 1);
                g2d.fillRect(14, 9, 1, 1);
                g2d.fillRect(15, 9, 1, 1);
                g2d.fillRect(8, 10, 1, 1);
                g2d.fillRect(9, 10, 1, 1);
                g2d.fillRect(8, 12, 1, 1);
                g2d.fillRect(9, 12, 1, 1);
                g2d.fillRect(8, 13, 1, 1);
                g2d.fillRect(9, 13, 1, 1);
                g2d.fillRect(8, 15, 1, 1);
                g2d.fillRect(9, 15, 1, 1);

                g2d.setColor(new Color(201, 202, 169, 255));
                g2d.fillRect(10, 6, 1, 1);
                g2d.fillRect(14, 6, 1, 1);
                g2d.fillRect(3, 7, 1, 1);
                g2d.fillRect(6, 7, 1, 1);
                g2d.fillRect(6, 3, 1, 1);
                g2d.fillRect(7, 3, 1, 1);
                g2d.fillRect(8, 3, 1, 1);
                g2d.fillRect(9, 3, 1, 1);
                g2d.fillRect(10, 3, 1, 1);
                g2d.fillRect(11, 3, 1, 1);
                g2d.fillRect(5, 4, 1, 1);
                g2d.fillRect(12, 4, 1, 1);
                g2d.fillRect(4, 5, 1, 1);
                g2d.fillRect(13, 5, 1, 1);
                g2d.fillRect(3, 6, 1, 1);
                g2d.fillRect(7, 6, 1, 1);
                g2d.fillRect(11, 7, 1, 1);
                g2d.fillRect(14, 7, 1, 1);
                g2d.fillRect(6, 8, 1, 1);
                g2d.fillRect(11, 8, 1, 1);
                g2d.fillRect(6, 9, 1, 1);
                g2d.fillRect(11, 9, 1, 1);
                g2d.fillRect(3, 10, 1, 1);
                g2d.fillRect(6, 10, 1, 1);
                g2d.fillRect(11, 10, 1, 1);
                g2d.fillRect(14, 10, 1, 1);
                g2d.fillRect(3, 11, 1, 1);
                g2d.fillRect(7, 11, 1, 1);
                g2d.fillRect(10, 11, 1, 1);
                g2d.fillRect(14, 11, 1, 1);
                g2d.fillRect(4, 12, 1, 1);
                g2d.fillRect(13, 12, 1, 1);
                g2d.fillRect(5, 13, 1, 1);
                g2d.fillRect(12, 13, 1, 1);
                g2d.fillRect(6, 14, 1, 1);
                g2d.fillRect(7, 14, 1, 1);
                g2d.fillRect(8, 14, 1, 1);
                g2d.fillRect(9, 14, 1, 1);
                g2d.fillRect(10, 14, 1, 1);
                g2d.fillRect(11, 14, 1, 1);

                g2d.setColor(new Color(235, 213, 19, 255));
                g2d.fillRect(8, 6, 1, 1);
                g2d.fillRect(9, 6, 1, 1);
                g2d.fillRect(8, 11, 1, 1);
                g2d.fillRect(9, 11, 1, 1);

                g2d.dispose();

                try {
                    File outputFile = new File("resourcepacks/Potion Shrine Icons/assets/potion_shrines/textures/example.png");
                    ImageIO.write(image, "png", outputFile);
                } catch (IOException e) {
                    System.err.println("Error saving image: " + e.getMessage());
                }
            }
        }
    }
}
