package com.chromadrive.home;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

public class Main extends org.bukkit.plugin.java.JavaPlugin
{
  MyConfigManager manager;
  MyConfig homesConfig;
  
  public Main() {}
  
  public void onEnable()
  {
    manager = new MyConfigManager(this);
    homesConfig = manager.getNewConfig("Homes.yml");
  }
  

  public boolean onCommand(org.bukkit.command.CommandSender sender, Command cmd, String commandLabel, String[] args)
  {
    Player p = (Player)sender;
    String prefix = ChatColor.BLUE + "Home > " + ChatColor.GRAY;
    
    if (!p.hasPermission("home.sethome")) {
      p.sendMessage(prefix + ChatColor.RED + "You do not have permission!");
      return true;
    }
    
    if (cmd.getName().equalsIgnoreCase("sethome")) {
      homesConfig.set("Homes." + p.getName() + ".X", Double.valueOf(p.getLocation().getX()));
      homesConfig.set("Homes." + p.getName() + ".Y", Double.valueOf(p.getLocation().getY()));
      homesConfig.set("Homes." + p.getName() + ".Z", Double.valueOf(p.getLocation().getZ()));
      homesConfig.set("Homes." + p.getName() + ".Yaw", Float.valueOf(p.getLocation().getYaw()));
      homesConfig.set("Homes." + p.getName() + ".Pitch", Float.valueOf(p.getLocation().getPitch()));
      homesConfig.set("Homes." + p.getName() + ".World", p.getLocation().getWorld().getName());
      homesConfig.saveConfig();
      p.sendMessage(prefix + "You have set your new home!");
      return true;
    }
    
    if (!p.hasPermission("home.home")) {
      p.sendMessage(prefix + ChatColor.RED + "You do not have permission!");
      return true;
    }
    if (cmd.getName().equalsIgnoreCase("home"))
    {
      if (homesConfig.getString("Homes." + p.getName()) == null) {
        p.sendMessage(prefix + ChatColor.RED + "Use " + ChatColor.GREEN + "/sethome " + ChatColor.RED + "to set a home first.");
        return true;
      }
      
      int homeX = homesConfig.getInt("Homes." + p.getName() + ".X");
      int homeY = homesConfig.getInt("Homes." + p.getName() + ".Y");
      int homeZ = homesConfig.getInt("Homes." + p.getName() + ".Z");
      int homeYaw = homesConfig.getInt("Homes." + p.getName() + ".Yaw");
      int homePitch = homesConfig.getInt("Homes." + p.getName() + ".Pitch");
      org.bukkit.World world = org.bukkit.Bukkit.getWorld(homesConfig.getString("Homes." + p.getName() + ".World"));
      Location home = new Location(world, homeX, homeY, homeZ, homeYaw, homePitch);
      p.teleport(home);
      p.sendMessage(prefix + "You have been sent home!");
      return true;
    }
    

    return false;
  }
}
