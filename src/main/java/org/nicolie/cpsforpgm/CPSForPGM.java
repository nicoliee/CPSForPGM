package org.nicolie.cpsforpgm;

import com.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.factory.spigot.SpigotPacketEventsBuilder;
import org.bukkit.plugin.java.JavaPlugin;
import org.nicolie.cpsforpgm.tag.TagAPI;
import org.nicolie.cpsforpgm.tracker.CPSTracker;

public class CPSForPGM extends JavaPlugin {

  private CPSTracker cpsTracker;

  @Override
  public void onLoad() {
    PacketEvents.setAPI(SpigotPacketEventsBuilder.build(this));
    PacketEvents.getAPI().load();
  }

  @Override
  public void onEnable() {
    PacketEvents.getAPI().init();
    cpsTracker = new CPSTracker();
    PacketEvents.getAPI().getEventManager().registerListener(cpsTracker);
    TagAPI tagAPI = new TagAPI(this);
    new CPSTags(this, tagAPI, cpsTracker);
  }

  @Override
  public void onDisable() {
    PacketEvents.getAPI().terminate();
  }
}
