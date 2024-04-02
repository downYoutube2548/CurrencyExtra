package com.downn_fzl.currencyextra.command.CurrencyCommand.currency;

import com.downn_fzl.currencyextra.CurrencyExtra;
import com.downn_fzl.currencyextra.command.CommandTreeNode;
import com.downn_fzl.currencyextra.exception.DataNotLoadException;
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

public class GiveCommandTreeNode extends CommandTreeNode {
    public GiveCommandTreeNode(CommandTreeNode parent) {
        super(parent, "give");
    }

    @Override
    public void execute(CommandSender sender, String command, String[] args) {

        Currency currency = CurrencyManager.get(command.toUpperCase());

        try {
            if (args.length >= 2) {
                Player target = Bukkit.getPlayer(args[0]);

                if (target != null) {

                    PlayerData playerData = PlayerData.get(target.getUniqueId());
                    double amount = Double.parseDouble(args[1]);
                    playerData.deposit(currency, amount);
                    sender.sendMessage(Utils.getMessage("give-success", true)
                            .replace("{amount}", Utils.formatNumber(amount))
                            .replace("{currency}", currency.getDisplayName())
                            .replace("{target}", args[0])
                            .replace("{balance}", Utils.formatNumber(playerData.getBalance(currency)))
                            .replace("{symbol}", currency.getSymbol())
                    );

                } else {
                    // player is not online

                    Bukkit.getScheduler().runTaskAsynchronously(CurrencyExtra.instance, ()->{
                        OfflinePlayer player = Bukkit.getOfflinePlayer(args[0]);
                        if (player.hasPlayedBefore()) {

                            PlayerData.getSavedData(player.getUniqueId(), playerData -> {
                                double amount = Double.parseDouble(args[1]);
                                playerData.deposit(currency, amount);
                                sender.sendMessage(Utils.getMessage("give-success", true)
                                        .replace("{amount}", Utils.formatNumber(amount))
                                        .replace("{currency}", currency.getDisplayName())
                                        .replace("{target}", args[0])
                                        .replace("{balance}", Utils.formatNumber(playerData.getBalance(currency)))
                                        .replace("{symbol}", currency.getSymbol())
                                );
                            });

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
                        .replace("{command}", getCurrentCommand(command) + " <target> <value>")
                );
            }
        } catch (DataNotLoadException e) {
            // data not load
            sender.sendMessage(Utils.getMessage("data-not-load", true));
        } catch (NumberFormatException e) {
            sender.sendMessage(Utils.getMessage("invalid-syntax", true));
            sender.sendMessage(Utils.getMessage("usage", true)
                    .replace("{command}", getCurrentCommand(command) + " <target> <value>")
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
