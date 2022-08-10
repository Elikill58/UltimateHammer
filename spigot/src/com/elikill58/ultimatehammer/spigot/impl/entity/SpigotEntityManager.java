package com.elikill58.ultimatehammer.spigot.impl.entity;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.IronGolem;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.Nullable;

import com.elikill58.ultimatehammer.api.UltimateHammerPlayer;
import com.elikill58.ultimatehammer.api.commands.CommandSender;
import com.elikill58.ultimatehammer.api.entity.Entity;

public class SpigotEntityManager {

	public static com.elikill58.ultimatehammer.api.entity.@Nullable Player getPlayer(@Nullable Player p){
		return p == null ? null : UltimateHammerPlayer.getUltimateHammerPlayer(p.getUniqueId(), () -> new SpigotPlayer(p)).getPlayer();
	}
	
	public static @Nullable Entity getEntity(org.bukkit.entity.@Nullable Entity bukkitEntity) {
		if(bukkitEntity == null)
			return null;
		switch (bukkitEntity.getType()) {
		case PLAYER:
			return getPlayer((Player) bukkitEntity);
		case IRON_GOLEM:
			return new SpigotIronGolem((IronGolem) bukkitEntity);
		case ARROW:
			return new SpigotArrow((Arrow) bukkitEntity);
		default:
			return new SpigotEntity<>(bukkitEntity);
		}
	}
	
	public static @Nullable Entity getProjectile(org.bukkit.projectiles.@Nullable ProjectileSource bukkitEntity) {
		if(bukkitEntity == null)
			return null;
		if(bukkitEntity instanceof Player)
			return getPlayer((Player) bukkitEntity);
		else if(bukkitEntity instanceof org.bukkit.entity.Entity)
			return new SpigotEntity<>((org.bukkit.entity.Entity) bukkitEntity);
		return null;
	}

	public static @Nullable CommandSender getExecutor(org.bukkit.command.@Nullable CommandSender sender) {
		if(sender == null)
			return null;
		if(sender instanceof Player)
			return getPlayer((Player) sender);
		return new SpigotCommandSender(sender);
	}
}
