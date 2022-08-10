package com.elikill58.ultimatehammer.universal.storage.accounts;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.checkerframework.checker.nullness.qual.Nullable;

import com.elikill58.ultimatehammer.universal.account.UltimateHammerAccount;

public final class VoidAccountStorage extends UltimateHammerAccountStorage {

	public static final VoidAccountStorage INSTANCE = new VoidAccountStorage();

	@Override
	public CompletableFuture<@Nullable UltimateHammerAccount> loadAccount(UUID playerId) {
		return CompletableFuture.completedFuture(null);
	}

	@Override
	public CompletableFuture<Void> saveAccount(UltimateHammerAccount account) {
		return CompletableFuture.completedFuture(null);
	}
}
