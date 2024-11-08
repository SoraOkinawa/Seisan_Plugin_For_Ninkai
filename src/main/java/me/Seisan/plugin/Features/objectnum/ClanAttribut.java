package me.Seisan.plugin.Features.objectnum;

import lombok.Getter;

public enum ClanAttribut {
    NULL("Null", 0),
    SABLE("§eSable", 1),
    OR("§6Poudre d'or", 2),
    ARGENT("§7Limaille d'argent", 3),
    FER("§8Limaille de fer", 4),

    INDEFINI("Non défini", -1);

    @Getter
    private String name;
    @Getter
    private int id;

    ClanAttribut(String name, int id){
        this.name = name;
        this.id = id;
    }

    public static ClanAttribut getFromID(int id){
        for(ClanAttribut clanAttribut : values()){
            if(clanAttribut.getId() == id){
                return clanAttribut;
            }
        }
        return INDEFINI;
    }
}
