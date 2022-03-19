package com.elikill58.ultimatehammer.universal.dataStorage;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.checkerframework.checker.nullness.qual.Nullable;

import com.elikill58.ultimatehammer.universal.Adapter;
import com.elikill58.ultimatehammer.universal.Database;
import com.elikill58.ultimatehammer.universal.account.UltimateHammerAccount;
import com.elikill58.ultimatehammer.universal.dataStorage.database.DatabaseAccountStorage;
import com.elikill58.ultimatehammer.universal.dataStorage.file.FileAccountStorage;

public abstract class UltimateHammerAccountStorage {

	private static final Map<String, UltimateHammerAccountStorage> storages = new HashMap<>();
	private static String storageId;

	public abstract CompletableFuture<@Nullable UltimateHammerAccount> loadAccount(UUID playerId);

	public abstract CompletableFuture<Void> saveAccount(UltimateHammerAccount account);

	public CompletableFuture<UltimateHammerAccount> getOrCreateAccount(UUID playerId) {
		return loadAccount(playerId).thenApply(existingAccount -> existingAccount == null ? new UltimateHammerAccount(playerId) : existingAccount);
	}
	
	public static UltimateHammerAccountStorage getStorage() {
		return storages.getOrDefault(storageId, VoidAccountStorage.INSTANCE);
	}

	public static void register(String id, UltimateHammerAccountStorage storage) {
		storages.put(id, storage);
	}

	public static String getStorageId() {
		return storageId;
	}

	public static void setStorageId(String storageId) {
		UltimateHammerAccountStorage.storageId = storageId;
	}

	public static void setDefaultStorage(String storageId) {
		UltimateHammerAccountStorage storage = storages.get(storageId);
		if (storage != null) {
			register("default", storage);
		}
	}

	public static void init() {
		Adapter adapter = Adapter.getAdapter();
		storageId = adapter.getConfig().getString("accounts.storage.id");
		
		UltimateHammerAccountStorage.register("file", new FileAccountStorage(new File(adapter.getDataFolder(), "user")));
		if (Database.hasCustom) {
			UltimateHammerAccountStorage.register("database", new DatabaseAccountStorage());
		}
	}
}
