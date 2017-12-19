package com.downthepark.sethome;

import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.Set;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;




public class MyConfig
{
  private int comments;
  private MyConfigManager manager;
  private File file;
  private FileConfiguration config;
  
  public MyConfig(InputStream configStream, File configFile, int comments, JavaPlugin plugin)
  {
    this.comments = comments;
    manager = new MyConfigManager(plugin);
    file = configFile;
    config = YamlConfiguration.loadConfiguration(configStream);
  }
  
  public Object get(String path) { return config.get(path); }
  
  public Object get(String path, Object def) { return config.get(path, def); }
  
  public String getString(String path) { return config.getString(path); }
  
  public String getString(String path, String def) { return config.getString(path, def); }
  
  public int getInt(String path) { return config.getInt(path); }
  
  public int getInt(String path, int def) { return config.getInt(path, def); }
  
  public boolean getBoolean(String path) { return config.getBoolean(path); }
  
  public boolean getBoolean(String path, boolean def) { return config.getBoolean(path, def); }
  
  public void createSection(String path) { config.createSection(path); }
  
  public ConfigurationSection getConfigurationSection(String path) { return config.getConfigurationSection(path); }
  
  public double getDouble(String path) { return config.getDouble(path); }
  
  public double getDouble(String path, double def) { return config.getDouble(path, def); }
  
  public List<?> getList(String path) { return config.getList(path); }
  
  public List<?> getList(String path, List<?> def) { return config.getList(path, def); }
  
  public boolean contains(String path) { return config.contains(path); }
  
  public void removeKey(String path) { config.set(path, null); }
  
  public void set(String path, Object value) { config.set(path, value); }
  
  public void set(String path, Object value, String comment)
  {
    if (!config.contains(path))
    {
      config.set(manager.getPluginName() + "_COMMENT_" + comments, " " + comment);
      comments += 1;
    }
    config.set(path, value);
  }
  
  public void set(String path, Object value, String[] comment)
  {
    for (String comm : comment)
    {
      if (!config.contains(path))
      {
        config.set(manager.getPluginName() + "_COMMENT_" + comments, " " + comm);
        comments += 1;
      }
    }
    config.set(path, value);
  }
  
  public void setHeader(String[] header)
  {
    manager.setHeader(file, header);
    comments = (header.length + 2);
    reloadConfig();
  }
  
  public void reloadConfig() {
    config = YamlConfiguration.loadConfiguration(manager.getConfigContent(file));
  }
  
  public void saveConfig() {
    String config = this.config.saveToString();
    manager.saveConfig(config, file);
  }
  
  public Set<String> getKeys() { return config.getKeys(false); }
}
