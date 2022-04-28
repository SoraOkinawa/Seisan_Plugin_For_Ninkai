package me.Seisan.plugin.Features.PlayerData;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class PlayerConfig {
    @Getter
    private Player player;

    @Getter
    String uuid;

    @Setter
    @Getter
    boolean changechat;

    @Setter
    @Getter
    boolean buildmode;

    @Getter
    @Setter
    boolean vanish;

    @Getter
    @Setter
    boolean swapfuin;

    @Getter
    @Setter
    boolean ryoji;

    @Getter
    @Setter
    boolean encamode;

    @Getter
    public static ArrayList<PlayerConfig> instanceList = new ArrayList<>();


    public PlayerConfig(Player p, boolean changechat, boolean buildmode, boolean vanish, boolean swapfuin, boolean ryoji, boolean encamode){
        this.player = p;
        this.uuid = p.getUniqueId().toString();
        this.changechat = changechat;
        this.buildmode = buildmode;
        this.vanish = vanish;
        // true : papier
        // false : encre
        this.swapfuin = swapfuin;
        this.ryoji = ryoji;
        this.encamode = encamode;

        if(!instanceList.contains(this))
            instanceList.add(this);
    }

    public PlayerConfig(String uuid, boolean changechat, boolean buildmode, boolean vanish, boolean swapfuin, boolean ryoji, boolean encamode){
        this.uuid = uuid;
        this.player = Bukkit.getServer().getPlayer(uuid);
        this.changechat = changechat;
        this.buildmode = buildmode;
        this.vanish = vanish;
        this.swapfuin = swapfuin;
        this.ryoji = ryoji;
        this.encamode = encamode;

    }

    public static PlayerConfig getPlayerConfig(Player p){
        return instanceList.stream().filter(info -> info.getUuid().equals(p.getUniqueId().toString())).findFirst().orElse(null);
    }

    public void destroy(){
        instanceList.remove(this);
    }
}
