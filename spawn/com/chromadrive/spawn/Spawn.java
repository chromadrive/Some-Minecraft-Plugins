package com.chromadrive.spawn;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;


public class Spawn
  extends org.bukkit.plugin.java.JavaPlugin
{
  public Spawn() {}
  
  public void onEnable() {
    saveDefaultConfig();
  }
  
  public boolean onCommand(org.bukkit.command.CommandSender sender, org.bukkit.command.Command cmd, String label, String[] args) {
    Player player = (Player)sender;
    if (label.equalsIgnoreCase("setspawn")) {
      if (player.hasPermission("spawn.setspawn")) {
        if (args.length == 0) {
          String x = String.valueOf(player.getLocation().getX());
          String y = String.valueOf(player.getLocation().getY());
          String z = String.valueOf(player.getLocation().getZ());
          getConfig().set("spawn.xPos", x);
          getConfig().set("spawn.world", player.getWorld().getName());
          getConfig().set("spawn.yPos", y);
          getConfig().set("spawn.zPos", z);
          saveConfig();
          player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6Spawn has been set at &4X:" + x + " &4Y:" + y + " &4Z:" + z + " &6in the world: &4" + player.getWorld().getName()));
        }
      } else {
        player.sendMessage("You do not have permission to set the spawn!");
      }
    }
    if (label.equalsIgnoreCase("spawn")) {
      if (getConfig().contains("spawn.")) {
        teleport(player);
        player.sendMessage(ChatColor.GOLD + "You have been teleported to" + ChatColor.RED + " spawn.");
      } else {
        player.sendMessage(ChatColor.GOLD + "Spawn does not appear to have ben set! " + ChatColor.RED + "/setspawn");
      }
    }
    return true;
  }
  
  public void onPlayerJoin(PlayerJoinEvent e) { Player player = e.getPlayer();
    teleport(player);
  }
  
  public void teleport(Player player) { double y = Double.parseDouble(getConfig().getString("spawn.yPos"));
    double x = Double.parseDouble(getConfig().getString("spawn.xPos"));
    double z = Double.parseDouble(getConfig().getString("spawn.zPos"));
    World world = org.bukkit.Bukkit.getWorld((String)getConfig().get("spawn..world"));
    Location warpLoc = new Location(world, x, y, z);
    player.teleport(warpLoc);
  }
}
