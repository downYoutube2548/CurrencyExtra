package com.downn_fzl.currencyextra.utils;

import com.downn_fzl.currencyextra.CurrencyExtra;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

    public static String colorize(String s) {
        if (s == null || s.equals(""))
            return "";
        Pattern pattern = Pattern.compile("#[a-fA-F0-9]{6}");
        Matcher match = pattern.matcher(s);
        while (match.find()) {
            String hexColor = s.substring(match.start(), match.end());
            s = s.replace(hexColor, net.md_5.bungee.api.ChatColor.of(hexColor).toString());
            match = pattern.matcher(s);
        }

        return net.md_5.bungee.api.ChatColor.translateAlternateColorCodes('&', s);
    }

    public static String getMessage(String path, boolean prefix) {
        return Utils.colorize((prefix ? CurrencyExtra.configManager.getConfig().getString("message.prefix") + " " : "") + CurrencyExtra.configManager.getConfig().getString("message."+path));
    }

    public static String formatNumber(Double input) {
        DecimalFormat df = new DecimalFormat("#,###.##");
        return df.format(input);
    }

    public static List<String> tabComplete(String a, List<String> arg) {
        List<String> matches = new ArrayList<>();
        String search = a.toLowerCase();
        for (String s : arg) {
            if (s.toLowerCase().startsWith(search)) {
                matches.add(s);
            }
        }
        return matches;
    }

}
