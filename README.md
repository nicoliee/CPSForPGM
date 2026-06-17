# CPSForPGM

**Requires:** [PGM](https://pgm.dev) + [packetevents](https://github.com/retrooper/packetevents)

Real-time CPS (Clicks Per Second) tracking and nametag display for PGM matches.

Tracks every player's left-click rate and shows it directly above their head as a live nametag. The tag color matches the player's current team color, making it easy to read at a glance during a match.

## Features

- **Per-player CPS counter** — tracks clicks in a rolling 20-tick window for accurate real-time readings
- **Scoreboard nametag** — each player's current CPS is rendered as a dynamic tag above their head
- **Team-colored tags** — the CPS tag automatically uses the player's team color from PGM
- **Zero configuration** — drop in, it just works
- **Lightweight** — uses packet-level listeners with no overhead from scoreboard teams or chat

## How it works

CPSForPGM listens to incoming `ANIMATION` packets (sent by the client on left click) and computes the click rate over the last 20 game ticks (1 second). The result is displayed as a floating nametag layer registered through PGM's tag system, updating every tick.

## Build

```bash
mvn clean package
```

The output jar is placed in `target/CPSForPGM.jar`.

## Installation

1. Place `CPSForPGM.jar` in your server's `plugins/` folder
2. Make sure PGM and packetevents are also installed
3. Start your server

# Credits

The CPS tracking logic, tag-layering system, and PGM team color integration in this plugin are derived from [brady](https://github.com/PGMFFEnthusiasts/brady), an open-source Minecraft server plugin developed by the PGM Flag Football Enthusiasts community.

Specifically, the following components were ported and adapted:

- **`CPSTags`** — per-player CPS measurement using a 20-tick sliding window, and team color rendering via PGM party events.
- **`TagAPI` / `TagTracker` / `TagView` / `TagPacketListener`** — the armor stand–based nametag layering system that renders additional text above player nametags without replacing them.

All credit for the original design and implementation goes to the contributors of the brady repository.
