package com.downn_fzl.currencyextra;

import com.downn_fzl.currencyextra.command.CoreCommand;
import com.downn_fzl.currencyextra.command.CurrencyCommand.CurrencyCommand;
import com.downn_fzl.currencyextra.database.DatabaseManager;
import com.downn_fzl.currencyextra.database.DatabaseTimeOutFix;
import com.downn_fzl.currencyextra.event.DisconnectEvent;
import com.downn_fzl.currencyextra.event.JoinEvent;
import com.downn_fzl.currencyextra.manager.ConfigManager;
import com.downn_fzl.currencyextra.manager.CurrencyManager;
import com.downn_fzl.currencyextra.manager.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.util.*;

public final class CurrencyExtra extends JavaPlugin {

    public static HashMap<UUID, PlayerData> mapPlayerData = new HashMap<>();
    public static CurrencyManager currencyManager;
    public static CurrencyExtra instance;
    public static DatabaseManager databaseManager;
    public static ConfigManager configManager;

    @Override
    public void onEnable() {
        // Plugin startup logic

        instance = this;

        getConfig().options().copyDefaults();
        saveDefaultConfig();

        configManager = new ConfigManager();
        configManager.reloadAll();

        currencyManager = new CurrencyManager();
        currencyManager.registerDefault();

        databaseManager = new DatabaseManager();
        databaseManager.connect();
        databaseManager.createTableIfNotExist();

        DatabaseTimeOutFix.start();

        Objects.requireNonNull(getCommand("currencyextra")).setExecutor(new CoreCommand());
        getServer().getPluginManager().registerEvents(new JoinEvent(), this);
        getServer().getPluginManager().registerEvents(new DisconnectEvent(), this);

        for (String currency : currencyManager.currencies.keySet()) {
            CurrencyCommand command = new CurrencyCommand(currency.toLowerCase());

            getCommandMap().register(getName(), command);
        }

        for (Player player : Bukkit.getOnlinePlayers()) {
            databaseManager.load(player);
        }
    }

    @Override
    public void onDisable() {
        SimpleCommandMap commandMap = getCommandMap();
        Map<String, Command> knownCommands;
        try {
            Field f = SimpleCommandMap.class.getDeclaredField("knownCommands");
            f.setAccessible(true);
            knownCommands = (Map<String, Command>) f.get(commandMap);
        } catch (Throwable e) {
            throw new RuntimeException();
        }

        knownCommands.entrySet().removeIf(entry -> entry.getValue() instanceof CurrencyCommand);
    }



    public SimpleCommandMap getCommandMap() {
        SimpleCommandMap commandMap = null;

        try {
            Field f = Bukkit.getPluginManager().getClass().getDeclaredField("commandMap");
            f.setAccessible(true);

            commandMap = (SimpleCommandMap) f.get(Bukkit.getPluginManager());
        } catch (NoSuchFieldException | IllegalAccessException | IllegalArgumentException | SecurityException e) {
            e.printStackTrace();
        }

        return commandMap;
    }
}
