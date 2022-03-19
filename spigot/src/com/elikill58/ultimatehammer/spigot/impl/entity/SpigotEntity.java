package com.elikill58.ultimatehammer.spigot.impl.entity;

import java.lang.reflect.Field;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.LivingEntity;

import com.elikill58.ultimatehammer.api.entity.AbstractEntity;
import com.elikill58.ultimatehammer.api.entity.BoundingBox;
import com.elikill58.ultimatehammer.api.entity.EntityType;
import com.elikill58.ultimatehammer.api.location.Location;
import com.elikill58.ultimatehammer.api.location.Vector;
import com.elikill58.ultimatehammer.spigot.impl.location.SpigotLocation;
import com.elikill58.ultimatehammer.spigot.utils.PacketUtils;
import com.elikill58.ultimatehammer.universal.Version;

public class SpigotEntity<E extends Entity> extends AbstractEntity {

	protected final E entity;
	
	public SpigotEntity(E entity) {
		this.entity = entity;
	}

	@Override
	public boolean isOnGround() {
		return entity.isOnGround();
	}

	@Override
	public boolean isOp() {
		return entity.isOp();
	}

	@Override
	public Location getLocation() {
		return SpigotLocation.toCommon(entity.getLocation());
	}

	@Override
	public double getEyeHeight() {
		return entity.getHeight();
	}

	@Override
	public EntityType getType() {
		return EntityType.get(entity == null ? null : entity.getType().name());
	}

	@Override
	public E getDefault() {
		return entity;
	}

	@Override
	public void sendMessage(String msg) {
		if(entity instanceof CommandSender)
			((CommandSender) entity).sendMessage(msg);
	}

	@Override
	public String getName() {
		if(entity instanceof HumanEntity) // prevent 1.7 error
			return ((HumanEntity) entity).getName();
		return entity.getName();
	}
	
	@Override
	public Location getEyeLocation() {
		if(entity instanceof LivingEntity) {
			return SpigotLocation.toCommon(((LivingEntity) entity).getEyeLocation());
		}
		return null;
	}
	
	@Override
	public Vector getRotation() {
		org.bukkit.util.Vector vec = entity.getLocation().getDirection();
		return new Vector(vec.getX(), vec.getY(), vec.getZ());
	}

	@Override
	public Vector getTheoricVelocity() {
		org.bukkit.util.Vector vel = entity.getVelocity();
		return new Vector(vel.getX(), vel.getY(), vel.getZ());
	}
	
	@Override
	public void setVelocity(Vector vel) {
		entity.setVelocity(new org.bukkit.util.Vector(vel.getX(), vel.getY(), vel.getZ()));
	}
	
	@Override
	public String getEntityId() {
		return String.valueOf(entity.getEntityId());
	}
	
	@Override
	public boolean isSameId(String id) {
		return String.valueOf(entity.getEntityId()).equalsIgnoreCase(id) || entity.getUniqueId().toString().equalsIgnoreCase(id);
	}
	
	@Override
	public boolean isSameId(com.elikill58.ultimatehammer.api.entity.Entity other) {
		return getEntityId() == other.getEntityId() || entity.getUniqueId().equals(((Entity) other.getDefault()).getUniqueId());
	}
	
	@Override
	public BoundingBox getBoundingBox() {
		try {
			Object bb = PacketUtils.getBoundingBox(entity);
			Class<?> clss = bb.getClass();
			boolean hasMinField = false;
			for(Field f : clss.getFields())
				if(f.getName().equalsIgnoreCase("minX"))
					hasMinField = true;
			if(Version.getVersion().isNewerOrEquals(Version.V1_13) && hasMinField) {
				double minX = clss.getField("minX").getDouble(bb);
				double minY = clss.getField("minY").getDouble(bb);
				double minZ = clss.getField("minZ").getDouble(bb);
				
				double maxX = clss.getField("maxX").getDouble(bb);
				double maxY = clss.getField("maxY").getDouble(bb);
				double maxZ = clss.getField("maxZ").getDouble(bb);
				
				return new BoundingBox(minX, minY, minZ, maxX, maxY, maxZ);
			} else {
				double minX = clss.getField("a").getDouble(bb);
				double minY = clss.getField("b").getDouble(bb);
				double minZ = clss.getField("c").getDouble(bb);
				
				double maxX = clss.getField("d").getDouble(bb);
				double maxY = clss.getField("e").getDouble(bb);
				double maxZ = clss.getField("f").getDouble(bb);
				
				return new BoundingBox(minX, minY, minZ, maxX, maxY, maxZ);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
