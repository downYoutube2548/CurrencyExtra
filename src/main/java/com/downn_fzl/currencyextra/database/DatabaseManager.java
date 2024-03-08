package com.downn_fzl.currencyextra.database;

import com.downn_fzl.currencyextra.CurrencyExtra;
import com.downn_fzl.currencyextra.manager.Currency;
import com.downn_fzl.currencyextra.manager.PlayerData;
import com.downn_fzl.currencyextra.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.sql.*;
import java.util.HashMap;
import java.util.UUID;
import java.util.function.Consumer;

public class DatabaseManager {
    private final String HOST = CurrencyExtra.configManager.getConfig().getString("mysql.host");
    private final String USER = CurrencyExtra.configManager.getConfig().getString("mysql.user");
    private final String PASSWORD = CurrencyExtra.configManager.getConfig().getString("mysql.password");
    private final String PORT = CurrencyExtra.configManager.getConfig().getString("mysql.port");
    private final String DATABASE = CurrencyExtra.configManager.getConfig().getString("mysql.database");
    private Connection connection;

    public void connect() {
        try {
            this.connection = DriverManager.getConnection("jdbc:mysql://" + this.HOST + ":" + this.PORT + "/" + this.DATABASE + "?autoReconnect=true&useSSL=" +
                    CurrencyExtra.instance.getConfig().getString("mysql.useSSL"), this.USER, this.PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        try {
            if (this.connection.isClosed()) {
                System.out.println("Reconnecting to Database...");
                connect();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return this.connection;
    }

    public void createTableIfNotExist() {

        try {
            Statement statement = getConnection().createStatement();
            for (String currency : CurrencyExtra.currencyManager.currencies.keySet())
                statement.executeUpdate("CREATE TABLE IF NOT EXISTS currencyextra_currency_"+currency.toLowerCase()+" (ID int NOT NULL AUTO_INCREMENT, UUID VARCHAR(40), BALANCE DOUBLE(20, 4), MAX_BALANCE DOUBLE(20, 4), PRIMARY KEY (ID));");

        } catch (SQLException | NullPointerException e) {
            System.out.println("" + ChatColor.RED + "Cannot connect to database! Disabling Plugin!");
            e.printStackTrace();
            Bukkit.getPluginManager().disablePlugin(CurrencyExtra.instance);
        }
    }

    public void Disconnect() {
        try {
            this.connection.close();
        } catch (SQLException ignored) {
        }
    }

    public void load(Player player) {

        Bukkit.getScheduler().runTaskAsynchronously(CurrencyExtra.instance, ()->{

            long before = System.currentTimeMillis();

            HashMap<Currency, Double> balances = new HashMap<>();
            HashMap<Currency, Double> maxBalances = new HashMap<>();

            try {

                for (Currency currency : CurrencyExtra.currencyManager.currencies.values()) {

                    String table = "currencyextra_currency_"+currency.getId().toLowerCase();

                    PreparedStatement addDataStatement = getConnection().prepareStatement(
                            "INSERT INTO "+ table +" (UUID, BALANCE, MAX_BALANCE)" +
                                    "    SELECT ?, ?, ?" +
                                    "    WHERE NOT EXISTS (SELECT * FROM "+ table +" WHERE UUID = ?);"
                    );

                    addDataStatement.setString(1, player.getUniqueId().toString());
                    addDataStatement.setDouble(2, currency.getStartingBalance());
                    addDataStatement.setDouble(3, currency.getDefaultMaxAmount());
                    addDataStatement.setString(4, player.getUniqueId().toString());
                    addDataStatement.executeUpdate();
                    addDataStatement.close();

                    PreparedStatement selectStatement = getConnection().prepareStatement("SELECT * FROM "+ table +" WHERE UUID = ?");

                    selectStatement.setString(1, player.getUniqueId().toString());
                    ResultSet resultSet = selectStatement.executeQuery();
                    if (resultSet.next()) {
                        balances.put(currency, resultSet.getDouble("BALANCE"));
                        maxBalances.put(currency, resultSet.getDouble("MAX_BALANCE"));
                    }
                    selectStatement.close();
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }

            CurrencyExtra.mapPlayerData.put(player.getUniqueId(), new PlayerData(player.getUniqueId(), balances, maxBalances));
            player.sendMessage(Utils.getMessage("load-data-success", true).replace("{duration}", String.valueOf(System.currentTimeMillis() - before)));

        });
    }

    public void loadTemp(UUID uuid, Consumer<PlayerData> consumer) {
        Bukkit.getScheduler().runTaskAsynchronously(CurrencyExtra.instance, ()->{

            HashMap<Currency, Double> balances = new HashMap<>();
            HashMap<Currency, Double> maxBalances = new HashMap<>();

            try {

                for (Currency currency : CurrencyExtra.currencyManager.currencies.values()) {

                    String table = "currencyextra_currency_"+currency.getId().toLowerCase();

                    PreparedStatement addDataStatement = getConnection().prepareStatement(
                            "INSERT INTO "+ table +" (UUID, BALANCE, MAX_BALANCE)" +
                                    "    SELECT ?, ?, ?" +
                                    "    WHERE NOT EXISTS (SELECT * FROM "+ table +" WHERE UUID = ?);"
                    );

                    addDataStatement.setString(1, uuid.toString());
                    addDataStatement.setDouble(2, currency.getStartingBalance());
                    addDataStatement.setDouble(3, currency.getDefaultMaxAmount());
                    addDataStatement.setString(4, uuid.toString());
                    addDataStatement.executeUpdate();
                    addDataStatement.close();

                    PreparedStatement selectStatement = getConnection().prepareStatement("SELECT * FROM "+ table +" WHERE UUID = ?");

                    selectStatement.setString(1, uuid.toString());
                    ResultSet resultSet = selectStatement.executeQuery();
                    if (resultSet.next()) {
                        balances.put(currency, resultSet.getDouble("BALANCE"));
                        maxBalances.put(currency, resultSet.getDouble("MAX_BALANCE"));
                    }
                    selectStatement.close();

                }

            } catch (SQLException e) {
                e.printStackTrace();
            }

            Bukkit.getScheduler().runTask(CurrencyExtra.instance, () -> consumer.accept(new PlayerData(uuid, balances, maxBalances)));

        });
    }

    public void setBalance(UUID uuid, Currency currency, double value) {
        Bukkit.getScheduler().runTaskAsynchronously(CurrencyExtra.instance, () -> {
            try {
                Statement statement = getConnection().createStatement();
                statement.executeUpdate("UPDATE currencyextra_currency_"+currency.getId().toLowerCase()+" SET BALANCE = '" + value + "' WHERE UUID = '" + uuid + "';");
            } catch (SQLException ignored) {
            }
        });
    }

    public void setMaxBalance(UUID uuid, Currency currency, double value) {
        Bukkit.getScheduler().runTaskAsynchronously(CurrencyExtra.instance, () -> {
            try {
                Statement statement = getConnection().createStatement();
                statement.executeUpdate("UPDATE currencyextra_currency_"+currency.getId().toLowerCase()+" SET MAX_BALANCE = '" + value + "' WHERE UUID = '" + uuid + "';");
            } catch (SQLException ignored) {
            }
        });
    }
}

