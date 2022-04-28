package me.Seisan.plugin.Features.objectnum;

import lombok.Getter;

public enum Gender {
    HOMME("Homme", 0),
    FEMME("Femme", 1),
    AUTRE("Autre", 2),
    INDEFINI("Non défini", -1);

    @Getter
    private String name;
    @Getter
    private int id;

    Gender(String name, int id){
        this.name = name;
        this.id = id;
    }

    public static Gender getFromID(int id){
        for(Gender genre : values()){
            if(genre.getId() == id){
                return genre;
            }
        }
        return INDEFINI;
    }

    public static Gender fromName(String str){
        for(Gender gender : values()){
            if(gender.getName().equalsIgnoreCase(str)){
                return gender;
            }
        }
        return null;
    }
}