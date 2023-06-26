package magtav.autopickupmt;

import io.lumine.mythic.bukkit.MythicBukkit;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityDropItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class Events implements Listener {

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void autoPickup(EntityDeathEvent e) {
        if (MythicBukkit.inst().getMobManager().isMythicMob(e.getEntity())) return;
        try {
            if (e.getEntity().getKiller() == null) return;
            Player killer = e.getEntity().getKiller();
            if (killer.hasPermission("autopickup.pickup")) {
                Inventory inv = killer.getInventory();
                List<ItemStack> dropped = e.getDrops();
                if (dropped.isEmpty()) return;
                Iterator<ItemStack> iterator = dropped.iterator();
                while (iterator.hasNext()) {
                    ItemStack itemStack = iterator.next();
                    if (inv.first(itemStack) != -1) {
                        int slot = inv.first(itemStack);
                        int number = Objects.requireNonNull(inv.getItem(slot)).getAmount();
                        itemStack.setAmount(number + itemStack.getAmount());
                        inv.setItem(slot, itemStack);
                        iterator.remove();
                    } else if (inv.firstEmpty() != -1) {
                        inv.addItem(itemStack);
                        iterator.remove();
                    }
                }
                e.getDrops().clear();
                e.getDrops().addAll(dropped);
            }
        } catch (NullPointerException ignored) {
        }
    }
}
