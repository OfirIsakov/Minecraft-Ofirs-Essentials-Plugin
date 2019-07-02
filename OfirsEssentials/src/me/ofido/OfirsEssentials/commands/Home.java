package me.ofido.OfirsEssentials.commands;

import java.io.File;

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

public class Home implements CommandExecutor{
	
	@SuppressWarnings("unused")
	private Main plugin;
	public Home(Main plugin) {
		this.plugin = plugin;
		plugin.getCommand("home").setExecutor(this);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!(sender instanceof Player)) {
			sender.sendMessage("Only players allowed.");
			return true;
		}
		Player player = (Player) sender;

		if(player.hasPermission("ofirsessentials.home.teleport")) {
			try {
				File configFile = new File(Bukkit.getServer().getPluginManager().getPlugin(Main.folderName()).getDataFolder(), "playerHomes.yml");
				FileConfiguration configYML = YamlConfiguration.loadConfiguration(configFile);
				
				String playerName = player.getName();
				if (args.length >= 1 && player.hasPermission("ofirsessentials.home.teleport.to.other")) { 
					playerName = args[0];
				}
				try {
					Location home = (Location) configYML.get(playerName);
					if (home != null) {
						Main.sendMessage(player, "Sending you to your home.", "HOME");
						player.teleport(home);
					} else {
						Main.sendMessage(player, ChatColor.DARK_RED + "Can't find your home! Please set it via /sethome", "HOME");
					}
				} catch (Exception e) {
					Main.sendMessage(player, ChatColor.DARK_RED + "Can't find your home! Please set it via /sethome", "HOME");
					return false;
				}
				
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
			return true;
		} else {
			Main.tellNoPermissionsCommand(player);
		}
		return false;
	}
	
}