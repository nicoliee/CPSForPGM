package org.nicolie.cpsforpgm.tag;

import java.util.function.BiPredicate;
import java.util.function.Function;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

public record TagLayer(
    String id,
    int priority,
    Function<Player, Component> text,
    BiPredicate<Player, Player> visibleTo) {}
