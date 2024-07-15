package ivanhauu.tech.bridgewars.listeners;

import ivanhauu.tech.bridgewars.BridgeWars;
import ivanhauu.tech.bridgewars.PlayerWinner;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class onPlayerDamage implements Listener {

    private final BridgeWars plugin;
    private final PlayerWinner playerWinner;

    public onPlayerDamage(BridgeWars plugin, PlayerWinner playerWinner) {
        this.plugin = plugin;
        this.playerWinner = playerWinner;
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent event) {
        String eventWorldName = event.getEntity().getWorld().getName();

        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            if (eventWorldName.startsWith("battle_4v4_") || eventWorldName.startsWith("battle_2v2_")) {
                Location iSpawn = new Location(player.getWorld(), -32, 50, 32);

                if (eventWorldName.startsWith("battle_4v4_")) {
                    boolean is4v4BattleStarted = plugin.getBattleConfig().getBoolean("worlds." + eventWorldName + ".is4v4BattleStarted");
                    if (!is4v4BattleStarted) {
                        event.setCancelled(true);
                    }
                } else if (eventWorldName.startsWith("battle_2v2_")) {
                    boolean is2v2BattleStarted = plugin.getBattleConfig().getBoolean("worlds." + eventWorldName + ".is2v2BattleStarted");
                    if (!is2v2BattleStarted) {
                        event.setCancelled(true);
                    }
                }

                if (event.getCause() == EntityDamageEvent.DamageCause.VOID) {
                    if (player.getGameMode() != GameMode.SPECTATOR) {
                        for (Player p : player.getWorld().getPlayers()) {
                            p.sendMessage("O player " + player.getName() + " foi de arrasta!");
                        }
                    }
                    player.teleport(iSpawn);
                    player.setGameMode(GameMode.SPECTATOR);
                    player.sendTitle("§4Você morreu!","§4Assista a partida, ou saia com /spawn");
                    playerWinner.playerWinner(player.getWorld(), false);
                }

            }
        }
    }
}
