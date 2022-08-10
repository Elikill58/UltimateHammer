package com.elikill58.ultimatehammer.universal.account;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;

import org.checkerframework.checker.nullness.qual.Nullable;

import com.elikill58.ultimatehammer.universal.Adapter;
import com.elikill58.ultimatehammer.universal.storage.accounts.UltimateHammerAccountStorage;

public class SimpleAccountManager extends UltimateHammerAccountManager {

	protected final Map<UUID, UltimateHammerAccount> accounts = Collections.synchronizedMap(new HashMap<>());
	private final Map<UUID, CompletableFuture<UltimateHammerAccount>> pendingRequests = Collections.synchronizedMap(new HashMap<>());
	private final boolean persistent;

	public SimpleAccountManager(boolean persistent) {
		this.persistent = persistent;
	}

	@Override
	public CompletableFuture<UltimateHammerAccount> get(UUID accountId) {
		CompletableFuture<UltimateHammerAccount> pendingRequest = pendingRequests.get(accountId);
		if (pendingRequest != null) {
			return pendingRequest;
		}

		UltimateHammerAccount existingAccount = accounts.get(accountId);
		if (existingAccount != null) {
			return CompletableFuture.completedFuture(existingAccount);
		}
		CompletableFuture<UltimateHammerAccount> loadFuture = UltimateHammerAccountStorage.getStorage().getOrCreateAccount(accountId);
		pendingRequests.put(accountId, loadFuture);
		loadFuture.whenComplete((account, throwable) -> {
			pendingRequests.remove(accountId);
			if (throwable != null && !(throwable instanceof CancellationException)) {
				Adapter.getAdapter().getLogger().error("Account loading completed exceptionally: " + throwable.getMessage());
				throwable.printStackTrace();
				return;
			}

			UUID playerId = account.getPlayerId();
			accounts.put(playerId, account);
		});
		return loadFuture;
	}

	@Override
	public CompletableFuture<Void> save(UUID accountId) {
		if (persistent) {
			UltimateHammerAccount existingAccount = accounts.get(accountId);
			if (existingAccount != null) {
				return UltimateHammerAccountStorage.getStorage().saveAccount(existingAccount);
			}
		}
		return CompletableFuture.completedFuture(null);
	}

	@Override
	public void update(UltimateHammerAccount account) {
		accounts.put(account.getPlayerId(), account);
	}

	@Nullable
	@Override
	public UltimateHammerAccount dispose(UUID accountId) {
		return accounts.remove(accountId);
	}
}
