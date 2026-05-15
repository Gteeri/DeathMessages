package dev.gteeri.deathmessages.commands;

import dev.gteeri.deathmessages.DeathMessages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class DeathMsgCommand implements CommandExecutor, TabCompleter {

    private final DeathMessages plugin;

    public DeathMsgCommand(DeathMessages plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("deathmessages.admin")) {
            sender.sendMessage(plugin.getLang("no-permission"));
            return true;
        }

        if (args.length == 0) {
            sender.sendMessage(plugin.getLang("usage"));
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "reload" -> {
                plugin.loadConfigs();
                sender.sendMessage(plugin.getLang("reload-success"));
            }
            case "toggle" -> {
                plugin.setPluginEnabled(!plugin.isPluginEnabled());
                String key = plugin.isPluginEnabled() ? "toggled-on" : "toggled-off";
                sender.sendMessage(plugin.getLang(key));
            }
            default -> sender.sendMessage(plugin.getLang("usage"));
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1 && sender.hasPermission("deathmessages.admin")) {
            List<String> options = List.of("reload", "toggle");
            for (String option : options) {
                if (option.startsWith(args[0].toLowerCase())) {
                    completions.add(option);
                }
            }
        }
        return completions;
    }
}
