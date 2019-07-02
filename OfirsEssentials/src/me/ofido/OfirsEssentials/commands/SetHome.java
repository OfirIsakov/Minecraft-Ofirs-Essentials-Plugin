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

public class SetHome implements CommandExecutor{
	
	@SuppressWarnings("unused")
	private Main plugin;
	public SetHome(Main plugin) {
		this.plugin = plugin;
		plugin.getCommand("sethome").setExecutor(this);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!(sender instanceof Player)) {
			sender.sendMessage("Only players allowed.");
			return true;
		}
		
		Player player = (Player) sender;
		
		if(player.hasPermission("ofirsessentials.home.set")) {
			Location newHome = player.getLocation();
			File configFile = new File(Bukkit.getServer().getPluginManager().getPlugin(Main.folderName()).getDataFolder(), "playerHomes.yml");
			FileConfiguration configYML = YamlConfiguration.loadConfiguration(configFile);
			configYML.set(player.getName(), newHome);
			
			try { // saving new home
				configYML.save(configFile);
            } catch (IOException e) {  // didnt work? tell the user
				player.sendMessage(ChatColor.DARK_RED + String.format("Something went wrong while creating the home!"));
                e.printStackTrace();
                return false;
            }

			Main.sendMessage(player, ChatColor.DARK_GREEN + String.format("Set home location to (%d, %d, %d)", newHome.getBlockX(), newHome.getBlockY(), newHome.getBlockZ()), "HOME");
			return true;
		} else {
			Main.tellNoPermissionsCommand(player);
			return false;
		}
	}
	
}