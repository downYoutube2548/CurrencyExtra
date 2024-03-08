package com.downn_fzl.currencyextra.database;

import com.downn_fzl.currencyextra.CurrencyExtra;
import org.bukkit.Bukkit;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatabaseTimeOutFix {

    public static void start() {
        Bukkit.getScheduler().runTaskTimerAsynchronously(CurrencyExtra.instance, () -> {
            try {
                PreparedStatement statement = CurrencyExtra.databaseManager.getConnection().prepareStatement("SELECT * FROM currencyextra_logs;");
                statement.executeQuery();
            } catch (SQLException ignored) {
            }
        }, 0, 6000);
    }
}

