package me.Seisan.plugin.Features.routines;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import static me.Seisan.plugin.Features.routines.Routines.*;

public class DayNight {

    public static World PaysMontagnes = Bukkit.getWorld("pays_montagnes") == null ? Bukkit.getWorld("world") : Bukkit.getWorld("pays_montagnes");
    protected static void SetTime() {
        if(PaysMontagnes != null) {
            timeMC+=16.667; // On ajoute 1 minute
            if(timeMC > 24000) {
                timeMC = 0;
            }
            PaysMontagnes.setTime(Math.round(timeMC));
        }
    }


    protected static void SetWeather() {
        try {
            URL url_1 = new URL("https://api.weatherbit.io/v2.0/current?lat=49.258&lon=4.0316&key=4570784ffb06427dbc15ef20522e96c3");
            InputStreamReader reader_1 = new InputStreamReader(url_1.openStream());
            JsonObject weather = new JsonParser().parse(reader_1).getAsJsonObject().get("data").getAsJsonArray().get(0).getAsJsonObject();
            int code = weather.get("weather").getAsJsonObject().get("code").getAsInt();
            int time = -1;
            if(code < 400) {
                if(!PaysMontagnes.isThundering()) {
                    PaysMontagnes.setStorm(true); /* Pluie */
                    PaysMontagnes.setThundering(true); /* Orage */
                    time = 0;
                }
            }
            else if(code < 700) {
                if(!PaysMontagnes.hasStorm() || PaysMontagnes.isThundering()) {
                    PaysMontagnes.setThundering(false); /* Orage */
                    PaysMontagnes.setStorm(true); /* Pluie */
                    time = 1;
                }
            }
            else {
                if (PaysMontagnes.hasStorm() || PaysMontagnes.isThundering()) {
                    PaysMontagnes.setThundering(false);
                    PaysMontagnes.setStorm(false);
                    time = 2;
                }
            }
            System.out.println("Le code est : "+code);
            int phrase;
            String phrasetemps;
            if(time == 0)  {
                // Orage
                phrase = random(0, PhrasesORAGE.size()-1);
                phrasetemps = PhrasesORAGE.get(phrase);
            }
            else if(time == 1) {
                // Pluie
                phrase = random(0, PhrasesPLUIE.size()-1);
                phrasetemps = PhrasesPLUIE.get(phrase);
            }
            else {
                // Beau temps
                phrase = random(0, PhrasesSUN.size()-1);
                phrasetemps = PhrasesSUN.get(phrase);
            }
            if(time != -1) {
                Bukkit.getServer().broadcastMessage(ChatColor.AQUA + "** " + phrasetemps + " **");
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    protected static void SetWeatherOld() {
//        https://api.weatherbit.io/v2.0/current?lat=35.6894&lon=139.692&key=4570784ffb06427dbc15ef20522e96c3

        int temps = random(0,13); /* Nombre de 0 à 13 (pour que le 13 = orage) */
        int time = -1;

        if(temps == 13) {
            if(!PaysMontagnes.isThundering()) {
                PaysMontagnes.setStorm(true); /* Pluie */
                PaysMontagnes.setThundering(true); /* Orage */
                time = 0;
            }
        }
        else if(temps > 10) {
            if(!PaysMontagnes.hasStorm() || PaysMontagnes.isThundering()) {
                PaysMontagnes.setThundering(false); /* Orage */
                PaysMontagnes.setStorm(true); /* Pluie */
                time = 1;
            }
        }
        else {
            if(PaysMontagnes.hasStorm() || PaysMontagnes.isThundering()) {
                PaysMontagnes.setThundering(false);
                PaysMontagnes.setStorm(false);
                time = 2;
            }
        }

        int phrase;
        String phrasetemps;
        if(time == 0)  {
            // Orage
            phrase = random(0, PhrasesORAGE.size()-1);
            phrasetemps = PhrasesORAGE.get(phrase);
        }
        else if(time == 1) {
            // Pluie
            phrase = random(0, PhrasesPLUIE.size()-1);
            phrasetemps = PhrasesPLUIE.get(phrase);
        }
        else {
            // Beau temps
            phrase = random(0, PhrasesSUN.size()-1);
            phrasetemps = PhrasesSUN.get(phrase);
        }
        if(time != -1) {
            Bukkit.getServer().broadcastMessage(ChatColor.AQUA + "** " + phrasetemps + " **");
        }
    }

    public static int random(int min, int max) {
        return min + (int)(Math.random() * ((max - min) + 1));
    }
}
