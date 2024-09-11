package com.dreu.potionshrines.config;

import com.electronwill.nightconfig.core.Config;
import com.electronwill.nightconfig.toml.TomlParser;
import org.apache.commons.lang3.tuple.Pair;

import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;

import static com.dreu.potionshrines.PotionShrines.LOGGER;
import static com.dreu.potionshrines.PotionShrines.MODID;

public class PSConfig {

    static Pair<Config, String> getConfigOrDefault(String name, String defaultConfig) {
        try {Files.createDirectories(Path.of("config/" + MODID));} catch (Exception ignored) {}
        return Pair.of(new TomlParser().parse(Path.of("config/" + MODID + "/"+ name +".toml").toAbsolutePath(),
                ((path, configFormat) -> {
                    FileWriter writer = new FileWriter(path.toFile().getAbsolutePath());
                    writer.write(defaultConfig);
                    writer.close();
                    return true;})), "config/" + MODID + "/"+ name +".toml");
    }
    static int getIntOrDefault(String key, Pair<Config, String> config, Config defaultConfig) {
        try {
            if ((config.getLeft().get(key) == null)) {
                LOGGER.error("Key [" + key + "] is missing from Config: " + config.getRight());
                return defaultConfig.get(key);
            }
            return config.getLeft().get(key);
        } catch (Exception e) {
            LOGGER.error("Value for [" + key + "] is an invalid type in Config: " + config.getRight());
            return defaultConfig.get(key);
        }
    }
    static boolean getBooleanOrDefault(String key, Pair<Config, String> config, Config defaultConfig) {
        try {
            if ((config.getLeft().get(key) == null)) {
                LOGGER.error("Key [" + key + "] is missing from Config: " + config.getRight());
                return defaultConfig.get(key);
            }
            return config.getLeft().get(key);
        } catch (Exception e) {
            LOGGER.error("Value for [" + key + "] is an invalid type in Config: " + config.getRight());
            return defaultConfig.get(key);
        }
    }
    public static int rangeBounded(int i, int min, int max){
        return Math.max(min, Math.min(i, max));
    }
}
