package me.Seisan.plugin.Features.commands.anothers;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.io.NumberInput;
import me.Seisan.plugin.Main.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.annotation.processing.Completion;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class ConversionCommand extends Command {
    @Override
    public void myOnCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] split) {
        if (sender instanceof Player) {
            Player p = (Player) sender;

            if (split.length == 0) {
                p.sendMessage("§cUsage : \n-/conversion euro [nombre] : Convertir un montant en euros en ryos.\n -/conversion ryo [nombre] : Convertir un montant en ryos en euros.");

            }

            if (split[0].equals("help")) {
                p.sendMessage("§cUsage : \n-/conversion euro [nombre] : Convertir un montant en euros en ryos.\n -/conversion ryo [nombre] : Convertir un montant en ryos en euros.");

            }

            if (split.equals(null)) {
                p.sendMessage("§cUsage : \n-/conversion euro [nombre] : Convertir un montant en euros en ryos.\n -/conversion ryo [nombre] : Convertir un montant en ryos en euros.");

            }
            if (split.equals("euro") && split.equals(null)) {
                p.sendMessage("§cUsage : \n-/conversion euro [nombre] : Convertir un montant en euros en ryos.\n -/conversion ryo [nombre] : Convertir un montant en ryos en euros.");

            }
            if (split.equals("ryo") && split.equals(null)) {
                p.sendMessage("§cUsage : \n-/conversion euro [nombre] : Convertir un montant en euros en ryos.\n -/conversion ryo [nombre] : Convertir un montant en ryos en euros.");
            }

            if (split[0].equals("euro")) {
                double Nb = 0.01;
                double NbInput = Double.parseDouble(split[1]);
                double res = (NbInput * Nb);
                p.sendMessage("§4HRP: §2Votre conversion en euro donne : §a" + NbInput + " ryo = " + res + " euro");

            }
            if (split[0].equals("ryo")) {
                double Nb = 0.01;
                double NbInput = Double.parseDouble(split[1]);
                double res1 = (NbInput / Nb);
                p.sendMessage("§4HRP: §2Votre conversion en ryo donne : §a" + NbInput + " euro = " + res1 + " ryo");
            }

        }
    }

    @Override
    protected List<String> myOnTabComplete(CommandSender sender, org.bukkit.command.Command command, String label, String[] split) {
        List<String> completion = new ArrayList();
        completion.add("euro");
        completion.add("ryo");
        completion.add("help");
        return completion;
    }

    @Override
    protected boolean isOpOnly() {
        return false;
    }

}
