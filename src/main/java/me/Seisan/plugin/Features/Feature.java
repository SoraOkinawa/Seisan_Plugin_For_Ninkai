package me.Seisan.plugin.Features;

import me.Seisan.plugin.Main;
import java.util.logging.Level;
import org.bukkit.event.Listener;

/**
 *
 * @author zeal
 */
public class Feature implements Listener
{
    protected final String featureName;
    protected final String path;
    public Feature()
    {
        featureName = getClass().getSimpleName();
        path = Main.initConfigFor(featureName);
    }

    public void register()
    {
        if(Main.isActivated(path))
        {
            Main.log(Level.INFO, "Enabled feature " + featureName);
            Main.plugin().getServer().getPluginManager().registerEvents(this,Main.plugin());
            doRegister();
        }
        else
        {
            Main.log(Level.INFO, "Tried to enable feature " + featureName + " but it was disabled in the config.");
        }
        Main.exitConfigFor();
    }

    protected void doRegister()
    {

    }

    public void ignore()
    {
        Main.log(Level.INFO, "Feature " + featureName + " was ignored.");
        Main.exitConfigFor();
    }

    protected void addConfig(String s, Object value)
    {
        Main.addConfig(s, value, path);
    }

    protected Object getConfig(String s)
    {
        return Main.getConfig(s, path);
    }
}