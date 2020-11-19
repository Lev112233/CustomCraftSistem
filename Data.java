
import java.io.File;
import java.io.IOException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class Data {
  private FileConfiguration config;
  
  private File file;
  
  public Data(File paramFile) {
    this.file = paramFile;
    this.config = (FileConfiguration)YamlConfiguration.loadConfiguration(paramFile);
  }
  
  public void reload() {
    this.config = (FileConfiguration)YamlConfiguration.loadConfiguration(this.file);
  }
  
  public FileConfiguration getConfig() {
    return this.config;
  }
  
  public void save() {
    try {
      this.config.save(this.file);
    } catch (IOException localIOException) {
      localIOException.printStackTrace();
    } 
  }
}
