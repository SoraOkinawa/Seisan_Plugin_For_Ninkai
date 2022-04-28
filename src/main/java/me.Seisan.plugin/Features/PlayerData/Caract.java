package me.Seisan.plugin.Features.PlayerData;

import lombok.Getter;
import me.Seisan.plugin.Features.ability.Ability;

import java.util.ArrayList;

public class Caract {
    @Getter
    int vitesse;
    @Getter
    int force;
    @Getter
    int instinct;
    @Getter
    int perception;

    public Caract(int force, int vitesse, int perception, int instinct) {
        this.force = force;
        this.vitesse = vitesse;
        this.perception = perception;
        this.instinct = instinct;
    }

    public Caract(ArrayList<Ability> abilities) {
        for (Ability ability : abilities) {
            switch (ability.getType()) {
                case "Force":
                    if (ability.getLvl() > force) {
                        force = ability.getLvl();
                    }
                    break;
                case "Vitesse":
                    if (ability.getLvl() > vitesse) {
                        vitesse = ability.getLvl();
                    }
                    break;
                case "Perception de la vitesse":
                    if (ability.getLvl() > perception) {
                        perception = ability.getLvl();
                    }
                    break;
                case "Instinct et expérience":
                    if (ability.getLvl() > instinct) {
                        instinct = ability.getLvl();
                    }
                    break;
            }
        }
    }
}
