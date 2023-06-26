package magtav.autopickupmt;

import io.lumine.mythic.api.adapters.AbstractPlayer;
import io.lumine.mythic.api.mobs.MythicMob;
import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.core.drops.Drop;
import io.lumine.mythic.core.drops.LootBag;
import io.lumine.mythic.core.mobs.ActiveMob;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import io.lumine.mythic.bukkit.events.MythicMobDeathEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class EventsMythic implements Listener {

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void autoPickup(MythicMobDeathEvent e) {
        try {
            if (e.getKiller() == null || !(e.getKiller() instanceof Player killer)) return;
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
        } catch (NullPointerException ignored) {}
    }
}
