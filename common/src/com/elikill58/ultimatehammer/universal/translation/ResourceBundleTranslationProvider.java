package com.elikill58.ultimatehammer.universal.translation;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.checkerframework.checker.nullness.qual.Nullable;

import com.elikill58.ultimatehammer.universal.utils.UniversalUtils;

public class ResourceBundleTranslationProvider implements TranslationProvider {

	private final ResourceBundle bundle;

	public ResourceBundleTranslationProvider(ResourceBundle bundle) {
		this.bundle = bundle;
	}

	@Nullable
	@Override
	public String get(String key) {
		try {
			return bundle.getString(key);
		} catch (MissingResourceException ignore) {
		}
		return null;
	}

	@Override
	public String applyPlaceholders(String raw, Object... placeholders) {
		return UniversalUtils.replacePlaceholders(raw, placeholders);
	}
}
