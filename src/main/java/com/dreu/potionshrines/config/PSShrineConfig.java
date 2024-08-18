package com.dreu.potionshrines.config;

import com.electronwill.nightconfig.core.Config;
import com.electronwill.nightconfig.toml.TomlParser;
import org.apache.commons.lang3.tuple.Pair;

import java.nio.file.Path;
import java.util.List;

import static com.dreu.potionshrines.PotionShrines.rand;

public class PSShrineConfig extends PSConfig{
    static final String defaultConfig =
            """
            # If you want to reset this config to default, delete this file from the config folder and run the game
            
            # Effect = ID of the Potion Effect
            # Duration = How long the effect lasts in Seconds (Range: 1 - 999999)
            # Cooldown = How many seconds until players can use the Shrine again. 0 means Shrine will never replenish (Range: 0 - 999999)
            # Amplifier = Level of the effect (Range: 1 - 256)
            # Weight = Chance for effect to be chosen compared to the Weight of all effects (Range: 1 - 2,147,483,647)
            
            [[Shrine]]
            Effect = "minecraft:regeneration"
            Duration = 180
            Cooldown = 180
            Amplifier = 2
            Weight = 5
            
            [[Shrine]]
            Effect = "minecraft:health_boost"
            Duration = 300
            Cooldown = 300
            Amplifier = 2
            Weight = 5
            
            [[Shrine]]
            Effect = "minecraft:health_boost"
            Duration = 300
            Cooldown = 300
            Amplifier = 3
            Weight = 3
            
            [[Shrine]]
            Effect = "minecraft:health_boost"
            Duration = 60
            Cooldown = 60
            Amplifier = 5
            Weight = 1
            
            [[Shrine]]
            Effect = "minecraft:absorption"
            Duration = 300
            Cooldown = 300
            Amplifier = 2
            Weight = 5
            
            [[Shrine]]
            Effect = "minecraft:absorption"
            Duration = 300
            Cooldown = 300
            Amplifier = 3
            Weight = 3
            
            [[Shrine]]
            Effect = "minecraft:absorption"
            Duration = 60
            Cooldown = 60
            Amplifier = 5
            Weight = 1
            
            [[Shrine]]
            Effect = "minecraft:jump_boost"
            Duration = 300
            Cooldown = 300
            Amplifier = 2
            Weight = 5
            
            [[Shrine]]
            Effect = "minecraft:strength"
            Duration = 300
            Cooldown = 300
            Amplifier = 1
            Weight = 5
            
            [[Shrine]]
            Effect = "minecraft:strength"
            Duration = 180
            Cooldown = 180
            Amplifier = 2
            Weight = 3
            
            [[Shrine]]
            Effect = "minecraft:strength"
            Duration = 60
            Cooldown = 60
            Amplifier = 3
            Weight = 1
            
            [[Shrine]]
            Effect = "minecraft:invisibility"
            Duration = 300
            Cooldown = 300
            Amplifier = 1
            Weight = 5
            
            [[Shrine]]
            Effect = "minecraft:fire_resistance"
            Duration = 300
            Cooldown = 300
            Amplifier = 1
            Weight = 5
            
            [[Shrine]]
            Effect = "minecraft:speed"
            Duration = 300
            Cooldown = 300
            Amplifier = 1
            Weight = 5
            
            [[Shrine]]
            Effect = "minecraft:speed"
            Duration = 180
            Cooldown = 180
            Amplifier = 2
            Weight = 3
            
            [[Shrine]]
            Effect = "minecraft:speed"
            Duration = 60
            Cooldown = 60
            Amplifier = 3
            Weight = 1
            
            [[Shrine]]
            Effect = "minecraft:haste"
            Duration = 300
            Cooldown = 300
            Amplifier = 1
            Weight = 5
            
            [[Shrine]]
            Effect = "minecraft:haste"
            Duration = 180
            Cooldown = 180
            Amplifier = 2
            Weight = 3
            
            [[Shrine]]
            Effect = "minecraft:resistance"
            Duration = 180
            Cooldown = 180
            Amplifier = 1
            Weight = 3
            
            [[Shrine]]
            Effect = "minecraft:resistance"
            Duration = 60
            Cooldown = 60
            Amplifier = 2
            Weight = 1
            
            [[Shrine]]
            Effect = "minecraft:water_breathing"
            Duration = 300
            Cooldown = 300
            Amplifier = 1
            Weight = 5
            
            [[Shrine]]
            Effect = "minecraft:resistance"
            Duration = 60
            Cooldown = 60
            Amplifier = 2
            Weight = 1
            
            [[Shrine]]
            Effect = "minecraft:resistance"
            Duration = 60
            Cooldown = 60
            Amplifier = 2
            Weight = 1
            
            [[Shrine]]
            Effect = "minecraft:slow_falling"
            Duration = 120
            Cooldown = 120
            Amplifier = 1
            Weight = 5
            """;
    private static final Pair<Config, String> CONFIG = getConfigOrDefault("shrines", defaultConfig);
    private static final Config CONFIG_DEFAULT = new TomlParser().parse(defaultConfig);
    private static final List<Config> SHRINES = CONFIG.getLeft().get("Shrine");
    public static double TOTAL_WEIGHT = 0;
    static {
        SHRINES.forEach((shrine) -> {
            TOTAL_WEIGHT += shrine.getInt("Weight");
        });
    }
    public static Config getRandomShrine() {
        double randomWeight = rand.nextDouble(TOTAL_WEIGHT);
        double weightCount = 0;
        for (Config shrine : SHRINES){
            weightCount += shrine.getInt("Weight");
            if (weightCount > randomWeight) return shrine;
        }
        return SHRINES.get(0);
    }
}
