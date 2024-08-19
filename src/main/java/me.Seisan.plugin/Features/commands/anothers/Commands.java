package me.Seisan.plugin.Features.commands.anothers;

import me.Seisan.plugin.Features.Feature;
import me.Seisan.plugin.Features.PlayerData.PlayerConfig;
import me.Seisan.plugin.Main;
import org.bukkit.Bukkit;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

public class Commands extends Feature {
    public static ArrayList<String> param = new ArrayList<>();
    public static ArrayList<String> phraseantonin = new ArrayList<>();
    public static HashMap<UUID, PermissionAttachment> perms = new HashMap<>();
    public static ArrayList<String> PlayerBuildTemp = new ArrayList<>();
    public static Random random;

    @Override
    protected void doRegister() {
        new ChatMasterCommand().register();
        new RollCommand().register();
        new RollResistanceCommand().register();
        new EncaCommand().register();
        new NickCommand().register();
        random = new Random(System.nanoTime());
        new BootsCommand().register();
        new HatCommand().register();
        new WalkCommand().register();
        new TestCommand().register();
        new BuildCommand().register();
        new LayCommand().register();
        new SitCommand().register();
        new ChangeChatDeSeisanCommand().register();
        new GmCommand().register();
        new PNJCommand().register();
        new LoreCommand().register();
        new ReloadSkinCommand().register();
        new RenameItemCommand().register();
        new ValiderItemCommand().register();
        new AntoninCommand().register();
        new TpWorldCommand().register();
        new CanishCommand().register();
        new HRPCommand().register();
        new JumpUpDownCommand().register();
        new WarpCommand().register();
        new PrefixCommand().register();

        param.add("add");
        param.add("remove");

        phraseantonin.add("Y'a des bon moments pour dire des truc et sa s'en est 1, t'es trop bg");
        phraseantonin.add("Tu veux le Yinyanton? Ok bah rdv le 30/02 mon pote");
        phraseantonin.add("T'es vraiment cool, tu peux tout faire dans la vie.");
        phraseantonin.add("T'es trop un roleplayer d'élite");
        phraseantonin.add("Merci pour tout ce que tu fais pour le serveur");
        phraseantonin.add("T'es trop beau et c'est pas un avis porter sur la rage ?");
    }


    // Commande qui permet de générer un nombre aléatoire
    static int getRandom(int lower, int upper) {
        return (random.nextInt((upper - lower) + 1) + lower);
    }

    public static void playerVanish(PlayerConfig pConfig) {
        String s;
        if(pConfig.isVanish()) {
            Main.plugin().getServer().getOnlinePlayers().forEach((p) -> {
                if (!p.isOp())
                {
                    p.hidePlayer(Main.plugin(), pConfig.getPlayer());
                }
            });
            pConfig.getPlayer().sendMessage("§cHRP : §7Flouf, vous disparaissez.");
            pConfig.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 200000, 2, false, false));
            s = "[V] " + pConfig.getPlayer().getDisplayName();
        }
        else {
            pConfig.getPlayer().sendMessage("§cHRP : §7Pouf, vous apparaissez.");
            pConfig.getPlayer().removePotionEffect(PotionEffectType.NIGHT_VISION);
            Main.plugin().getServer().getOnlinePlayers().forEach((p) -> p.showPlayer(Main.plugin(), pConfig.getPlayer()));
            s = pConfig.getPlayer().getDisplayName();
        }
        Bukkit.getScheduler().runTaskLater(Main.plugin(), () -> pConfig.getPlayer().setPlayerListName(s), 50);

    }
}
