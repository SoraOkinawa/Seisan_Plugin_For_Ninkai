package me.Seisan.plugin.Features.data;

import me.Seisan.plugin.Main;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Set;

/**
 * Created by Helliot on 03/08/2017.
 */
public class Config {

    private YamlConfiguration yamlFile;
    private File configFile;

    private static String spigotPath;
    private static JavaPlugin spigot = Main.plugin();

    /**
     * Defines static values of the class
     */
    public static void enable() {
        try {
            if (!spigot.getDataFolder().exists()) {
                spigot.getDataFolder().mkdir();
            }
            spigotPath = spigot.getDataFolder().getCanonicalPath();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Construct a Config object
     * @param fileName The name of the config file
     */
    public Config(String fileName) {
        if (spigotPath == null) {
            enable();
        }

        File file = new File(spigotPath, fileName);
        if (!file.exists()) {
            try (InputStream in = spigot.getResource(fileName)) { //Loading defaults if existing
                Files.copy(in, file.toPath());
            } catch (IOException e) {
                spigot.getLogger().severe("File " + fileName + " defaults not copied from plugin. Reason: File defaults not found");
                e.printStackTrace();
            } catch (NullPointerException e){
                spigot.getLogger().severe("File " + fileName + " defaults not copied from plugin. Reason: File defaults not found");
            }

        }
        configFile = file;
        yamlFile = YamlConfiguration.loadConfiguration(file);

    }

    /**
     * Gives the int value of a given key
     * @param key
     * @return int
     */
    public int getInt(String key) {
        return yamlFile.getInt(key);
    }

    /**
     * Gives the string value of a given key
     * @param key
     * @return String
     */
    public String getString(String key) {
        return yamlFile.getString(key);
    }

    /**
     * Gives the double value of a given key
     * @param key
     * @return double
     */
    public double getDouble(String key) {
        return yamlFile.getDouble(key);
    }

    /**
     * Gives the long value of a given key
     * @param key
     * @return long
     */
    public long getLong(String key) {
        return yamlFile.getLong(key);
    }

    /**
     * Gives the char value of a given key
     * @param key
     * @return char
     */
    public char getChar(String key) {
        return yamlFile.getString("key").toCharArray()[0]; //According to the fact that the string is 1 length
    }

    /**
     * Returns the array list stored in the given key
     * @param key
     * @return
     */
    public ArrayList<String> getStringList(String key) {
        return new ArrayList<>(yamlFile.getStringList(key));
    }

    /**
     * Gives the value of a given key
     * @param key
     * @return Object
     */
    public Object getObject(String key) {
        return yamlFile.get(key);
    }

    /**
     * Returns true if the given key has a value set in config file, false if not.
     * @param key
     * @return
     */

    public boolean getBoolean(String key){ return yamlFile.getBoolean(key);}

    public boolean isInConfig(String key) {
        return yamlFile.isSet(key);
    }

    public void reloadFile(){ yamlFile = YamlConfiguration.loadConfiguration(configFile);}

    public Set<String> getRootKeys(){ return yamlFile.getConfigurationSection("").getKeys(false);}

    /**
     * Sets a given value to a given key and then physically save the file
     * @param key
     * @param value
     */
    public void set(String key, Object value) {
        yamlFile.set(key, value);
        try {
            yamlFile.save(configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
