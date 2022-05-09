package me.Seisan.plugin.Features.routines;

import me.Seisan.plugin.Features.Feature;
import me.Seisan.plugin.Main;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Routines extends Feature {

    private final Runnable JourNuit = DayNight::SetTime;
    private final Runnable WeatherChange = DayNight::SetWeather;
    protected static long timeMC;

    public static List<String> PhrasesSUN;
    public static List<String> PhrasesPLUIE;
    public static List<String> PhrasesORAGE;

    @Override
    protected void doRegister() {
        /* Loadings taches répétitives..."); */
        inittimeMC();
        initphrases();
        Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.plugin(), JourNuit, 0L,  60 * 20L); // 1 minute
        Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.plugin(), WeatherChange, 0L, 3600 * 20L); // 1 heure (3600 * 20L)
    }

    private void inittimeMC() {
        LocalTime irlNow = java.time.LocalTime.now(ZoneId.systemDefault()); // LocalTime = 14:42:43.062
        LocalTime igNow = irlNow.minusHours(3); // On décale le "couché du soleil" de 18h à 21h
        float dayPart = ((float) igNow.toSecondOfDay()) / 86400;
        int timeInTick = Math.round(dayPart*24000);
        timeMC = (timeInTick - 6000) % 24000; /* On décale minuit à 0 tick */
    }
    private void initphrases() {
        PhrasesSUN = new ArrayList<>();
        PhrasesORAGE = new ArrayList<>();
        PhrasesPLUIE = new ArrayList<>();

        PhrasesSUN.add("Quelqu’un a déjà vu le soleil briller à ce point ?");
        PhrasesSUN.add("L’eau a l’air bonne sous ce soleil de plomb.");
        PhrasesSUN.add("Le soleil brille désormais fort, la rumeur court qu'Atagi est dans la région.");
        PhrasesSUN.add("La terre va fondre à ce rythme !");
        PhrasesSUN.add("Pensez à vous mettre à l’ombre pour aujourd’hui.");
        PhrasesSUN.add("L’œil s’est ouvert, voici sa lumière éblouissante !");
        PhrasesSUN.add("Seisan, baignée d’un berceau de lumière et de chaleur, telle une mère et son enfant.");
        PhrasesSUN.add("La vie nous offre son plus beau cadeau : le beau temps");
        PhrasesSUN.add("Au fil des temps, quelque chose ne change jamais, l’éclat du soleil.");
        PhrasesSUN.add("Laisse-toi envahir par l’envie de chauffer sur une pierre lisse.");
        PhrasesSUN.add("A l’aube d’un jour de grâce, le soleil étend sa protection a toutes choses vivantes.");

        PhrasesPLUIE.add("Du bruit sur le toit..? Oh, ce n’est que la pluie.");
        PhrasesPLUIE.add("C’est l’heure de la douche pour certains.");
        PhrasesPLUIE.add("Qui sème le vent... récolte le riz, il pleut !");
        PhrasesPLUIE.add("Telle l’encre sur le papier, la pluie déposa son armure d’humidité sur Seisan.");
        PhrasesPLUIE.add("Viendra le temps où on pourra sortir en claquettes ! Satanée pluie.");
        PhrasesPLUIE.add("La ville se trouve a présent recouverte d’un fin rideau de pluie.");
        PhrasesPLUIE.add("C’est alors que l’eau se mit à ruisseler du ciel avec finesse.");
        PhrasesPLUIE.add("Dans un torrent délicat, les fenêtres se virent couvertes de fines goutelettes.");
        PhrasesPLUIE.add("La terre sèche réclame sa résurrection !");
        PhrasesPLUIE.add("La grande marée est arrivée !");
        PhrasesPLUIE.add("Un ciel gris chargé d’histoire s’abat sur Seisan…");
        PhrasesPLUIE.add("Quand vînt la pluie, la mer se réjouit.");
        PhrasesPLUIE.add("Soyez témoins du cycle de la vie.");

      /*  PhrasesPLUIE.add("Voilà la poudreuse !");
        PhrasesPLUIE.add("Vu le climat, Atagi ne passe pas par là...");
        PhrasesPLUIE.add("Les feuilles se gèlent, la neige tombe.");
        PhrasesPLUIE.add("Les flocons arrivent !");
        PhrasesPLUIE.add("Et si on faisait de la luge ?");
        PhrasesPLUIE.add("Sortez couverts, il neige !");
        PhrasesPLUIE.add("N'oublie pas ton bonnet et tes moufles !");
        PhrasesPLUIE.add("Vous entendez les clochettes dans le ciel ?");
        PhrasesPLUIE.add("Voulez-vous construire un bonhomme de neige ? ♪");
        PhrasesPLUIE.add("Vu le temps, il faudrait mieux rester devant la cheminée avec un chocolat chaud !");
        PhrasesPLUIE.add("Krismas approche.");
        PhrasesPLUIE.add("nik zebi, il neige");*/

        PhrasesORAGE.add("Qui sème le vent... récolte la bourrasque... ou la tempête !");
        PhrasesORAGE.add("Le Ciel semble répondre aux prières de ses fidèles.");
        PhrasesORAGE.add("L'Orage gronde, le Dieu de la Foudre est visiblement de mauvaise humeur !");
        PhrasesORAGE.add("Pourvu que la maison tienne le coup. Vu ce qu’il se passe dehors !");
        PhrasesORAGE.add("Le rugissement du vent mêlé à la puissance des éclairs forment une danse chaotique.");
        PhrasesORAGE.add("Craquement de tonnerre et plaintes des Zéphyrs résonnent en ces temps sombres.");
        PhrasesORAGE.add("L’enfer s’abat par vents et cyclones !");
        PhrasesORAGE.add("Rasoir naturel, la bourrasque souffle son passage.");
        PhrasesORAGE.add("Le chant du vent caresse les feuilles d’une délicate attention.");
        PhrasesORAGE.add("Rugit la terreur des hommes ! Vînt le moment des éclairs.");
    }
}
