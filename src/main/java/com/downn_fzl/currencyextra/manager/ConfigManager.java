package com.downn_fzl.currencyextra.manager;

import com.downn_fzl.currencyextra.CurrencyExtra;
import com.google.common.io.ByteStreams;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;

public class ConfigManager {

    private FileConfiguration config;
    private FileConfiguration currency;

    public void reloadAll() {

        CurrencyExtra.instance.reloadConfig();

        config = CurrencyExtra.instance.getConfig();
        currency = YamlConfiguration.loadConfiguration(loadResource(CurrencyExtra.instance, "currency.yml"));
    }

    public FileConfiguration getConfig() {
        return config;
    }

    public FileConfiguration getCurrency() {
        return currency;
    }

    private static File loadResource(Plugin plugin, String resource) {
        File folder = plugin.getDataFolder();
        if (!folder.exists()) folder.mkdir();
        File resourceFile = new File(folder, resource);
        try {
            if (!resourceFile.exists()) {
                resourceFile.createNewFile();
                try (InputStream in = plugin.getResource(resource);
                     OutputStream out = Files.newOutputStream(resourceFile.toPath())) {
                    assert in != null;
                    ByteStreams.copy(in, out);
                }
            }
        } catch (Exception e) { e.printStackTrace();
            e.printStackTrace();
        }
        return resourceFile;
    }
}
