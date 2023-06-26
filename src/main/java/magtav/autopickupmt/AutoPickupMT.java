package magtav.autopickupmt;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class AutoPickupMT extends JavaPlugin {

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(new Events(), this);
        if (Bukkit.getPluginManager().getPlugin("MythicMobs") != null && Bukkit.getPluginManager().getPlugin("MythicMobs").isEnabled()) Bukkit.getPluginManager().registerEvents(new EventsMythic(), this);
    }

}
