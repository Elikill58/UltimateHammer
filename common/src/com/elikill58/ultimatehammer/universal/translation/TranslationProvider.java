package com.elikill58.ultimatehammer.universal.translation;

import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Provides messages, usually from a specific language.
 */
public interface TranslationProvider {

	/**
	 * Returns a message of a single line.
	 * @param key the key of the requested message
	 */
	@Nullable
	String get(String key);

	/**
	 * Returns a message of a single line.
	 * @param key the key of the requested message
	 * @param placeholders the placeholders to use in this message
	 */
	@Nullable
	default String get(String key, Object... placeholders) {
		String rawMessage = get(key);
		if (rawMessage == null || placeholders.length == 0) {
			return rawMessage;
		}
		return applyPlaceholders(rawMessage, placeholders);
	}
	
	/**
	 * Applied placeholders to the given raw message.
	 * @param raw the raw message to process
	 * @param placeholders placeholders to use
	 * @return the message with placeholders applied
	 */
	String applyPlaceholders(String raw, Object... placeholders);
}
