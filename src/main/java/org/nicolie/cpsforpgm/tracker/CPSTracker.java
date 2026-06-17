package org.nicolie.cpsforpgm.tracker;

import com.github.retrooper.packetevents.event.PacketListenerAbstract;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.event.UserDisconnectEvent;
import com.github.retrooper.packetevents.event.UserLoginEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerFlying;
import java.util.Map;
import java.util.UUID;
import org.jctools.maps.NonBlockingHashMap;

public class CPSTracker extends PacketListenerAbstract {

  private final Map<Integer, Profile> profiles = new NonBlockingHashMap<>();
  private final Map<UUID, Integer> uuidToEntityId = new NonBlockingHashMap<>();

  @Override
  public void onUserLogin(UserLoginEvent event) {
    int eid = event.getUser().getEntityId();
    UUID uuid = event.getUser().getUUID();
    profiles.put(eid, new Profile());
    if (uuid != null) uuidToEntityId.put(uuid, eid);
  }

  @Override
  public void onUserDisconnect(UserDisconnectEvent event) {
    int eid = event.getUser().getEntityId();
    UUID uuid = event.getUser().getUUID();
    profiles.remove(eid);
    if (uuid != null) uuidToEntityId.remove(uuid);
  }

  @Override
  public void onPacketReceive(PacketReceiveEvent event) {
    if (!(event.getPacketType() instanceof PacketType.Play.Client type)) return;

    Profile profile = profiles.get(event.getUser().getEntityId());
    if (profile == null) return;

    if (WrapperPlayClientPlayerFlying.isFlying(type)) {
      profile.tick();
    } else if (type == PacketType.Play.Client.ANIMATION) {
      profile.click();
    }
  }

  public int getCPS(UUID uuid) {
    Integer eid = uuidToEntityId.get(uuid);
    return eid != null ? getCPS(eid) : 0;
  }

  public int getCPS(int entityId) {
    Profile p = profiles.get(entityId);
    return p != null ? p.cps : 0;
  }

  private static class Profile {
    private final int[] clickBuffer = new int[20];
    private int index = 0;
    private int count = 0;
    volatile int cps = 0;

    void tick() {
      index = (index + 1) % 20;
      count -= clickBuffer[index];
      clickBuffer[index] = 0;
      cps = count;
    }

    void click() {
      clickBuffer[index]++;
      count++;
      cps = count;
    }
  }
}
