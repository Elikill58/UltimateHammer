package com.elikill58.ultimatehammer.support.factions;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.massivecraft.factions.Board;
import com.massivecraft.factions.FLocation;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.perms.PermissibleActions;

public class FactionsUUIDSupport implements BuildChecker {

	@Override
	public boolean canBuild(Player p, Location loc) {
		FLocation floc = new FLocation(loc);
		return Board.getInstance().getFactionAt(floc).hasAccess(FPlayers.getInstance().getByPlayer(p), PermissibleActions.BUILD, floc);
	}
}
