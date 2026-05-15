package dev.gteeri.deathmessages.listeners;

import dev.gteeri.deathmessages.DeathMessages;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

public class DeathListener implements Listener {

    private final DeathMessages plugin;

    public DeathListener(DeathMessages plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (!plugin.isPluginEnabled()) return;

        Player victim = event.getEntity();
        String cause = determineCause(victim);
        String message = plugin.getMessageConfig().getMessage(cause);

        message = replacePlaceholders(message, victim, cause);
        message = ChatColor.translateAlternateColorCodes('&', message);

        event.setDeathMessage(null);
        plugin.getServer().broadcastMessage(message);
    }

    private String determineCause(Player victim) {
        EntityDamageEvent damageEvent = victim.getLastDamageCause();
        if (damageEvent == null) return "default";

        if (damageEvent instanceof EntityDamageByEntityEvent entityEvent) {
            Entity damager = entityEvent.getDamager();
            if (damager instanceof Player) {
                return "player-kill";
            }
            return "mob";
        }

        return switch (damageEvent.getCause()) {
            case FALL -> "fall";
            case DROWNING -> "drowning";
            case FIRE, FIRE_TICK, LAVA -> "fire";
            case ENTITY_EXPLOSION, BLOCK_EXPLOSION -> "explosion";
            case VOID -> "void";
            default -> "default";
        };
    }

    private String replacePlaceholders(String message, Player victim, String cause) {
        Location loc = victim.getLocation();

        message = message.replace("{victim}", victim.getName());
        message = message.replace("{world}", loc.getWorld().getName());

        if (plugin.showCoordinates()) {
            message = message.replace("{x}", String.valueOf(loc.getBlockX()));
            message = message.replace("{y}", String.valueOf(loc.getBlockY()));
            message = message.replace("{z}", String.valueOf(loc.getBlockZ()));
        } else {
            message = message.replace("{x}", "?").replace("{y}", "?").replace("{z}", "?");
        }

        EntityDamageEvent damageEvent = victim.getLastDamageCause();

        if (cause.equals("player-kill") && damageEvent instanceof EntityDamageByEntityEvent entityEvent) {
            Entity damager = entityEvent.getDamager();
            if (damager instanceof Player killer) {
                message = message.replace("{killer}", killer.getName());
                ItemStack weapon = killer.getInventory().getItemInMainHand();
                String weaponName = weapon.getType().name().toLowerCase().replace("_", " ");
                if (weapon.hasItemMeta() && weapon.getItemMeta().hasDisplayName()) {
                    weaponName = weapon.getItemMeta().getDisplayName();
                }
                message = message.replace("{weapon}", weaponName);
            }
        }

        if (cause.equals("mob") && damageEvent instanceof EntityDamageByEntityEvent entityEvent) {
            Entity mob = entityEvent.getDamager();
            String mobName = mob.getCustomName() != null ? mob.getCustomName() : mob.getType().name().toLowerCase().replace("_", " ");
            message = message.replace("{mob}", mobName);
        }

        return message;
    }
}
