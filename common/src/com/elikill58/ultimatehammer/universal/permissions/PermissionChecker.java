package com.elikill58.ultimatehammer.universal.permissions;

import com.elikill58.ultimatehammer.api.UltimateHammerPlayer;

/**
 * PermissionCheckers are used by Negativity to determine if a player has the right to do something.
 * Checkers responses are based on their own criteria.
 */
public interface PermissionChecker {

	boolean hasPermission(UltimateHammerPlayer player, String permission);
}
