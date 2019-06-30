package me.ofido.OfirsEssentials.commands;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import me.ofido.OfirsEssentials.Main;

public class SetHub implements CommandExecutor{
	
	@SuppressWarnings("unused")
	private Main plugin;
	public SetHub(Main plugin) {
		this.plugin = plugin;
		plugin.getCommand("setspawn").setExecutor(this);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!(sender instanceof Player)) {
			sender.sendMessage("Only players allowed.");
			return true;
		}
		
		Player player = (Player) sender;
		
		if(player.hasPermission("hub.set")) {
			String worldNmae = player.getWorld().getName();
			File configFile = new File(Bukkit.getServer().getPluginManager().getPlugin(Main.folderName()).getDataFolder(), "hubConfig.yml");
			FileConfiguration configYML = YamlConfiguration.loadConfiguration(configFile);
			configYML.set("worldName", worldNmae);
			
			if (!configFile.exists()) { // if no file, try again
				try {
					configYML.save(configFile);
	            } catch (IOException e) { // didnt work? tell the user
					player.sendMessage(ChatColor.DARK_RED + String.format("Something went wrong while creating the spawnpoint!"));
	                e.printStackTrace();
	            }
			}
			
			Location newSpawn = player.getLocation();
			Bukkit.getWorld(worldNmae).setSpawnLocation(newSpawn.getBlockX(), newSpawn.getBlockY(), newSpawn.getBlockZ());
			player.sendMessage(ChatColor.DARK_GREEN + String.format("Set spawn location to (%d, %d, %d)", newSpawn.getBlockX(), newSpawn.getBlockY(), newSpawn.getBlockZ()));
			
			return true;
		}
			
		
		return false;
	}
	
}