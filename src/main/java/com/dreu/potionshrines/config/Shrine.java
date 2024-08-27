package com.dreu.potionshrines.config;

import com.electronwill.nightconfig.core.Config;
import com.electronwill.nightconfig.toml.TomlParser;
import org.apache.commons.lang3.tuple.Pair;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.dreu.potionshrines.PotionShrines.rand;
import static com.dreu.potionshrines.config.AoEShrine.AOE_SHRINES;
import static com.dreu.potionshrines.config.AoEShrine.TOTAL_WEIGHT_AOE;

public class Shrine extends PSConfig{
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
            Duration = 180
            Cooldown = 180
            Replenish = true
            Amplifier = 2
            Weight = 5
            Icon = "regeneration"
            
            [[Shrine]]
            Effect = "minecraft:health_boost"
            Duration = 300
            Cooldown = 300
            Replenish = true
            Amplifier = 2
            Weight = 5
            Icon = "health_boost"
            
            [[Shrine]]
            Effect = "minecraft:health_boost"
            Duration = 300
            Cooldown = 300
            Replenish = true
            Amplifier = 3
            Weight = 3
            Icon = "health_boost"
            
            [[Shrine]]
            Effect = "minecraft:health_boost"
            Duration = 60
            Cooldown = 60
            Replenish = true
            Amplifier = 5
            Weight = 1
            Icon = "health_boost"
            
            [[Shrine]]
            Effect = "minecraft:absorption"
            Duration = 300
            Cooldown = 300
            Replenish = true
            Amplifier = 2
            Weight = 5
            Icon = "absorption"
            
            [[Shrine]]
            Effect = "minecraft:absorption"
            Duration = 300
            Cooldown = 300
            Replenish = true
            Amplifier = 3
            Weight = 3
            Icon = "absorption"
            
            [[Shrine]]
            Effect = "minecraft:absorption"
            Duration = 60
            Cooldown = 60
            Replenish = true
            Amplifier = 5
            Weight = 1
            Icon = "absorption"
            
            [[Shrine]]
            Effect = "minecraft:jump_boost"
            Duration = 300
            Cooldown = 300
            Replenish = true
            Amplifier = 2
            Weight = 5
            Icon = "jump_boost"
            
            [[Shrine]]
            Effect = "minecraft:strength"
            Duration = 300
            Cooldown = 300
            Replenish = true
            Amplifier = 1
            Weight = 5
            Icon = "strength"
            
            [[Shrine]]
            Effect = "minecraft:strength"
            Duration = 180
            Cooldown = 180
            Replenish = true
            Amplifier = 2
            Weight = 3
            Icon = "strength"
            
            [[Shrine]]
            Effect = "minecraft:strength"
            Duration = 60
            Cooldown = 60
            Replenish = true
            Amplifier = 3
            Weight = 1
            Icon = "strength"
            
            [[Shrine]]
            Effect = "minecraft:invisibility"
            Duration = 300
            Cooldown = 300
            Replenish = true
            Amplifier = 1
            Weight = 5
            Icon = "invisibility"
            
            [[Shrine]]
            Effect = "minecraft:fire_resistance"
            Duration = 300
            Cooldown = 300
            Replenish = true
            Amplifier = 1
            Weight = 5
            Icon = "fire_resistance"
            
            [[Shrine]]
            Effect = "minecraft:speed"
            Duration = 300
            Cooldown = 300
            Replenish = true
            Amplifier = 1
            Weight = 5
            Icon = "speed"
            
            [[Shrine]]
            Effect = "minecraft:speed"
            Duration = 180
            Cooldown = 180
            Replenish = true
            Amplifier = 2
            Weight = 3
            Icon = "speed"
            
            [[Shrine]]
            Effect = "minecraft:speed"
            Duration = 60
            Cooldown = 60
            Replenish = true
            Amplifier = 3
            Weight = 1
            Icon = "speed"
            
            [[Shrine]]
            Effect = "minecraft:haste"
            Duration = 300
            Cooldown = 300
            Replenish = true
            Amplifier = 1
            Weight = 5
            Icon = "haste"
            
            [[Shrine]]
            Effect = "minecraft:haste"
            Duration = 180
            Cooldown = 180
            Replenish = true
            Amplifier = 2
            Weight = 3
            Icon = "haste"
            
            [[Shrine]]
            Effect = "minecraft:resistance"
            Duration = 180
            Cooldown = 180
            Replenish = true
            Amplifier = 1
            Weight = 3
            Icon = "resistance"
            
            [[Shrine]]
            Effect = "minecraft:resistance"
            Duration = 60
            Cooldown = 60
            Replenish = true
            Amplifier = 2
            Weight = 1
            Icon = "resistance"
            
            [[Shrine]]
            Effect = "minecraft:water_breathing"
            Duration = 300
            Cooldown = 300
            Replenish = true
            Amplifier = 1
            Weight = 5
            Icon = "water_breathing"
            
            [[Shrine]]
            Effect = "minecraft:resistance"
            Duration = 60
            Cooldown = 60
            Replenish = true
            Amplifier = 2
            Weight = 1
            Icon = "resistance"
            
            [[Shrine]]
            Effect = "minecraft:resistance"
            Duration = 60
            Cooldown = 60
            Replenish = true
            Amplifier = 2
            Weight = 1
            Icon = "resistance"
            
            [[Shrine]]
            Effect = "minecraft:slow_falling"
            Duration = 120
            Cooldown = 120
            Replenish = true
            Amplifier = 1
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
