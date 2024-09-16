package me.Seisan.plugin.Features.objectnum;

import lombok.Getter;

import java.util.ArrayList;

public class Clan {
    public static ArrayList<Clan> allClans = new ArrayList<>();
    
    @Getter
    private String name;
    @Getter
    private int id;
    @Getter
    private String tag;
    @Getter
    private String identifiant;
    @Getter
    private String colorHexa;

    public Clan(String name, int id, String tag, String identifiant, String colorHexa){
        this.name = name;
        this.id = id;
        this.tag = tag;
        this.identifiant = identifiant;
        this.colorHexa = colorHexa;
        
        allClans.add(this);
    }

    public static Clan getFromID(int id){
        for(Clan clan : allClans){
            if(clan.getId() == id){
                return clan;
            }
        }
        return getFromID(1);
    }

    public static int getIDFromName(String name) {
        for(Clan clan : allClans){
            if(clan.getName().equals(name)){
                return clan.getId();
            }
        }
        return -2;
    }

    public static Clan getFromIdentifiant(String identifiant) {
        for(Clan clan : allClans){
            if(clan.getIdentifiant().equals(identifiant)){
                return clan;
            }
        }
        return getFromID(1);
    }

    public static Clan getFromName(String name) {
        for(Clan clan : allClans){
            if(clan.getName().equals(name)){
                return clan;
            }
        }
        return getFromID(1);
    }
}
