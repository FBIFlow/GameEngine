package me.fbiflow.remapped.model.party;

import me.fbiflow.remapped.model.wrapper.internal.Player;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class PartyManager {

    private final List<Party> parties;

    private Map<Player, List<Player>> invites;
    private Map<Player, Player> lastInvitedBy;

    public PartyManager() {
        this.parties = new ArrayList<>();
        this.invites = new HashMap<>();
        this.lastInvitedBy = new HashMap<>();
    }

    /**
     * Create new instance of Party and add it to holder
     *
     * @param owner who creates
     * @return created Party
     */
    public Party createParty(Player owner) {
        Party party = Party.newInstance();
        party.setOwner(owner);
        party.getMembers().add(owner);
        //TODO: send message
        parties.add(party);
        party.setPermissionLevel(owner.getPermissions());
        return party;
    }

    /**
     * Delete Party
     */
    public void removeParty(Party party) {
        Player owner = party.getOwner();
        List<Player> members = party.getMembers();
        members.remove(owner);
        //TODO: send message to owner
        for (Player member : members) {
            members.remove(member);
            //TODO: send message to members
        }
        parties.remove(party);
    }

    /**
     * Remove player from party. If player is owner set new owner
     * @param player who leave
     */
    public void leaveParty(Player player) {
        Party party = getByMember(player);
        if (party == null) {
            //TODO: send message (not in party)
            return;
        }
        List<Player> members = party.getMembers();
        if (party.getOwner() == player) {
            members.remove(player);
            removeInvites(player);
            party.setOwner(members.getFirst());
            //TODO: send message (new owner)
            for (Player p : members) {
                if (p != party.getOwner()) {
                    //TODO: send message (owner leave, have new owner)
                }
            }
        } else {
            for (Player p : members) {
                //TODO: send message (member leave)
            }
        }
    }

    /**
     * Invite player to Party
     *
     * @param sender  who invites, only party owner
     * @param invited who invited
     */
    public void addInvite(Player sender, Player invited) {
        List<Player> invitedPlayers = Optional.ofNullable(invites.get(sender)).orElse(List.of(invited));
        lastInvitedBy.put(invited, sender);
        invites.put(sender, invitedPlayers);
        //TODO: send message
    }

    /**
     * Accept invite
     * @param invited who accepts
     * @param sender  who invited
     */
    public void acceptInvite(Player invited, @Nullable Player sender) {
        Party party = sender != null ? getByOwner(sender) : getByOwner(lastInvitedBy.remove(invited));
        if (party == null) {
            //TODO: send message
            return;
        }
        party.getMembers().add(invited);
        //TODO: send message
    }

    /**
     * Remove invite for player
     *
     * @param sender  who invited
     * @param invited to remove invite
     */
    public void removeInvite(Player sender, Player invited) {
        List<Player> invitedPlayers = invites.get(sender);
        invitedPlayers.remove(invited);
        if (lastInvitedBy.get(invited) == sender) {
            lastInvitedBy.remove(invited);
        }
        //TODO: send message (isn`t invited now)
    }

    /**
     * Remove all invites created by sender
     * @param sender who created invites
     */
    public void removeInvites(Player sender) {
        for (Player invited : lastInvitedBy.keySet()) {
            if (lastInvitedBy.get(invited) == sender) {
                lastInvitedBy.remove(invited);
            }
        }
        invites.remove(sender);
    }

    /**
     * Get Party by party owner
     * @param owner party owner
     * @return Party if exists, otherwise null
     */
    public Party getByOwner(Player owner) {
        for (Party party : parties) {
            if (party.getOwner() == owner) {
                return party;
            }
        }
        return null;
    }

    /**
     * Get Party by member
     * @param member member
     * @return Party if exists, otherwise null
     */
    public Party getByMember(Player member) {
        for (Party party : parties) {
            for (Player p : party.getMembers()) {
                if (p == member) {
                    return party;
                }
            }
        }
        return null;
    }

    /**
     * check if player in party
     * @param player player
     * @return true if player is in party
     */
    public boolean isPlayerInParty(Player player) {
        return getByMember(player) != null;
    }

}