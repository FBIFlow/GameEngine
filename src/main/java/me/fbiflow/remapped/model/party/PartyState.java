package me.fbiflow.remapped.model.party;

import me.fbiflow.remapped.model.wrapper.internal.Player;

import java.util.List;

public record PartyState(Player owner, List<Player> members, List<String> permissionLevel) {
}