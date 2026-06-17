package org.nicolie.cpsforpgm;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.nicolie.cpsforpgm.tag.TagAPI;
import org.nicolie.cpsforpgm.tag.TagLayer;
import org.nicolie.cpsforpgm.tracker.CPSTracker;
import tc.oc.pgm.api.party.Party;
import tc.oc.pgm.events.PlayerJoinPartyEvent;

public class CPSTags implements Listener {

  private final Map<Integer, TextColor> teamColors = new ConcurrentHashMap<>();

  private final CPSTracker tracker;

  public CPSTags(Plugin plugin, TagAPI tagAPI, CPSTracker tracker) {
    this.tracker = tracker;

    tagAPI.register(new TagLayer(
        "cps",
        0,
        this::getCPS,
        (target, viewer) -> true // visible para todos; ajusta según necesidad
        ));

    plugin.getServer().getPluginManager().registerEvents(this, plugin);
  }

  private Component getCPS(Player target) {
    int cps = tracker.getCPS(target.getUniqueId());
    TextColor teamColor = teamColors.getOrDefault(target.getEntityId(), NamedTextColor.AQUA);
    return Component.text(cps + " CPS", teamColor);
  }

  @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
  public void onTeamChange(PlayerJoinPartyEvent event) {
    int eid = event.getPlayer().getBukkit().getEntityId();
    Party party = event.getNewParty();
    teamColors.put(eid, party == null ? NamedTextColor.AQUA : party.getTextColor());
  }
}
