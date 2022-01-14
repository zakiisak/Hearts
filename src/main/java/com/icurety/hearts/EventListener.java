package com.icurety.hearts;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.ItemDespawnEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.Random;

public class EventListener implements Listener {

    private Random random = new Random();

    private void updatePlayerMaxHpFromRegistry(Player player, int defaultValue)
    {
        Integer registeredMaxHp = MaxHpRegistry.getMaxHpFor(player);
        System.out.println("Registered max hp: " + registeredMaxHp);
        player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(registeredMaxHp != null ? registeredMaxHp : defaultValue);
        if(registeredMaxHp == null) {
            MaxHpRegistry.updateMaxHpFor(player, defaultValue);
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        updatePlayerMaxHpFromRegistry(player, 6);
    }

    @EventHandler
    public void onPlayerDeathEvent(PlayerDeathEvent event) {

        //A player killed another player
        if(event.getEntity().getKiller() != null) {

            Player killedPlayer = event.getEntity();
            Player killer = killedPlayer.getKiller();
            Integer killedMaxHp = MaxHpRegistry.getMaxHpFor(killedPlayer);
            Integer killerMaxHp = MaxHpRegistry.getMaxHpFor(killer);
            //as long as the killed player has at least 1 heart, it will get life stealed
            if(killedMaxHp != null && killerMaxHp != null && killedMaxHp > 2) {
                MaxHpRegistry.updateMaxHpFor(killedPlayer, killedMaxHp - 2);
                killedPlayer.getWorld().dropItem(killedPlayer.getEyeLocation(), Recipes.getHeartContainerStack());
            }
        }
    }

    private static ItemStack createHeartStack() {
        ItemStack stack = new ItemStack(Material.APPLE, 1);
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(ChatColor.RED + "Heart");
        meta.setCustomModelData(21);
        stack.setItemMeta(meta);
        return stack;
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        //Entity is living, but not a player
        if((event.getEntity() instanceof LivingEntity) && (event.getEntity() instanceof Player) == false) {
            //Entity was killed by a player
            if(event.getEntity().getKiller() != null)
            {
                //add heart piece if player is missing
                Player killer = event.getEntity().getKiller();
                if(killer.getHealth() < killer.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue())
                {
                    event.getDrops().add(createHeartStack());
                }
                Creeper e;

                if(event.getEntity() instanceof Monster && random.nextInt(30) == 5)
                {
                    event.getDrops().add(Recipes.getHeartPieceItemStack());
                }
            }
        }
    }

    @EventHandler
    public void onCraftItemEvent(CraftItemEvent event)
    {
        if(event.getCurrentItem() != null && event.getCurrentItem().getType().equals(Material.APPLE)
                && event.getCurrentItem().getItemMeta().getCustomModelData() == 20)
        {
            if(event.getWhoClicked() instanceof Player)
            {
                Player player = (Player) event.getWhoClicked();
                player.playSound(player.getLocation(), "heart.container.get.sound", 1f, 1f);
            }
        }
    }

    @EventHandler
    public void onItemDespawn(ItemDespawnEvent event)
    {
        ItemStack stack = event.getEntity().getItemStack();
        if(stack.getType().equals(Material.APPLE) && stack.getItemMeta().getCustomModelData() == 20)
        {
            event.setCancelled(true);
        }
        else if(stack.getType().equals(Material.FEATHER) && stack.getItemMeta().getCustomModelData() == 19)
        {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityPickupItem(EntityPickupItemEvent event) {
        ItemStack stack = event.getItem().getItemStack();

        //This means, it's a heart
        if(stack.getType().equals(Material.APPLE))
        {
            if(stack.getItemMeta().getCustomModelData() == 20)
            {
                if(event.getEntity() instanceof Player)
                {
                    Player player = (Player) event.getEntity();
                    player.playSound(player.getLocation(), "heart.container.get.sound", 1f, 1f);
                }
            }
            if(stack.getItemMeta().getCustomModelData() == 21)
            {
                event.setCancelled(true);
                if(event.getEntity().getHealth() <  event.getEntity().getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue()) {
                    event.getItem().remove();

                    if(event.getEntity() instanceof Player)
                    {
                        Player player = (Player) event.getEntity();
                        player.playSound(player.getLocation(), "heart.get.sound", 1f, 1f);
                    }

                    if(event.getEntity().getHealth() + 2 > event.getEntity().getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue())
                    {
                        event.getEntity().setHealth(event.getEntity().getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue());
                    }
                    else event.getEntity().setHealth(event.getEntity().getHealth() + 2);
                }
            }
        }
        else if(stack.getType().equals(Material.FEATHER) && stack.getItemMeta().getCustomModelData() == 19)
        {
            if(event.getEntity() instanceof Player)
            {
                Player player = (Player) event.getEntity();
                player.playSound(player.getLocation(), "heart.piece.sound", 1f, 1f);
            }
        }
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Integer maxHp = MaxHpRegistry.getMaxHpFor(event.getPlayer());
        event.getPlayer().getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(maxHp);
        if(event.getPlayer().getHealth() > maxHp) {
            event.getPlayer().setHealth(maxHp);
        }
        System.out.println("updated max hp");
    }

    @EventHandler
    public void onPlayerItemConsume(PlayerItemConsumeEvent event) {
        List<String> lore = event.getItem().getItemMeta().getLore();
        System.out.println("Custom Model Data: " + event.getItem().getItemMeta().getCustomModelData());
        if(event.getItem().getItemMeta().getCustomModelData() == 20) {
            event.getPlayer().playSound(event.getPlayer().getLocation(), "heart.container.sound", 1f, 1f);
            MaxHpRegistry.updateMaxHpFor(event.getPlayer(), MaxHpRegistry.getMaxHpFor(event.getPlayer()) + 2);
        }
    }
}