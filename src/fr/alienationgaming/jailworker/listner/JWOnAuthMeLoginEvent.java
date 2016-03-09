package fr.alienationgaming.jailworker.listner;
import fr.alienationgaming.jailworker.JailWorker;

import org.bukkit.Location;
import fr.xephi.authme.events.LoginEvent;
import org.bukkit.util.Vector;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class JWOnAuthMeLoginEvent implements Listener {
	
	JailWorker plugin;
	public JWOnAuthMeLoginEvent(JailWorker jailworker) {
		plugin = jailworker;
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void OnLogin(LoginEvent event) {
		Player player = event.getPlayer();
		if (plugin.getJailConfig().contains("Prisoners." + player.getName())) {
			String jailName = plugin.getJailConfig().getString("Prisoners." + player.getName() + ".Prison");
			int remain = plugin.getJailConfig().getInt("Prisoners." + player.getName() + ".RemainingBlocks");
			World world = plugin.getServer().getWorld(plugin.getJailConfig().getString("Jails." + jailName + ".World"));
			Vector spawn = plugin.getJailConfig().getVector("Jails." + jailName + ".Location.PrisonerSpawn");
			player.sendMessage(plugin.toLanguage("info-command-prisonerorder", remain, plugin.getJailConfig().getString("Jails." + jailName + ".Type")));
			player.teleport(new Location(world, spawn.getX(), spawn.getY()+1, spawn.getZ()));
		}
		if (plugin.getJailConfig().contains("Queue." + player.getName())) {
			String jailName = plugin.getJailConfig().getString("Queue." + player.getName() + ".Prison");
			String Punisher = plugin.getJailConfig().getString("Queue." + player.getName() + ".Punisher");
			int blocks = plugin.getJailConfig().getInt("Queue." + player.getName() + ".PunishToBreak");
			String cause = plugin.getJailConfig().getString("Queue." + player.getName() + ".Cause");
			
			/* Get inventory */
			JWInventorySaver invSaver = new JWInventorySaver(plugin);
			
			/* Create Section prisoner for target */
			plugin.getJailConfig().set("Prisoners." + player.getName() + ".Prison", jailName);
			plugin.getJailConfig().set("Prisoners." + player.getName() + ".Punisher", Punisher);
			plugin.getJailConfig().set("Prisoners." + player.getName() + ".Date", utils.getDate());
			plugin.getJailConfig().set("Prisoners." + player.getName() + ".PreviousPosition", player.getLocation().toVector());
			plugin.getJailConfig().set("Prisoners." + player.getName() + ".PreviousWorld", player.getWorld().getName());
			plugin.getJailConfig().set("Prisoners." + player.getName() + ".PunishToBreak", blocks);
			plugin.getJailConfig().set("Prisoners." + player.getName() + ".RemainingBlocks", blocks);
			plugin.getJailConfig().set("Prisoners." + player.getName() + ".Cause", cause);
			plugin.getJailConfig().set("Prisoners." + player.getName() + ".Gamemode", player.getGameMode().name());
			if (player.getGameMode() == GameMode.CREATIVE)
				player.setGameMode(GameMode.SURVIVAL);
			invSaver.save(player);
			invSaver.clear(player);

			/* Send Player to jail */
			player.getInventory().clear();
			player.getEquipment().clear();
			Vector spawn = plugin.getJailConfig().getVector("Jails." + jailName + ".Location.PrisonerSpawn");
			World world = plugin.getServer().getWorld(plugin.getJailConfig().getString("Jails." + jailName + ".World"));
			player.teleport(new Location(world, spawn.getX(), spawn.getY()+1, spawn.getZ()));
			plugin.getServer().broadcastMessage(plugin.toLanguage("info-command-broadcastpunish", player.getName(), jailName, Punisher));
			plugin.getServer().broadcastMessage(plugin.toLanguage("info-command-broadcastcantear"));
			player.sendMessage(plugin.toLanguage("info-command-sendtojail", Punisher));
			if (!cause.isEmpty() && cause != "No Reason.")
				player.sendMessage(plugin.toLanguage("info-command-displayreason", cause));
			player.sendMessage(plugin.toLanguage("info-command-prisonerorder", blocks, plugin.getJailConfig().getString("Jails." + jailName + ".Type")));
			
			plugin.getJailConfig().set("Queue." + player.getName(), null);
			plugin.saveJailConfig();
			plugin.reloadJailConfig();
			
		}
	}
}
