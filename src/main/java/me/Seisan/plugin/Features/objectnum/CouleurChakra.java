package me.Seisan.plugin.Features.objectnum;

import lombok.Getter;

public enum CouleurChakra {
    Bleu("§9Bleu", 0),
    Rouge("§4Rouge", 1),
    Vert("§2Vert", 2),
    Jaune("§eJaune", 3),
    Orange("§6Orange", 4),
    Violet("§5Violet", 5),
    Noir("§0Noir", 6),
    Blanc("§fBlanc", 7),
    INDEFINI("Non défini", -1);

    @Getter
    private String name;
    @Getter
    private int id;

    CouleurChakra(String name, int id){
        this.name = name;
        this.id = id;
    }

    public static CouleurChakra getFromID(int id){
        for(CouleurChakra couleurChakra : values()){
            if(couleurChakra.getId() == id){
                return couleurChakra;
            }
        }
        return INDEFINI;
    }

    public static CouleurChakra fromName(String str){
        for(CouleurChakra couleurChakra : values()){
            if(couleurChakra.getName().substring(2).equalsIgnoreCase(str)){
                return couleurChakra;
            }
        }
        return null;
    }
}