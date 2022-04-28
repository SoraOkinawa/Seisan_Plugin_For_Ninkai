package me.Seisan.plugin.Features.objectnum;

import lombok.Getter;


public enum RPRank {

    NULL("null", "Erreur", -1, 0, -5),
    STUDENT("etudiant", "Étudiant", 0,100, -5),
    GENIN("genin", "Genin", 1, 200, 0),
    CHUUNIN("chuunin", "Chuunin", 2,200, 10),
    JUUNIN("juunin", "Juunin", 3,200, 20),
    SANNIN("sannin", "Sannin", 4,200, 20);



    RPRank(String name, String displayName, int id, int chakraRank, int resistance){
        this.name = name;
        this.displayName = displayName;
        this.id = id;
        this.chakraRank = chakraRank;
        this.resistance = resistance;

    }

    @Getter
    public String name;
    @Getter
    public String displayName;
    @Getter
    public int id;
    @Getter
    public int chakraRank;
    @Getter
    public int resistance;

    public static RPRank getById(int id){
        for(RPRank rank : values()){
            if(rank.getId() == id){
                return rank;
            }
        }
        return RPRank.NULL;
    }
}