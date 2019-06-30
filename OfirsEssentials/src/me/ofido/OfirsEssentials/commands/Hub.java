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

public class Hub implements CommandExecutor{
	
	@SuppressWarnings("unused")
	private Main plugin;
	public Hub(Main plugin) {
		this.plugin = plugin;
		plugin.getCommand("hub").setExecutor(this);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!(sender instanceof Player)) {
			sender.sendMessage("Only players allowed.");
			return true;
		}
		Player player = (Player) sender;
		try {
			if (args.length >= 1 && player.hasPermission("")) { 
				String playerName = args[0];
				player = Bukkit.getServer().getPlayer(playerName);
			}
			
			File configFile = new File(Bukkit.getServer().getPluginManager().getPlugin(Main.folderName()).getDataFolder(), "hubConfig.yml");
			FileConfiguration configYML = YamlConfiguration.loadConfiguration(configFile);
			try {
				String worldName = configYML.getString("worldName");
				Location spawn = Bukkit.getWorld(worldName).getSpawnLocation();
				spawn.setX(spawn.getX() + 0.5);
				spawn.setZ(spawn.getZ() + 0.5);
				Main.sendMessage(player, "Sending you to the spawn");
				player.teleport(spawn);
			} catch (Exception e) {
				Main.sendMessage(player, ChatColor.DARK_RED + "Can't find spawn in the config file! Please set it via /setspawn");
				return false;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return true;
	}
	
}