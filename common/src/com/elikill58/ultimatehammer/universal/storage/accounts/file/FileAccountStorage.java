package com.elikill58.ultimatehammer.universal.storage.accounts.file;

import java.io.File;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.checkerframework.checker.nullness.qual.Nullable;

import com.elikill58.ultimatehammer.api.yaml.Configuration;
import com.elikill58.ultimatehammer.api.yaml.YamlConfiguration;
import com.elikill58.ultimatehammer.universal.Adapter;
import com.elikill58.ultimatehammer.universal.TranslatedMessages;
import com.elikill58.ultimatehammer.universal.account.UltimateHammerAccount;
import com.elikill58.ultimatehammer.universal.storage.accounts.UltimateHammerAccountStorage;

public class FileAccountStorage extends UltimateHammerAccountStorage {

	private final File userDir;

	public FileAccountStorage(File userDir) {
		this.userDir = userDir;
	}

	@Override
	public CompletableFuture<@Nullable UltimateHammerAccount> loadAccount(UUID playerId) {
		return CompletableFuture.supplyAsync(() -> {
			File file = new File(userDir, playerId + ".yml");
			if (!file.exists()) {
				return null;
			}
			try {
				Configuration config = YamlConfiguration.load(file);
				String playerName = config.getString("playername");
				String language = config.getString("lang", TranslatedMessages.getDefaultLang());
				return new UltimateHammerAccount(playerId, playerName, language);
			} catch (Exception e) { // prevent parsing error due to corrupted file.
				Adapter ada = Adapter.getAdapter();
				ada.getLogger().info("File account of " + ada.getOfflinePlayer(playerId).getName() + " have been corrupted. Creating a new one ...");
				if(!file.delete())
					file.deleteOnExit();
				UltimateHammerAccount acc = new UltimateHammerAccount(playerId);
				// TODO try to get most data as possible from old file
				saveAccount(acc).join();
				return acc;
			}
		});
	}

	@Override
	public CompletableFuture<Void> saveAccount(UltimateHammerAccount account) {
		return CompletableFuture.runAsync(() -> {
			File file = new File(userDir, account.getPlayerId() + ".yml");
			if(!file.exists()) {
				try {
					userDir.mkdirs();
					file.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			Configuration accountConfig = YamlConfiguration.load(file);
			accountConfig.set("playername", account.getPlayerName());
			accountConfig.set("lang", account.getLang());
			accountConfig.directSave();
		});
	}
}
