package me.Seisan.plugin.Features.data;

import lombok.Getter;
import me.Seisan.plugin.Features.PlayerData.PlayerInfo;
import me.Seisan.plugin.Main;
import org.bukkit.Bukkit;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;


public class DBManager {

    private static Config cfg = new Config("database.yml");
    private static int dataId = -1;
    private Connection connection;
    private Main main;

    @Getter
    PlayerConfigDB playerConfigDB;
    @Getter
    PlayerDB playerDB;
    @Getter
    MeditationDB meditationDB;
    public DBManager(Main main){
        this.main = main;

        this.playerConfigDB = new PlayerConfigDB(this);
        this.playerDB = new PlayerDB(this);
        this.meditationDB = new MeditationDB(this);
    }

    public Connection getConnection() {
        if (!isConnected()) {
            disconnect();
            connect();
        }
        return this.connection;
    }

    public void connect(){
        try {
            String host = cfg.getString("host");
            int port = cfg.getInt("port");
            String user = cfg.getString("user");
            String password = cfg.getString("password");
            String database = cfg.getString("database");
            String sqlhost = "jdbc:mysql://" + host + ":" + port + "/" + database + "?verifyServerCertificate=false&useSSL=true";
            
            Bukkit.getLogger().log(Level.FINE, "Connexion à la base de données réussie.");
            this.connection = DriverManager.getConnection(sqlhost, user, password);
        } catch (SQLException e) {
            Bukkit.getLogger().log(Level.SEVERE, "Une erreur de connection à la base de données est survenue.");
            Bukkit.getServer().shutdown();
            e.printStackTrace();
        }
    /*
        for (PlayerInfo pInfo : PlayerInfo.getInstanceList().values()) {
            System.out.println("[SeisanPlugin] Sauvegarde des données de "+pInfo.getPlayer().getName());
            playerDB.updatePlayer(pInfo);
        }
    */

        if(dataId == -1) {
            dataId = Bukkit.getScheduler().scheduleSyncRepeatingTask(main, this::refreshConnection, 10 * 60 * 20L, 10L);
        }
    }

    public boolean isConnected(){
        try {
            return (this.connection != null) && (!this.connection.isClosed());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void refreshConnection(){
        System.out.println("[Seisan_Plugin] Refresh de la connexion à la base de données.");
        try {
            if (isConnected()) {
                disconnect();
            }
            connect();
        } catch (Exception e) {
            e.printStackTrace();
            Bukkit.getServer().shutdown();
        }
    }

    public void disconnect(){
        if (isConnected()) {
            try {
                this.connection.close();
                Bukkit.getScheduler().cancelTask(dataId);
                dataId = -1;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
