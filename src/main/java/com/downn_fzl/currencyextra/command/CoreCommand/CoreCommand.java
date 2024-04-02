package com.downn_fzl.currencyextra.command.CoreCommand;

import com.downn_fzl.currencyextra.command.CommandTreeNode;
import com.downn_fzl.currencyextra.command.CoreCommand.core.ReloadCommandTreeNode;
import com.downn_fzl.currencyextra.command.CoreCommand.core.TestCommandTreeNode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CoreCommand extends CommandTreeNode implements CommandExecutor, TabExecutor {
    public CoreCommand() {
        super(null, "currency");

        addChild(new ReloadCommandTreeNode(this));
        addChild(new TestCommandTreeNode(this));

    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command lable, @NotNull String command, String[] args) {

        super.execute(sender, command, args);
        return false;

    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command label, @NotNull String command, @NotNull String[] args) {
        return super.tabComplete(sender, command, args);
    }
}
