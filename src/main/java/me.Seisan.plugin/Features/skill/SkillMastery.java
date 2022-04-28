package me.Seisan.plugin.Features.skill;

import lombok.Getter;

/**
 * Created by Helliot on 11/02/2018.
 */
public enum SkillMastery {

    UNLEARNED("Non maîtrisée", 0),
    LEARNED("Maîtrisée", 1),
    ONEHAND("Maîtrisée à une main", 2);

    @Getter
    private String name;
    @Getter
    private int id;
    SkillMastery(String name, int id){
        this.name = name;
        this.id = id;
    }

    public static SkillMastery getById(int id){
        for(SkillMastery mastery : SkillMastery.values()){
            if(mastery.getId() == id)
                return mastery;
        }
        return null;
    }
}
