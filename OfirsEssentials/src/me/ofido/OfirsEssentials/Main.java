package me.ofido.OfirsEssentials;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import me.ofido.OfirsEssentials.commands.Hub;
import me.ofido.OfirsEssentials.commands.HubProtection;
import me.ofido.OfirsEssentials.commands.SetHub;
import me.ofido.OfirsEssentials.events.Events;

public class Main extends JavaPlugin{
	
	@Override
	public void onEnable() {
		new Hub (this);
		new HubProtection (this);
		new SetHub (this);
		getServer().getPluginManager().registerEvents(new Events(), this);
	}
	
	public static void sendMessage(Player player, String msg)
    {
        String prefix = "" + ChatColor.GOLD + ChatColor.BOLD + "[OfirsEssentials]" + ChatColor.RESET + ChatColor.GRAY;
        player.sendMessage(prefix + msg);
    }
	
	public static void tellNoPermissionsCommand(Player player)
    {
        String noPermissionMsg = ChatColor.DARK_RED + "You don't have permission to execute that command!";
        sendMessage(player, noPermissionMsg);
    }

	public static void tellNoPermissions(Player player)
    {
        String noPermissionMsg = ChatColor.DARK_RED + "You don't have permission to do that!";
        sendMessage(player, noPermissionMsg);
    }
	public static void notifyPlayerError(Player player, String msg)
    {
		sendMessage(player, ChatColor.DARK_RED + "ERROR: " + ChatColor.DARK_RED + msg);
		sendMessage(player, ChatColor.DARK_RED + "PLEASE report this to the developer of the plugin.");
    }
	
	public static String folderName() {
		return "OfirsEssentials";
	}
}
