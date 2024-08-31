package me.Seisan.plugin.Features.objectnum;

import lombok.Getter;


public enum RPRank {

    NULL("null", "Erreur", -1, 0),
    STUDENT("etudiant", "Étudiant(e)", 0,100),
    GENIN("genin", "Genin", 1, 200),
    CHUUNIN("chuunin", "Chuunin", 2,200),
    JUUNIN("juunin", "Juunin", 3,200),
    SANNIN("sannin", "Sannin", 4,200),
    CHEF("chef", "Chef(fe) de village", 5,200);



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