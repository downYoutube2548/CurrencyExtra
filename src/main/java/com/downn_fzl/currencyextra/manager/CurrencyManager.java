package com.downn_fzl.currencyextra.manager;

import com.downn_fzl.currencyextra.CurrencyExtra;
import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;

public class CurrencyManager {

    public static Currency get(String id) { return CurrencyExtra.currencyManager.currencies.get(id); }

    public HashMap<String, Currency> currencies = new HashMap<>();

    public void registerCurrency(Currency currency) {
        currencies.put(currency.getId(), currency);
    }

    public void unregister(String id) {
        currencies.remove(id);
    }

    public void registerDefault() {
        for (String currencyID : CurrencyExtra.configManager.getCurrency().getKeys(false)) {

            ConfigurationSection currencyConfig = CurrencyExtra.configManager.getCurrency().getConfigurationSection(currencyID);

            assert currencyConfig != null;
            registerCurrency(new Currency(currencyID,
                    currencyConfig.getString("currency-symbol"),
                    currencyConfig.getString("display-name"),
                    currencyConfig.getString("display-item"),
                    currencyConfig.getDouble("starting-balance"),
                    currencyConfig.getDouble("min-amount"),
                    currencyConfig.getDouble("max-amount"),
                    currencyConfig.getDouble("minimum-pay-amount"),
                    currencyConfig.getBoolean("pay")
                    ));
        }
    }
}
