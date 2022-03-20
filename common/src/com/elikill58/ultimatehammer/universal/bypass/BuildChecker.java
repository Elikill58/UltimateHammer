package com.elikill58.ultimatehammer.universal.bypass;

import com.elikill58.ultimatehammer.api.entity.Player;
import com.elikill58.ultimatehammer.api.location.Location;

public interface BuildChecker {

	public boolean canBuild(Player p, Location loc);
}
