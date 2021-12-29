package com.icurety.hearts;


import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MaxHpRegistry {

    private static Map<String, Integer> records = new HashMap<String, Integer>();

    //Returns the max hp of the given player id, or null if not found
    public static Integer getMaxHpFor(Player player)
    {
        return records.get(player.getUniqueId().toString());
    }

    public static void updateMaxHpFor(Player player, Integer newMaxHp)
    {
        records.put(player.getUniqueId().toString(), newMaxHp);
        player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(newMaxHp);
    }

    public static Map<String, Integer> getRecords() {
        return records;
    }

    public static void loadRecords(Map<String, Integer> savedRecords) {
        records.clear();
        for(String uuid : savedRecords.keySet())
        {
            records.put(uuid, savedRecords.get(uuid));
        }
    }

}
