package me.Seisan.plugin.Features.Inventory;

import de.themoep.inventorygui.GuiElementGroup;
import de.themoep.inventorygui.GuiPageElement;
import de.themoep.inventorygui.InventoryGui;
import de.themoep.inventorygui.StaticGuiElement;
import me.Seisan.plugin.Features.skill.Skill;
import me.Seisan.plugin.Main;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class BbInventory {
	public static void openBb(List<Skill> learnableSkillList, Player player) {
		String title = "§7Bibliothèque de Techniques";
		String[] setup = {
				"jjjjjjjjj",
				"jjjjjjjjj",
				"jjjjjjjjj",
				"f  pcn   l"
		};
		
		InventoryGui gui = new InventoryGui(Main.plugin(), player, title, setup);
		gui.setFiller(new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1));
		
		GuiElementGroup learnableSkillsGroup = new GuiElementGroup('j');
		Collections.sort(learnableSkillList, new Comparator<Skill>() {
			@Override
			public int compare(Skill o1, Skill o2) {
				return o1.getElement().compareTo(o2.getElement());
			}
		});
		
		gui.addElement(new GuiPageElement('f', new ItemStack(Material.WARPED_HANGING_SIGN), GuiPageElement.PageAction.FIRST, "Première page"));
		gui.addElement(new GuiPageElement('p', new ItemStack(Material.ARROW), GuiPageElement.PageAction.PREVIOUS, "Page précédente (%prevpage%"));
		gui.addElement(new StaticGuiElement('c', new ItemStack(Material.OAK_BUTTON), "Page %page%"));
		gui.addElement(new GuiPageElement('n', new ItemStack(Material.ARROW), GuiPageElement.PageAction.PREVIOUS, "Page suivante (%nextpage%"));
		gui.addElement(new GuiPageElement('l', new ItemStack(Material.WARPED_HANGING_SIGN), GuiPageElement.PageAction.FIRST, "Dernière page"));
		
		for (int i = 0; i < learnableSkillList.size(); i++) {
			learnableSkillsGroup.addElement(new StaticGuiElement(
					'j',
					learnableSkillList.get(i).getItem(),
					click -> {
						player.sendMessage(click.getElement().getItem(player, click.getSlot()).getItemMeta().displayName());
						return true;
					}
			));
		}
		
		gui.show(player);
	}
}
