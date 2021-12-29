package com.icurety.hearts;

import org.bukkit.ChatColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandSetHp implements CommandExecutor {

    private Integer tryParse(String num) {
        try {
            return Integer.parseInt(num);
        }
        catch(NumberFormatException e) {
            return null;
        }
    }

    private void changeHp(Player player, int newHp) throws IllegalArgumentException {
        player.setHealth(newHp);
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
                        Integer newHp = tryParse(args[0]);
                        if(newHp == null) sender.sendMessage("New HP must be in number format");
                        else {
                            boolean error = false;
                            String appendage = "";
                            try {
                                changeHp(player, newHp);
                            }
                            catch(IllegalArgumentException e) {
                                if(newHp < 0) {
                                    error = true;
                                }
                                else {
                                    newHp = (int) player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue();
                                    appendage = " to max";
                                }
                            }
                            if(error == false) {
                                sender.sendMessage("You hp has been changed" + appendage);
                            }
                            else
                                sender.sendMessage("That hp amount is outside the boundary of 0 and max hp");
                        }
                    }
                    else sender.sendMessage("You can't set your own hp from console");
                }
                else {
                    String playerName = args[0];
                    Integer newHp = tryParse(args[1]);
                    Player player = sender.getServer().getPlayer(playerName);
                    if(player != null)
                    {
                        if(newHp != null) {
                            try {
                                changeHp(player, newHp);
                                sender.sendMessage(ChatColor.GREEN + "Updated " + player.getName() + "'s hp to " + ChatColor.RED + ((double) (newHp) / 2.0) + "hearts");
                            }
                            catch(IllegalArgumentException e) {
                                sender.sendMessage("That hp amount is outside the boundary of 0 and max hp");
                            }
                        }
                        else sender.sendMessage("New HP must be in number format");
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
