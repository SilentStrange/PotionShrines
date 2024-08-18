package com.dreu.potionshrines.config;

import com.electronwill.nightconfig.core.Config;
import com.electronwill.nightconfig.toml.TomlParser;
import org.apache.commons.lang3.tuple.Pair;

public class PSGeneralConfig extends PSConfig {
    static final String defaultConfig = """
            # Can the Shrines replenish? (DEFAULT = true)
            Replenish = true
            # All players share shrine cooldowns? (DEFAULT = true)
            GlobalCooldowns = true
            # Shrine rarity (Range: 1 - 10000) (DEFAULT = 400)
            Rarity = 400
            """;

    private static final Pair<Config, String> CONFIG = getConfigOrDefault("shrines", defaultConfig);
    private static final Config CONFIG_DEFAULT = new TomlParser().parse(defaultConfig);
    private static final Boolean SHRINES_REPLENISH = getBooleanOrDefault("Replenish", CONFIG, CONFIG_DEFAULT);
    private static final Boolean GLOBAL_COOLDOWN = getBooleanOrDefault("GlobalCooldowns", CONFIG, CONFIG_DEFAULT);
    private static final int SHRINE_RARITY = rangeBounded(getIntOrDefault("Rarity", CONFIG, CONFIG_DEFAULT), 1, 10000);
}
