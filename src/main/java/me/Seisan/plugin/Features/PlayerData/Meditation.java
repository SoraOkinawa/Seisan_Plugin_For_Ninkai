package me.Seisan.plugin.Features.PlayerData;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

public class Meditation {
    @Getter
    String nameplayer;

    @Setter
    @Getter
    String inventory;

    @Setter
    @Getter
    int xborder;
    @Setter
    @Getter
    int zborder;

    @Getter
    @Setter
    double xspawn;
    @Getter
    @Setter
    double yspawn;
    @Getter
    @Setter
    double zspawn;

    @Getter
    @Setter
    boolean hasmedit;

    @Getter
    public static ArrayList<Meditation> instanceList = new ArrayList<>();

    public Meditation(String nameplayer, String inventory, int xborder, int zborder, double xspawn, double yspawn, double zspawn, boolean hasmedit) {
        this.nameplayer = nameplayer;
        this.inventory = inventory;
        this.xborder = xborder;
        this.zborder = zborder;
        this.xspawn = xspawn;
        this.yspawn = yspawn;
        this.zspawn = zspawn;
        this.hasmedit = hasmedit;

        if(!instanceList.contains(this))
            instanceList.add(this);
    }

    public static Meditation getMeditationFromUUID(String uuid) {
        return instanceList.stream().filter(joueur -> joueur.getNameplayer().equals(uuid)).findFirst().orElse(null);
    }

}