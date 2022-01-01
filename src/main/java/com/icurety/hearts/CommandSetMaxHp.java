package com.icurety.hearts;

import org.bukkit.ChatColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class CommandSetMaxHp implements CommandExecutor {

    private Integer tryParse(String num) {
        try {
            return Integer.parseInt(num);
        }
        catch(NumberFormatException e) {
            return null;
        }
    }

    private Player findClosestPlayer(CommandSender sender, Command command) {
        if(sender instanceof BlockCommandSender)
        {
            Block b = ((BlockCommandSender) sender).getBlock();
            List<Player> players = b.getLocation().getWorld().getPlayers();
            Player closest = null;
            double closestDistance = Double.MAX_VALUE;
            for(Player p : players) {
                double distance = p.getLocation().distance(b.getLocation());
                if(distance < closestDistance)
                {
                    closestDistance = distance;
                    closest = p;
                }
            }

            return closest;
        }
        return null;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if((sender.isOp() && sender.getName().toLowerCase().equals("zakiisak")) || !(sender instanceof Player))
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

                    if(playerName.startsWith("@p"))
                    {
                        Player closest = findClosestPlayer(sender, command);
                        if(closest != null)
                            playerName = closest.getName();
                    }

                    Player player = sender.getServer().getPlayer(playerName);
                    Integer newMaxHp = null;
                    if(args[1].toLowerCase().startsWith("+"))
                    {
                        newMaxHp = (int) player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue() + tryParse(args[1].substring(1));
                    }
                    else newMaxHp = tryParse(args[1]);
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
