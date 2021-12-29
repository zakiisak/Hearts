package com.icurety.hearts;

import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class EventListener implements Listener {

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
    public void onEntityDeath(EntityDeathEvent event) {
        if(Hearts.isLifeStealEnabled())
        {
            //A player killed another player
            if(event.getEntity() instanceof Player && event.getEntity().getLastDamageCause().getEntity() instanceof Player) {
                Player killedPlayer = (Player) event.getEntity();
                Player killer = (Player) event.getEntity().getLastDamageCause().getEntity();
                int killedMaxHp = MaxHpRegistry.getMaxHpFor(killedPlayer);
                int killerMaxHp = MaxHpRegistry.getMaxHpFor(killer);
                //as long as the killed player has at least 1 heart, it will get life stealed
                if(killedMaxHp > 2) {
                    MaxHpRegistry.updateMaxHpFor(killedPlayer, killedMaxHp - 2);
                    MaxHpRegistry.updateMaxHpFor(killer, killerMaxHp + 2);
                }
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

}
