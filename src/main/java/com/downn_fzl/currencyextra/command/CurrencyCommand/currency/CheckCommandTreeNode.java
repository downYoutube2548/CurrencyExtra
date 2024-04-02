package com.downn_fzl.currencyextra.command.CurrencyCommand.currency;

import com.downn_fzl.currencyextra.CurrencyExtra;
import com.downn_fzl.currencyextra.command.CommandTreeNode;
import com.downn_fzl.currencyextra.manager.Currency;
import com.downn_fzl.currencyextra.manager.CurrencyManager;
import com.downn_fzl.currencyextra.manager.PlayerData;
import com.downn_fzl.currencyextra.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class CheckCommandTreeNode extends CommandTreeNode {
    public CheckCommandTreeNode(CommandTreeNode parent) {
        super(parent, "check");
    }

    @Override
    public void execute(CommandSender sender, String command, String[] args) {

        if (args.length >= 1) {
            Player target = Bukkit.getPlayer(args[0]);
            Currency currency = CurrencyManager.get(command.toUpperCase());

            if (target != null) {

                PlayerData playerData = PlayerData.get(target.getUniqueId());
                sender.sendMessage(Utils.getMessage("balance-other", true)
                        .replace("{target}", args[0])
                        .replace("{amount}", Utils.formatNumber(playerData.getBalance(currency)))
                        .replace("{symbol}", currency.getSymbol())
                        .replace("{currency}", currency.getDisplayName()));

            } else {
                Bukkit.getScheduler().runTaskAsynchronously(CurrencyExtra.instance, () -> {
                    OfflinePlayer player = Bukkit.getOfflinePlayer(args[0]);
                    if (player.hasPlayedBefore()) {

                        PlayerData.getSavedData(player.getUniqueId(), playerData -> sender.sendMessage(Utils.getMessage("balance-other", true)
                                .replace("{target}", args[0])
                                .replace("{amount}", Utils.formatNumber(playerData.getBalance(currency)))
                                .replace("{symbol}", currency.getSymbol())
                                .replace("{currency}", currency.getDisplayName())
                        ));

                    } else {
                        sender.sendMessage(Utils.getMessage("invalid-player", true)
                                .replace("{player}", args[0])
                        );
                    }
                });
            }
        } else {
            sender.sendMessage(Utils.getMessage("invalid-syntax", true));
            sender.sendMessage(Utils.getMessage("usage", true)
                    .replace("{command}", getCurrentCommand(command) + " <target>")
            );
        }
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String command, String[] args) {
        List<String> output = new ArrayList<>();
        if (args.length == 1) {
            List<String> players = new ArrayList<>(Bukkit.getOnlinePlayers().stream().map(Player::getName).toList());
            output = Utils.tabComplete(args[0], players);
        }
        return output;
    }
}
