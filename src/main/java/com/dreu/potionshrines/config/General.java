package com.dreu.potionshrines.config;

import com.electronwill.nightconfig.core.Config;
import com.electronwill.nightconfig.toml.TomlParser;
import net.minecraft.util.Mth;
import org.apache.commons.lang3.tuple.Pair;

public class General extends PSConfig {
    static final String defaultConfig = """
            # Can the Shrines replenish? (DEFAULT = true)
            Replenish = true
            # Are Shrines indestructible? (DEFAULT = true)
            Indestructible = true
            # Can Shrines be obtained when destroyed? (DEFAULT = false)
            Obtainable = false
            # Percent Bonus per level of XP effect (Range: 1 - 10000) (DEFAULT = 10)
            BonusXP = 10
            
            # Shrine rarity (Range: 1 - 10000) (DEFAULT = 400)
            [Rarity]
            SimpleShrine = 400
            AoEShrine = 400
            AuraShrine = 400
            """;

    private static final Pair<Config, String> CONFIG = getConfigOrDefault("general", defaultConfig);
    private static final Config CONFIG_DEFAULT = new TomlParser().parse(defaultConfig);
    public static final Boolean SHRINES_REPLENISH = getBooleanOrDefault("Replenish", CONFIG, CONFIG_DEFAULT);
    public static final int SIMPLE_SHRINE_RARITY = Mth.clamp(getIntOrDefault("Rarity.SimpleShrine", CONFIG, CONFIG_DEFAULT), 1, 10000);
    public static final int AOE_SHRINE_RARITY = Mth.clamp(getIntOrDefault("Rarity.AoEShrine", CONFIG, CONFIG_DEFAULT), 1, 10000);
    public static final int AURA_SHRINE_RARITY = Mth.clamp(getIntOrDefault("Rarity.AuraShrine", CONFIG, CONFIG_DEFAULT), 1, 10000);
    public static final boolean SHRINE_INDESTRUCTIBLE = getBooleanOrDefault("Indestructible", CONFIG, CONFIG_DEFAULT);
    public static final boolean OBTAINABLE = getBooleanOrDefault("Obtainable", CONFIG, CONFIG_DEFAULT);
    public static final float XP_BONUS = Mth.clamp(getIntOrDefault("BonusXP", CONFIG, CONFIG_DEFAULT), 1, 10000) * 0.01F;
}
