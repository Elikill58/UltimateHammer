package com.elikill58.ultimatehammer.support.factions;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.massivecraft.factions.Board;
import com.massivecraft.factions.FLocation;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.perms.PermissibleAction;

public class FactionsUUIDSupport implements BuildChecker {

	@Override
	public boolean canBuild(Player p, Location loc) {
		return Board.getInstance().getFactionAt(new FLocation(loc)).hasAccess(FPlayers.getInstance().getByPlayer(p), PermissibleAction.BUILD);
	}
}
