package me.Seisan.plugin.Features.commands.others;

import java.util.ArrayList;
import java.util.List;

import me.Seisan.plugin.Main;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.CommandSender;
import me.Seisan.plugin.Main.Command;
import org.bukkit.entity.Player;
import org.bukkit.entity.Player.Spigot;

public class WhoIsCommand extends Command {
    @Override
    public void myOnCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] split) {
        Spigot player = ((Player)sender).spigot();
        List<TextComponent> finalText = new ArrayList<>();
        Main.plugin().getServer().getOnlinePlayers().stream().filter((p) -> (sender.isOp() || !p.isOp())).map((p) ->
        {
            BaseComponent[] base = new ComponentBuilder(p.getName()).color(net.md_5.bungee.api.ChatColor.YELLOW).create();
            HoverEvent hoverEvent = new HoverEvent(HoverEvent.Action.SHOW_TEXT, base);
            TextComponent text = new TextComponent(p.getDisplayName());
            text.setHoverEvent(hoverEvent);
            return text;
        }).peek(finalText::add).forEach((_item) ->
        {
            finalText.add(new TextComponent(", "));
        });
        if(finalText.size()>0)
        {
            finalText.remove(finalText.size()-1);
            player.sendMessage(finalText.toArray(new TextComponent[0]));
        }
    }

    @Override
    protected List<String> myOnTabComplete(CommandSender sender, org.bukkit.command.Command command, String label, String[] split)
    {
        return new ArrayList<>();
    }

    @Override
    protected boolean isOpOnly() {
        return false;
    }
}