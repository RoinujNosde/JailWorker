package fr.alienationgaming.jailworker.listner;
// TODO: Fazer com que este evento seja opcional
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
	public void OnPlayerJoin(LoginEvent event) {
		Player player = event.getPlayer();
		if (plugin.getJailConfig().contains("Prisoners." + player.getName())) {
			String jailName = plugin.getJailConfig().getString("Prisoners." + player.getName() + ".Prison");
			int remain = plugin.getJailConfig().getInt("Prisoners." + player.getName() + ".RemainingBlocks");
			World world = plugin.getServer().getWorld(plugin.getJailConfig().getString("Jails." + jailName + ".World"));
			Vector spawn = plugin.getJailConfig().getVector("Jails." + jailName + ".Location.PrisonerSpawn");
			player.sendMessage(plugin.toLanguage("info-command-prisonerorder", remain, plugin.getJailConfig().getString("Jails." + jailName + ".Type")));
			player.teleport(new Location(world, spawn.getX(), spawn.getY()+1, spawn.getZ()));
		}
	}
}