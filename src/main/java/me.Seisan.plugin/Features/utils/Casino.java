package me.Seisan.plugin.Features.utils;


import lombok.Getter;

import static me.Seisan.plugin.Features.routines.DayNight.random;


public enum Casino {

    ONE(0, 8123383, 1, 0.1),
    TWO(10, 15123383, 1, 0.1),
    THREE(50, 15763383, 1, 0.1),
    FOUR(100, 15923383, 20, 0.4),
    FIVE(200, 15973383, 20, 0.4),
    SIX(500, 15998383, 20, 0.4),
    SEVEN(1000, 15999983, 100, 1),
    EIGHT(2000, 15999999, 100, 1),
    NINE(5000, 16000000, 100, 1);

    @Getter
    private int ryo;

    @Getter
    private int roll;

    @Getter
    private int nbparticle;

    @Getter
    private double time; // En seconde

    Casino(int ryo, int roll, int nbparticle, double time) {
        this.ryo = ryo;
        this.roll = roll;
        this.nbparticle = nbparticle;
        this.time = time;
    }

    public static Casino getFromRoll() {
        int luck = random(1, 16000000);
        for (Casino casino : values()) {
            if (casino.roll >= luck) {
                return casino;
            }
        }
        return NINE;
    }
}
