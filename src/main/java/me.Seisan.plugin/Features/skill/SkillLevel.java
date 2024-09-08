package me.Seisan.plugin.Features.skill;

import lombok.Getter;

/**
 * Created by Helliot on 10/02/2018.
 */
public enum SkillLevel {

    F("Sans rang", "F", 35, 70, 20, 70, 80, 7),
    E("Rang E", "E", 20, 60, 30, 50, 70, 15),
    D("Rang D", "D", 30, 65, 25, 55, 75, 10),
    C("Rang C", "C", 35, 72, 20, 70, 82, 7),
    B("Rang B", "B", 40, 80, 20, 75, 90, 7),
    A("Rang A", "A", 50, 85, 15, 85, 95, 5),
    S("Rang S", "S", 70, 90, 10, 90, 98, 3),
    SS("Rang S+", "S+", 75, 95, 5, 92, 100, 2),
    NULL("ERREUR", "[?]", 101, 101, 0, 101,101,0);

    /*

    E : 20-60-30
    D : 30-65-25
    C : 35 - 72 - 20
    B : 40 - 80 - 20
    A : 50 - 85 - 15
    S : 70 - 90 - 10
    S+ : 75 - 95 - 5

     */

    @Getter
    private String name;
    @Getter
    private String charName;
    @Getter
    private int requiredRoll;
    @Getter
    private int critRoll;
    @Getter
    private int maxBonus;
    @Getter
    private int requiredRollOneHand;
    @Getter
    private int critRollOneHand;
    @Getter
    private int maxBonusOneHand;





    SkillLevel(String name, String charName, int requiredRoll, int critRoll, int maxBonus, int requiredRollOneHand, int critRollOneHand, int maxBonusOneHand){
        this.name = name;
        this.charName = charName;
        this.requiredRoll = requiredRoll;
        this.critRoll = critRoll;
        this.maxBonus = maxBonus;
        this.requiredRollOneHand = requiredRollOneHand;
        this.critRollOneHand = critRollOneHand;
        this.maxBonusOneHand = maxBonusOneHand;
    }

    public static SkillLevel getByCharName(String s){
        for(SkillLevel value : values()){
            if(value.getCharName().equals(s)){
                return value;
            }
        }

        return SkillLevel.NULL;
    }

}
