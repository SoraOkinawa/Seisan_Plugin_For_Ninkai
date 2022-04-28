package me.Seisan.plugin.Features.objectnum;

import lombok.Getter;

public enum Teinte {
    NEUTRE("", 0),
    CLAIR("clair", 1),
    FONCE("foncé", 2),
    INDEFINI("Non défini", -1);

    @Getter
    private String name;
    @Getter
    private int id;

    Teinte(String name, int id){
        this.name = name;
        this.id = id;
    }

    public static Teinte getFromID(int id){
        for(Teinte teinte : values()){
            if(teinte.getId() == id){
                return teinte;
            }
        }
        return INDEFINI;
    }

    public static Teinte fromName(String str){
        for(Teinte teinte : values()){
            if(teinte.getName().equals(str)){
                return teinte;
            }
        }
        return null;
    }
}