package me.Seisan.plugin.Features.commands.profil;

import me.Seisan.plugin.Features.Feature;

public class ProfilRegister extends Feature {
    @Override
    protected void doRegister() {
        new AbilitiesCommand().register();
        new AgeCommand().register();
        new ApparenceCommand().register();
        new AProfilCommand().register();
        new ChakraCommand().register();
        new ColorChakraCommand().register();
        new CompetenceCommand().register();
        new EntrainementCommand().register();
        new FeuilleCommand().register();
        new GenreCommand().register();
        new GiveCompetenceCommand().register();
        new LostCommand().register();
        new LostFuinCommand().register();
        new MaskProfilCommand().register();
        new ProfilCommand().register();
        new ProfilEditCommand().register();
        new ProfilMJCommand().register();
        new RankCommand().register();
        new ReducNinjutsuCommand().register();
        new ProuesseCommand().register();
        new RyojiCommand().register();
        new SwapFuinCommand().register();
    }
}
