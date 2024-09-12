package com.dreu.potionshrines.config;

import com.electronwill.nightconfig.core.Config;
import com.electronwill.nightconfig.toml.TomlParser;
import net.minecraft.util.Mth;
import org.apache.commons.lang3.tuple.Pair;

public class General extends PSConfig {
    static final String defaultConfig = """
            # Can the Shrines replenish? (DEFAULT = true)
            Replenish = true
            # Simple shrine rarity (Range: 1 - 10000) (DEFAULT = 400)
            SimpleShrineRarity = 400
            # AoE shrine rarity (Range: 1 - 10000) (DEFAULT = 400)
            AoEShrineRarity = 400
            # Are Shrines indestructible? (DEFAULT = true)
            Indestructible = true
            # Can Shrines be obtained when destroyed? (DEFAULT = false)
            Obtainable = false
            # Percent Bonus per level of XP effect (Range: 1 - 10000) (DEFAULT = 10)
            BonusXP = 10
            """;

    private static final Pair<Config, String> CONFIG = getConfigOrDefault("general", defaultConfig);
    private static final Config CONFIG_DEFAULT = new TomlParser().parse(defaultConfig);
    public static final Boolean SHRINES_REPLENISH = getBooleanOrDefault("Replenish", CONFIG, CONFIG_DEFAULT);
    public static final int SHRINE_RARITY = Mth.clamp(getIntOrDefault("SimpleShrineRarity", CONFIG, CONFIG_DEFAULT), 1, 10000);
    public static final int AOE_SHRINE_RARITY = Mth.clamp(getIntOrDefault("AoEShrineRarity", CONFIG, CONFIG_DEFAULT), 1, 10000);
    public static final boolean SHRINE_INDESTRUCTIBLE = getBooleanOrDefault("Indestructible", CONFIG, CONFIG_DEFAULT);
    public static final boolean OBTAINABLE = getBooleanOrDefault("Obtainable", CONFIG, CONFIG_DEFAULT);
    public static final float XP_BONUS = Mth.clamp(getIntOrDefault("BonusXP", CONFIG, CONFIG_DEFAULT), 1, 10000) * 0.01F;
}
