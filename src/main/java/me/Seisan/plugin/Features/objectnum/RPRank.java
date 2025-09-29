package me.Seisan.plugin.Features.objectnum;

import lombok.Getter;
import me.Seisan.plugin.Main;


public enum RPRank {

    NULL("null", "Erreur", -1, 0),
    STUDENT("etudiant", "Étudiant(e)", 0, Main.CONFIG.getInt("chakra.etudiant")),
    GENIN("genin", "Genin", 1, Main.CONFIG.getInt("chakra.genin")),
    CHUUNIN("chuunin", "Chuunin", 2,Main.CONFIG.getInt("chakra.genin")),
    JUUNIN("juunin", "Juunin", 3,Main.CONFIG.getInt("chakra.genin")),
    SANNIN("sannin", "Sannin", 4,Main.CONFIG.getInt("chakra.genin")),
    CHEF("chef", "Chef(fe) de village", 5,Main.CONFIG.getInt("chakra.genin"));



    RPRank(String name, String displayName, int id, int chakraRank){
        this.name = name;
        this.displayName = displayName;
        this.id = id;
        this.chakraRank = chakraRank;

    }

    @Getter
    public String name;
    @Getter
    public String displayName;
    @Getter
    public int id;
    @Getter
    public int chakraRank;


    public static RPRank getById(int id){
        for(RPRank rank : values()){
            if(rank.getId() == id){
                return rank;
            }
        }
        return RPRank.NULL;
    }
}