package com.elikill58.ultimatehammer.universal.account;

import java.util.UUID;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

import org.checkerframework.checker.nullness.qual.Nullable;

public abstract class UltimateHammerAccountManager {

	private static UltimateHammerAccountManager manager = new SimpleAccountManager(true);
	public static UltimateHammerAccountManager getManager() {
		return manager;
	}
	public static void setManager(UltimateHammerAccountManager manager) {
		UltimateHammerAccountManager.manager = manager;
	}
	
	/**
	 * Gets the account associated to the given UUID wrapped in a CompletableFuture.
	 * <p>
	 * The account may be returned directly (if cached for example), or loaded via
	 * potential (slow) IO operations.
	 * <p>
	 * Ideally, cancelling the returned CompletableFuture will cancel the underlying
	 * (potentially slow) operations to avoid wasting resources.
	 *
	 * @param accountId the ID of the account to get
	 *
	 * @return the requested account wrapped in a CompletableFuture
	 *
	 * @see #getNow
	 */
	public abstract CompletableFuture<UltimateHammerAccount> get(UUID accountId);

	/**
	 * Gets the account associated to the given UUID in a blocking manner.
	 * <p>
	 * This is equivalent to calling {@code get(accountId).join()}
	 * <p>
	 * This method may throw {@link CompletionException} according to {@link CompletableFuture#join()},
	 * and is unlikely (but still may) throw {@link CancellationException}.
	 *
	 * @param accountId the ID of the account to get
	 *
	 * @return the requested account
	 */
	public UltimateHammerAccount getNow(UUID accountId) {
		return get(accountId).join();
	}

	/**
	 * Saves the account associated to the given UUID.
	 * The returned CompletableFuture will be completed once the save has been done,
	 * and may complete exceptionally if it failed in some way.
	 *
	 * @param accountId the ID of the account to save
	 *
	 * @return a CompletableFuture that will complete once the save has been done
	 */
	public abstract CompletableFuture<Void> save(UUID accountId);

	/**
	 * Makes this manager use the the given account's data instead of what it may have already.
	 *
	 * @param account the account to use
	 */
	public abstract void update(UltimateHammerAccount account);

	/**
	 * Indicates that the account for the given UUID can be forgotten by this manager.
	 * <p>
	 * What this method does is completely up to the implementing class, it may do nothing,
	 * or remove the account from a cache for example.
	 * <p>
	 * If an implementation caches accounts, the returned account would be the one removed from its cache.
	 *
	 * @param accountId the ID of the account to dispose
	 *
	 * @return the disposed account, if available
	 */
	@Nullable
	public abstract UltimateHammerAccount dispose(UUID accountId);
}
