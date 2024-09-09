package com.dreu.potionshrines.config;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class ExampleResourcePack {
    public static void generate() {
        File baseDir = new File("resourcepacks/Potion Shrine Icons");
        if (baseDir.mkdirs()) {
            try {
                // Write pack.mcmeta
                Files.writeString(Paths.get(baseDir.getPath(), "pack.mcmeta"),
                        """
                                {
                                  "pack": {
                                    "pack_format": 9,
                                    "description": "Example resource pack for custom Potion Shrine icons"
                                  }
                                }""", StandardOpenOption.CREATE);

                // Write README.txt
                Files.writeString(Paths.get(baseDir.getPath(), "README.txt"),
                        """
                                This pack is structured as follows:
                                [Potion Shrine Icons]
                                   -[assets]
                                      -[potion_shrines]
                                         -[models]
                                            -[icon]
                                               -default.json
                                         -[textures]
                                            -[icon]
                                               -default.png
                                               ~add your icon png here~
                                   - pack.mcmeta

                                In the "textures/icon" folder put the desired png for the custom icon (nothing more than 64x64 is recommended)
                                *In order for these to be usable the game MUST be restarted.
                                """, StandardOpenOption.CREATE);

                File textureDir = new File("resourcepacks/Potion Shrine Icons/assets/potion_shrines/textures/icon");
                if (textureDir.mkdirs()) {
                    BufferedImage image = new BufferedImage(18, 18, BufferedImage.TYPE_INT_ARGB);
                    Graphics2D g2d = image.createGraphics();

                    Color[] colors = {
                            new Color(255, 233, 39, 255),
                            new Color(201, 202, 169, 255),
                            new Color(221, 222, 189, 255),
                            new Color(235, 213, 19, 255)
                    };

                    int[][] rectangles = {
                            {8, 2, 1, 1, 0}, {9, 2, 1, 1, 0},
                            {6, 3, 1, 1, 1}, {7, 3, 1, 1, 1},
                            {8, 3, 1, 1, 2}, {9, 3, 1, 1, 2},
                            {10, 3, 1, 1, 1}, {11, 3, 1, 1, 1},
                            {5, 4, 1, 1, 1}, {8, 4, 1, 1, 0},
                            {9, 4, 1, 1, 0}, {12, 4, 1, 1, 1},
                            {4, 5, 1, 1, 1}, {8, 5, 1, 1, 0},
                            {9, 5, 1, 1, 0}, {13, 5, 1, 1, 1},
                            {3, 6, 1, 1, 1}, {7, 6, 1, 1, 1},
                            {8, 6, 1, 1, 3}, {9, 6, 1, 1, 3},
                            {10, 6, 1, 1, 1}, {14, 6, 1, 1, 1},
                            {3, 7, 1, 1, 1}, {6, 7, 1, 1, 1},
                            {8, 7, 1, 1, 0}, {9, 7, 1, 1, 0},
                            {11, 7, 1, 1, 1}, {14, 7, 1, 1, 1},
                            {2, 8, 1, 1, 0}, {3, 8, 1, 1, 3},
                            {4, 8, 1, 1, 0}, {5, 8, 1, 1, 0},
                            {6, 8, 1, 1, 2}, {7, 8, 1, 1, 0},
                            {8, 8, 1, 1, 0}, {9, 8, 1, 1, 0},
                            {10, 8, 1, 1, 0}, {11, 8, 1, 1, 2},
                            {12, 8, 1, 1, 0}, {13, 8, 1, 1, 0},
                            {14, 8, 1, 1, 3}, {15, 8, 1, 1, 0},
                            {2, 9, 1, 1, 0}, {3, 9, 1, 1, 3},
                            {4, 9, 1, 1, 0}, {5, 9, 1, 1, 0},
                            {6, 9, 1, 1, 2}, {7, 9, 1, 1, 0},
                            {8, 9, 1, 1, 0}, {9, 9, 1, 1, 0},
                            {10, 9, 1, 1, 0}, {11, 9, 1, 1, 2},
                            {12, 9, 1, 1, 0}, {13, 9, 1, 1, 0},
                            {14, 9, 1, 1, 3}, {15, 9, 1, 1, 0},
                            {3, 10, 1, 1, 1}, {6, 10, 1, 1, 1},
                            {8, 10, 1, 1, 0}, {9, 10, 1, 1, 0},
                            {11, 10, 1, 1, 1}, {14, 10, 1, 1, 1},
                            {3, 11, 1, 1, 1}, {7, 11, 1, 1, 1},
                            {8, 11, 1, 1, 3}, {9, 11, 1, 1, 3},
                            {10, 11, 1, 1, 1}, {14, 11, 1, 1, 1},
                            {4, 12, 1, 1, 1}, {8, 12, 1, 1, 0},
                            {9, 12, 1, 1, 0}, {13, 12, 1, 1, 1},
                            {5, 13, 1, 1, 1}, {8, 13, 1, 1, 0},
                            {9, 13, 1, 1, 0}, {12, 13, 1, 1, 1},
                            {6, 14, 1, 1, 1}, {7, 14, 1, 1, 1},
                            {8, 14, 1, 1, 2}, {9, 14, 1, 1, 2},
                            {10, 14, 1, 1, 1}, {11, 14, 1, 1, 1},
                            {8, 15, 1, 1, 0}, {9, 15, 1, 1, 0}
                    };

                    for (int[] rect : rectangles) {
                        g2d.setColor(colors[rect[4]]);
                        g2d.fillRect(rect[0], rect[1], rect[2], rect[3]);
                    }

                    g2d.dispose();

                    // Save image
                    File outputFile = new File(textureDir, "default.png");
                    ImageIO.write(image, "png", outputFile);

                }
            } catch (IOException e) {
                System.err.println("Error creating files or saving image: " + e.getMessage());
            }
        }
    }
}
