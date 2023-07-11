package com.elikill58.ultimatehammer.api.nms;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import javax.annotation.Nullable;

import com.elikill58.ultimatehammer.api.item.ItemStack;
import com.elikill58.ultimatehammer.universal.Adapter;

public abstract class VersionAdapter<R> {

	protected final String version;
	
	public VersionAdapter(String version) {
		this.version = version;
	}
	
	public String getVersion() {
		return version;
	}
	
	public abstract ItemStack setNbtTag(ItemStack item, String key, String val);
	
	public abstract ItemStack setNbtTag(ItemStack item, String key, int val);

	/**
	 * Check if the given item has key/val as nbt tag
	 * 
	 * @param item the item to check
	 * @param key the key of nbt tag
	 * @param val the search value
	 * @return true if key/val is same as on item
	 */
	public boolean hasNbtTagValue(ItemStack item, String key, String val) {
		String real = getNbtTagValue(item, key);
		return real != null && real.equalsIgnoreCase(val);
	}
	
	/**
	 * Get nbt tag value for given key on item
	 * 
	 * @param item the item to check
	 * @param key the key of nbt tag
	 * @return the nbt tag value
	 */
	public abstract @Nullable String getNbtTagValue(ItemStack item, String key);

	protected <T> T get(Class<?> clazz, Object obj, String name) {
		try {
			Field f = clazz.getDeclaredField(name);
			f.setAccessible(true);
			return (T) f.get(obj);
		} catch (NoSuchFieldException e) { // prevent issue when wrong version
			Adapter.getAdapter().debug("Failed to find field " + name + " in class " + obj.getClass().getSimpleName());
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	protected <T> T getFromMethod(Class<?> clazz, Object obj, String methodName) {
		try {
			Method f = clazz.getDeclaredMethod(methodName);
			f.setAccessible(true);
			return (T) f.invoke(obj);
		} catch (NoSuchMethodException e) { // prevent issue when wrong version
			Adapter.getAdapter().debug("Failed to find method " + methodName + " in class " + obj.getClass().getSimpleName());
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
