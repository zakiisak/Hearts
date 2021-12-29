package com.icurety.hearts;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandSetMaxHp implements CommandExecutor {

    private Integer tryParse(String num) {
        try {
            return Integer.parseInt(num);
        }
        catch(NumberFormatException e) {
            return null;
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender.isOp())
        {
            if(args.length < 1)
            {
                return false;
            }
            else {
                //Set your own max hp
                if(args.length == 1)
                {
                    if(sender instanceof Player) {
                        Player player = (Player) sender;
                        Integer newMaxHp = tryParse(args[0]);
                        if(newMaxHp == null) sender.sendMessage("New Max HP must be in number format");
                        else {
                            MaxHpRegistry.updateMaxHpFor(player, newMaxHp);
                            sender.sendMessage("You Max HP has been changed");
                        }
                    }
                    else sender.sendMessage("You can't set your own maxhp from console");
                }
                else {
                    String playerName = args[0];
                    Integer newMaxHp = tryParse(args[1]);
                    Player player = sender.getServer().getPlayer(playerName);
                    if(player != null)
                    {
                        if(newMaxHp != null) {
                            MaxHpRegistry.updateMaxHpFor(player, newMaxHp);
                            sender.sendMessage(ChatColor.GREEN + "Updated " + player.getName() + "'s max hp to " + ChatColor.RED + ((double) (newMaxHp) / 2.0) + "hearts");
                        }
                        else sender.sendMessage("New Max HP must be in number format");
                    }
                    else sender.sendMessage("That player isn't logged in");
                }
                return true;
            }
        }
        else {
            sender.sendMessage("You are not authorized to use this command");
            return true;
        }
    }
}
