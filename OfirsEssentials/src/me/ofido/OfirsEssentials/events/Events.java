package me.ofido.OfirsEssentials.events;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import me.ofido.OfirsEssentials.Main;

public class Events implements Listener{

	@EventHandler
    public void onInteract(PlayerInteractEvent event){
		Player player = event.getPlayer();
        Action action = event.getAction();
        World world = player.getWorld();
        
		if (!player.hasPermission("hub.protected.build")) {
			boolean allowed = true;
			try {
				if(action == Action.LEFT_CLICK_BLOCK || action == Action.RIGHT_CLICK_BLOCK){

	            	if (isInRegion(player.getLocation(), world)) {
	            		allowed = false;
	    			}
	        	}
			} catch (Exception e) {
				System.out.println(e);
			}
			
        	if (!allowed) {
            	Main.tellNoPermissions(player);
				event.setCancelled(true);
			}
		}
    }
	
	@EventHandler
    public void onPlayerDamage(final EntityDamageEvent event){
        World world = event.getEntity().getWorld();
		try {
			if (event.getEntityType() == EntityType.PLAYER && isInRegion(event.getEntity().getLocation(), world)) {
				Player player = (Player) event.getEntity();
				if (player.getFireTicks() > 0) {
					player.setFireTicks(0);
				}
				event.setCancelled(true);
			}
		} catch (Exception e) {
			System.out.println(e);
		}
    }
	
	@EventHandler
    public void onPlayerHungerLoose(final FoodLevelChangeEvent event){
        World world = event.getEntity().getWorld();
		try {
			if (isInRegion(event.getEntity().getLocation(), world)) {
				event.setCancelled(true);
			}
		} catch (Exception e) {
			System.out.println(e);
		}
    }
	
	@EventHandler
    public void onMobSpawn(CreatureSpawnEvent event){
        World world = event.getEntity().getWorld();
		try {
			if (event.getSpawnReason() != CreatureSpawnEvent.SpawnReason.SPAWNER_EGG && isInRegion(event.getEntity().getLocation(), world)) {
				event.setCancelled(true);
			}
		} catch (Exception e) {
			System.out.println(e);
		}
    }
	
	@EventHandler
    public void onFireSpread(BlockSpreadEvent event){
        World world = event.getBlock().getWorld();
		try {
			if (isInRegion(event.getBlock().getLocation(), world)) {
				event.setCancelled(true);
			}
		} catch (Exception e) {
			System.out.println(e);
		}
    }
	
	@EventHandler
    public void onBlockBurn(BlockBurnEvent event){
        World world = event.getBlock().getWorld();
		try {
			if (isInRegion(event.getBlock().getLocation(), world)) {
				event.setCancelled(true);
			}
		} catch (Exception e) {
			System.out.println(e);
		}
    }
	
	private boolean isInRegion(Location locationInRegion, World world) {
		boolean inRegion = false;
        File regionsFile = new File(Bukkit.getServer().getPluginManager().getPlugin(Main.folderName()).getDataFolder(), "regions.yml");
		FileConfiguration regionsYml = YamlConfiguration.loadConfiguration(regionsFile);
		
		if (!regionsFile.exists()) {
			try {
				regionsYml.save(regionsFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
		}

		if (regionsYml.getKeys(false).size() != 0) {
			try {
				for (String name: regionsYml.getConfigurationSection(world.getName()).getKeys(false)) {
					ArrayList<Integer> point;
					String path = world.getName() + "." + name + ".point";
					
					point = getPoint(regionsYml, path + "1");
					Location point1 = new Location(world, point.get(0), point.get(1), point.get(2));
					
					point = getPoint(regionsYml, path + "2");
					Location point2 = new Location(world, point.get(0), point.get(1), point.get(2));
	
					if (locationInRegion.getX() <= point1.getX()
					        && locationInRegion.getX() >= point2.getX()
					        && locationInRegion.getY() <= point1.getY()
					        && locationInRegion.getY() >= point2.getY()
					        && locationInRegion.getZ() <= point1.getZ()
					        && locationInRegion.getZ() >= point2.getZ()) {
						inRegion = true;
						break;
					}
				}
			} catch (Exception e) {
				System.out.println("ERROR in the isInRegion function:\n" + e.getStackTrace());
			}
		}
		return (inRegion);
	}
	
	private ArrayList<Integer> getPoint(FileConfiguration regionsData, String path) {
		ArrayList<Integer> point = new ArrayList<Integer>();
		for (String key: regionsData.getConfigurationSection(path).getKeys(false)) {
			point.add(regionsData.getConfigurationSection(path).getInt(key));
		}
		return point;
	}
}
