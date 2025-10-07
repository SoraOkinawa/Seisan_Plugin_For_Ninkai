package me.Seisan.plugin.Features.Inventory;

import de.themoep.inventorygui.GuiElementGroup;
import de.themoep.inventorygui.GuiPageElement;
import de.themoep.inventorygui.InventoryGui;
import de.themoep.inventorygui.StaticGuiElement;
import me.Seisan.plugin.Features.PlayerData.PlayerInfo;
import me.Seisan.plugin.Features.commands.others.ParcheminCommand;
import me.Seisan.plugin.Features.data.BbController;
import me.Seisan.plugin.Features.skill.Skill;
import me.Seisan.plugin.Main;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentBuilder;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class BbInventory {
	public static void openBbUi(List<Skill> learnableSkillList, Player player) {
		String title = "§7Bibliothèque de Techniques";
		String[] setup = {
				"jjjjjjjjj",
				"jjjjjjjjj",
				"jjjjjjjjj",
				"f  pcn  l"
		};
		
		InventoryGui gui = new InventoryGui(Main.plugin(), player, title, setup);
		gui.setFiller(new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1));
		
		Collections.sort(learnableSkillList, new Comparator<Skill>() {
			@Override
			public int compare(Skill o1, Skill o2) {
				return o1.getNameInPlugin().compareTo(o2.getNameInPlugin());
			}
		});
		
		gui.addElement(new GuiPageElement('f', new ItemStack(Material.WARPED_HANGING_SIGN), GuiPageElement.PageAction.FIRST, "§r§7Première page"));
		gui.addElement(new GuiPageElement('p', new ItemStack(Material.ARROW), GuiPageElement.PageAction.PREVIOUS, "§r§7Page précédente (§3%prevpage%§r§7)"));
		gui.addElement(new StaticGuiElement('c', new ItemStack(Material.OAK_BUTTON), "§r§7Page %page%", "§r§7Points : §6§l" + PlayerInfo.getPlayerInfo(player).getJutsuPoints()));
		gui.addElement(new GuiPageElement('n', new ItemStack(Material.ARROW), GuiPageElement.PageAction.NEXT, "§r§7Page suivante (§3%nextpage%§r§7)"));
		gui.addElement(new GuiPageElement('l', new ItemStack(Material.WARPED_HANGING_SIGN), GuiPageElement.PageAction.LAST, "§r§7Dernière page"));
		
		GuiElementGroup learnableSkillsGroup = new GuiElementGroup('j');
		learnableSkillsGroup.setFiller(new ItemStack(Material.AIR, 1));
		for (int i = 0; i < learnableSkillList.size(); i++) {
			Skill tmpSkill = learnableSkillList.get(i);

			ItemStack item = tmpSkill.getItem().clone();
			item.lore(addRankAndCostToItemLore(tmpSkill));
			
			learnableSkillsGroup.addElement(new StaticGuiElement(
					'e',
					item,
					click -> {
						openLearnUi(tmpSkill, player, gui);
						return true;
					}
			));
		}
		gui.addElement(learnableSkillsGroup);
		
		gui.show(player);
	}
	
	public static void openLearnUi(Skill skill, Player player, InventoryGui bb) {
		PlayerInfo pInfo = PlayerInfo.getPlayerInfo(player);
		String title = "Apprendre " + skill.getName();
		String[] setup = {
				"y d n"
		};
		
		InventoryGui gui = new InventoryGui(Main.plugin(), player, title, setup);
		gui.setFiller(new ItemStack(Material.AIR, 1));
		
		gui.addElement(new StaticGuiElement(
				'y',
				new ItemStack(Material.GREEN_WOOL, 1),
				click -> {
					int price = skill.getJutsuPointsPrice();
					
					if (pInfo.getJutsuPoints() >= price) {
						ParcheminCommand.GiveParchemin(skill, player);
						pInfo.setJutsuPoints(pInfo.getJutsuPoints() - price);
					}
					gui.close();
					return true;
				},
				"§r§aApprendre la Technique"
		));
		
		ItemStack item = skill.getItem().clone();
		item.lore(addRankAndCostToItemLore(skill));
		
		gui.addElement(new StaticGuiElement(
				'd',
				item,
				click -> {
					AbilityInventory.openInBook(player, skill.getInfosup().replace("%displayname%", player.getDisplayName()).split(";"));
					return true;
				}
		));
		
		gui.addElement(new StaticGuiElement(
				'n',
				new ItemStack(Material.RED_WOOL, 1),
				click -> {
					gui.close();
					bb.show(player);
					return true;
				},
				"§r§4Revenir en arrière"
		));
		
		gui.show(player);
	}
	
	private static List<Component> addRankAndCostToItemLore(Skill skill) {
		List<Component> lore = skill.getItem().lore();
		
		lore.add(Component.text("[" + skill.getLevel().getName() + " - Coût : " + skill.getJutsuPointsPrice() + "]")
				.color(NamedTextColor.DARK_GRAY)
				.decoration(TextDecoration.ITALIC, false)
		);
		
		return lore;
	}
}
