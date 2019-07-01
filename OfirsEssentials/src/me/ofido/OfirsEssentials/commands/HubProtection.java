package me.ofido.OfirsEssentials.commands;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import me.ofido.OfirsEssentials.Main;

public class HubProtection implements CommandExecutor{
	
	@SuppressWarnings("unused")
	private Main plugin;
	public HubProtection(Main plugin) {
		this.plugin = plugin;
		plugin.getCommand("hubprotection").setExecutor(this);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!(sender instanceof Player)) {
			sender.sendMessage("Only players allowed.");
			return true;
		}
		
		Player player = (Player) sender;

		if(player.hasPermission("hub.editRegions")) {
			if(args.length >= 1) {

				if (args[0].toLowerCase().equals("add".toLowerCase())) { // the add sub command
					if (args.length < 8) { // if not enough args given tell user
	    	        	Main.sendMessage(player, ChatColor.DARK_RED + "Usage: hubprotection add <regionName> <x1> <y1> <z1> <x2> <y2> <z2>");
	    	        	return false;
					}

					String regionName = args[1];
					ArrayList<Integer> cords = new ArrayList<Integer>();
					try { // inserting the cords to an arrayList

						cords.add(Integer.max(Integer.parseInt(args[2]), Integer.parseInt(args[5]))); // point 1 (bigger point)
						cords.add(Integer.max(Integer.parseInt(args[3]), Integer.parseInt(args[6])));
						cords.add(Integer.max(Integer.parseInt(args[4]), Integer.parseInt(args[7])));

						cords.add(Integer.min(Integer.parseInt(args[2]), Integer.parseInt(args[5]))); // point 2 (smaller point)
						cords.add(Integer.min(Integer.parseInt(args[3]), Integer.parseInt(args[6])));
						cords.add(Integer.min(Integer.parseInt(args[4]), Integer.parseInt(args[7])));
						
					} catch (Exception e) { // if error it means that it cannot parse so its not integer
	    	        	Main.sendMessage(player, ChatColor.DARK_RED + "ERROR: Please enter the cordinatade as integers.");
						return false;
					}
	
					createRegion(player, regionName,  cords); 
					return true;
				} else if (args[0].toLowerCase().equals("delete".toLowerCase())) { // the delete sub command
					if (args.length < 2) {
	    	        	Main.sendMessage(player, ChatColor.DARK_RED + "Usage: /hubprotection delete <regionName>");
	    	        	return false;
					}
					
					String regionName = args[1];
					deleteRegion(player, regionName);
					return true;
					
				} else if (args[0].toLowerCase().equals("regions".toLowerCase())) { // the regions sub command
					printRegions(player);
					return true;
				}
			} 
        	Main.sendMessage(player, "Subcommands: add, delete, regions");
    		return false;
    		
		}
		
    	Main.tellNoPermissionsCommand(player);
		return false;
		
	}
	
	public void createRegion(Player player, String regionName, ArrayList<Integer> cords) {
		try {
			String world_name = player.getWorld().getName();
			String start = world_name + "." + regionName;
			File regionsFile = new File(Bukkit.getServer().getPluginManager().getPlugin(Main.folderName()).getDataFolder(), "regions.yml");
			FileConfiguration regionsYML = YamlConfiguration.loadConfiguration(regionsFile);
			if (!regionsFile.exists()) { // if no file, try again
				try {
					regionsYML.save(regionsFile);
	            } catch (IOException e) { // didnt work? tell the user
					player.sendMessage(ChatColor.DARK_RED + String.format("Something went wrong while creating the regions file!"));
	                e.printStackTrace();
		        	return;
	            }
			}
			
        	if (!regionsYML.isConfigurationSection(world_name)) { // if the world wasn't created yet
        		regionsYML.createSection(world_name);
			}
        	regionsYML.set(start, cords);
           
        	regionsYML.save(regionsFile);
        } catch (Exception exception) {
        	Main.notifyPlayerError(player, "Failed to setup region");
        	exception.printStackTrace();
        	return;
        }
        
    	Main.sendMessage(player, ChatColor.DARK_GREEN + "Region created");
	}
	
	public void deleteRegion(Player player, String regionName) {
		try {
			String world_name = player.getWorld().getName();
			String start = world_name + "." + regionName;
			File regionsFile = new File(Bukkit.getServer().getPluginManager().getPlugin(Main.folderName()).getDataFolder(), "regions.yml");
			FileConfiguration regionsYML = YamlConfiguration.loadConfiguration(regionsFile);
			if (!regionsFile.exists()) { // if no file, try again
				try {
					regionsYML.save(regionsFile);
	            } catch (IOException e) { // didnt work? tell the user
					player.sendMessage(ChatColor.DARK_RED + String.format("Something went wrong while creating the regions file!"));
	                e.printStackTrace();
		        	return;
	            }
			}
			
			if (regionsYML.contains(start)) {
				regionsYML.set(start, null); 
				regionsYML.save(regionsFile);
			} else {
	        	Main.sendMessage(player, ChatColor.DARK_RED + "Region does not exist!");
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		Main.sendMessage(player, ChatColor.DARK_GREEN + "Deleted region " + regionName + " successfully!");
	}
	
	public void printRegions(Player player) {
		try {
			String currentWorld = "";
			File regionsFile = new File(Bukkit.getServer().getPluginManager().getPlugin(Main.folderName()).getDataFolder(), "regions.yml");
			FileConfiguration regionsYML = YamlConfiguration.loadConfiguration(regionsFile);
			if (!regionsFile.exists()) { // if no file, try again
				try {
					regionsYML.save(regionsFile);
	            } catch (IOException e) { // didnt work? tell the user
					player.sendMessage(ChatColor.DARK_RED + String.format("Something went wrong while creating the regions file!"));
	                e.printStackTrace();
		        	return;
	            }
			}
			for (String world: regionsYML.getKeys(false)) {
				currentWorld = ChatColor.DARK_AQUA + world + ": " + ChatColor.AQUA;
				for (String name: regionsYML.getConfigurationSection(world).getKeys(false)) {
					currentWorld += name + ", ";
				}
	        	Main.sendMessage(player, currentWorld);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}