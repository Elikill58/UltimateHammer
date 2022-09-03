package com.elikill58.ultimatehammer.universal.permissions;

import com.elikill58.ultimatehammer.api.UltimateHammerPlayer;
import com.elikill58.ultimatehammer.universal.Adapter;

public class DefaultPermissionChecker implements PermissionChecker {

	@Override
	public boolean hasPermission(UltimateHammerPlayer player, String permission) {
		return player.getPlayer().isOp() || player.getPlayer().hasPermission(Adapter.getAdapter().getConfig().getString("Permissions." + permission + ".default", permission));
	}

}
