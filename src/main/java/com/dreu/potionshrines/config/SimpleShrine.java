package com.dreu.potionshrines.config;

import com.electronwill.nightconfig.core.Config;
import com.electronwill.nightconfig.toml.TomlParser;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

import static com.dreu.potionshrines.PotionShrines.rand;

public class SimpleShrine extends PSConfig{
    static final String defaultConfig =
            """
            # If you want to reset this config to default, delete this file from the config folder and run the game
            
            # Effect = ID of the Potion Effect
            # Duration = How long the effect lasts in Seconds (Range: 1 - 999999)
            # Cooldown = How many seconds until players can use the Shrine again. -1 means Shrine will never replenish (Range: 3 - 999999)
            # Replenish = Will the shrine replenish
            # Amplifier = Level of the effect (Range: 1 - 256)
            # Weight = Chance for effect to be chosen compared to the Weight of all effects (Range: 1 - 2,147,483,647)
            # Icon = Symbol hovering above the shrine. Either use one that other shrines are using or
            #        go to the CurseForge page to see a detailed walkthrough on custom icons.
            
            [[Shrine]]
            Effect = "minecraft:regeneration"
            Amplifier = 2
            Duration = 180
            Cooldown = 180
            Replenish = true
            Weight = 5
            Icon = "regeneration"
            
            [[Shrine]]
            Effect = "minecraft:health_boost"
            Amplifier = 2
            Duration = 300
            Cooldown = 300
            Replenish = true
            Weight = 5
            Icon = "health_boost"
            
            [[Shrine]]
            Effect = "minecraft:health_boost"
            Amplifier = 3
            Duration = 300
            Cooldown = 300
            Replenish = true
            Weight = 3
            Icon = "health_boost"
            
            [[Shrine]]
            Effect = "minecraft:health_boost"
            Amplifier = 5
            Duration = 60
            Cooldown = 60
            Replenish = true
            Weight = 1
            Icon = "health_boost"
            
            [[Shrine]]
            Effect = "minecraft:absorption"
            Amplifier = 2
            Duration = 300
            Cooldown = 300
            Replenish = true
            Weight = 5
            Icon = "absorption"
            
            [[Shrine]]
            Effect = "minecraft:absorption"
            Amplifier = 3
            Duration = 300
            Cooldown = 300
            Replenish = true
            Weight = 3
            Icon = "absorption"
            
            [[Shrine]]
            Effect = "minecraft:absorption"
            Amplifier = 5
            Duration = 60
            Cooldown = 60
            Replenish = true
            Weight = 1
            Icon = "absorption"
            
            [[Shrine]]
            Effect = "minecraft:jump_boost"
            Amplifier = 2
            Duration = 300
            Cooldown = 300
            Replenish = true
            Weight = 5
            Icon = "jump_boost"
            
            [[Shrine]]
            Effect = "minecraft:strength"
            Amplifier = 1
            Duration = 300
            Cooldown = 300
            Replenish = true
            Weight = 5
            Icon = "strength"
            
            [[Shrine]]
            Effect = "minecraft:strength"
            Amplifier = 2
            Duration = 180
            Cooldown = 180
            Replenish = true
            Weight = 3
            Icon = "strength"
            
            [[Shrine]]
            Effect = "minecraft:strength"
            Amplifier = 3
            Duration = 60
            Cooldown = 60
            Replenish = true
            Weight = 1
            Icon = "strength"
            
            [[Shrine]]
            Effect = "minecraft:invisibility"
            Amplifier = 1
            Duration = 300
            Cooldown = 300
            Replenish = true
            Weight = 5
            Icon = "invisibility"
            
            [[Shrine]]
            Effect = "minecraft:fire_resistance"
            Amplifier = 1
            Duration = 300
            Cooldown = 300
            Replenish = true
            Weight = 5
            Icon = "fire_resistance"
            
            [[Shrine]]
            Effect = "minecraft:speed"
            Amplifier = 1
            Duration = 300
            Cooldown = 300
            Replenish = true
            Weight = 5
            Icon = "speed"
            
            [[Shrine]]
            Effect = "minecraft:speed"
            Amplifier = 2
            Duration = 180
            Cooldown = 180
            Replenish = true
            Weight = 3
            Icon = "speed"
            
            [[Shrine]]
            Effect = "minecraft:speed"
            Amplifier = 3
            Duration = 60
            Cooldown = 60
            Replenish = true
            Weight = 1
            Icon = "speed"
            
            [[Shrine]]
            Effect = "minecraft:haste"
            Amplifier = 1
            Duration = 300
            Cooldown = 300
            Replenish = true
            Weight = 5
            Icon = "haste"
            
            [[Shrine]]
            Effect = "minecraft:haste"
            Amplifier = 2
            Duration = 180
            Cooldown = 180
            Replenish = true
            Weight = 3
            Icon = "haste"
            
            [[Shrine]]
            Effect = "minecraft:resistance"
            Amplifier = 1
            Duration = 180
            Cooldown = 180
            Replenish = true
            Weight = 3
            Icon = "resistance"
            
            [[Shrine]]
            Effect = "minecraft:resistance"
            Amplifier = 2
            Duration = 60
            Cooldown = 60
            Replenish = true
            Weight = 1
            Icon = "resistance"
            
            [[Shrine]]
            Effect = "minecraft:water_breathing"
            Amplifier = 1
            Duration = 300
            Cooldown = 300
            Replenish = true
            Weight = 5
            Icon = "water_breathing"
            
            [[Shrine]]
            Effect = "minecraft:resistance"
            Amplifier = 2
            Duration = 60
            Cooldown = 60
            Replenish = true
            Weight = 1
            Icon = "resistance"
            
            [[Shrine]]
            Effect = "minecraft:resistance"
            Amplifier = 2
            Duration = 60
            Cooldown = 60
            Replenish = true
            Weight = 1
            Icon = "resistance"
            
            [[Shrine]]
            Effect = "minecraft:slow_falling"
            Amplifier = 1
            Duration = 120
            Cooldown = 120
            Replenish = true
            Weight = 5
            Icon = "slow_falling"
            """;
    private static final Pair<Config, String> CONFIG = getConfigOrDefault("shrines", defaultConfig);
    private static final Config CONFIG_DEFAULT = new TomlParser().parse(defaultConfig);
    public static final List<Config> SHRINES = CONFIG.getLeft().get("Shrine");
    public static double TOTAL_WEIGHT = 0;

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
