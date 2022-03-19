package com.elikill58.ultimatehammer.universal.account;

import java.util.UUID;

import org.checkerframework.checker.nullness.qual.NonNull;

import com.elikill58.ultimatehammer.universal.TranslatedMessages;

/**
 * Contains player-related data that can be accessed when the player is offline.
 */
public final class UltimateHammerAccount {

	private final UUID playerId;
	private String lang, playerName;

	public UltimateHammerAccount(UUID playerId) {
		this(playerId, null, TranslatedMessages.getDefaultLang());
	}

	public UltimateHammerAccount(UUID playerId, String playerName, String lang) {
		this.playerId = playerId;
		this.playerName = playerName;
		this.lang = lang;
	}

	public UUID getPlayerId() {
		return playerId;
	}
	
	public String getPlayerName() {
		return playerName;
	}
	
	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}
	
	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

	@NonNull
	public static UltimateHammerAccount get(UUID accountId) {
		return UltimateHammerAccountManager.getManager().getNow(accountId);
	}
}
