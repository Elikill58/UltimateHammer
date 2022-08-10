package com.elikill58.ultimatehammer.universal.storage.accounts.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.checkerframework.checker.nullness.qual.Nullable;

import com.elikill58.ultimatehammer.universal.Database;
import com.elikill58.ultimatehammer.universal.account.UltimateHammerAccount;
import com.elikill58.ultimatehammer.universal.storage.accounts.UltimateHammerAccountStorage;

public class DatabaseAccountStorage extends UltimateHammerAccountStorage {

	public DatabaseAccountStorage() {
		try {
			Connection connection = Database.getConnection();
			if (connection != null) {
				DatabaseMigrator.executeRemainingMigrations(connection, "accounts");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public CompletableFuture<@Nullable UltimateHammerAccount> loadAccount(UUID playerId) {
		return CompletableFuture.supplyAsync(() -> {
			try (PreparedStatement stm = Database.getConnection().prepareStatement("SELECT * FROM negativity_accounts WHERE id = ?")) {
				stm.setString(1, playerId.toString());
				ResultSet result = stm.executeQuery();
				if (result.next()) {
					String playerName = result.getString("playername");
					String language = result.getString("language");
					return new UltimateHammerAccount(playerId, playerName, language);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return null;
		});
	}

	@Override
	public CompletableFuture<Void> saveAccount(UltimateHammerAccount account) {
		return CompletableFuture.runAsync(() -> {
			try (PreparedStatement stm = Database.getConnection().prepareStatement(
					"REPLACE INTO negativity_accounts (id, playername, language) VALUES (?, ?, ?)")) {
				stm.setString(1, account.getPlayerId().toString());
				stm.setString(2, account.getPlayerName());
				stm.setString(3, account.getLang());
				stm.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		});
	}
}
