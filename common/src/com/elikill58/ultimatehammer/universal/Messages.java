package com.elikill58.ultimatehammer.universal;

import java.util.UUID;

import com.elikill58.ultimatehammer.api.commands.CommandSender;
import com.elikill58.ultimatehammer.api.entity.Player;
import com.elikill58.ultimatehammer.api.utils.Utils;
import com.elikill58.ultimatehammer.universal.account.UltimateHammerAccount;

public class Messages {
	
	/**
	 * Get a message with the default lang
	 * 
	 * @param dir the message key
	 * @param placeholders all message placeholders
	 * @return the translated message
	 */
	public static String getMessage(String dir, Object... placeholders) {
		String message = TranslatedMessages.getStringFromLang(TranslatedMessages.getDefaultLang(), dir, placeholders);
		return Utils.coloredMessage(message);
	}

	/**
	 * Get a message with the default lang
	 * 
	 * @param uuid the UUID of the player which will receive the message
	 * @param dir the message key
	 * @param placeholders all message placeholders
	 * @return the translated message
	 */
	public static String getMessage(UUID uuid, String dir, Object... placeholders) {
		String message = TranslatedMessages.getStringFromLang(UltimateHammerAccount.get(uuid).getLang(), dir, placeholders);
		return Utils.coloredMessage(message);
	}

	/**
	 * Get a message with the default lang
	 * 
	 * @param sender the sender which will receive the message
	 * @param dir the message key
	 * @param placeholders all message placeholders
	 * @return the translated message
	 */
	public static String getMessage(CommandSender sender, String dir, Object... placeholders) {
		String lang = (sender instanceof Player ? TranslatedMessages.getLang(((Player) sender).getUniqueId()) : TranslatedMessages.DEFAULT_LANG);
		String message = TranslatedMessages.getStringFromLang(lang, dir, placeholders);
		return Utils.coloredMessage(message);
	}

	/**
	 * Get a message with the default lang
	 * 
	 * @param account the account which will receive the message
	 * @param dir the message key
	 * @param placeholders all message placeholders
	 * @return the translated message
	 */
	public static String getMessage(UltimateHammerAccount account, String dir, Object... placeholders) {
		String message = TranslatedMessages.getStringFromLang(account.getLang(), dir, placeholders);
		return Utils.coloredMessage(message);
	}

	/**
	 * Send a message to the given command sender
	 * 
	 * @param p the sender which will receive the message
	 * @param dir the message keye
	 * @param placeholders all message placeholders
	 */
	public static void sendMessage(CommandSender p, String dir, Object... placeholders) {
		p.sendMessage(getMessage(p, dir, placeholders));
	}
	
	public static void broadcastMessage(String dir, Object... placeholders) {
		Adapter.getAdapter().getOnlinePlayers().forEach((p) -> sendMessage(p, dir, placeholders));
	}
}
