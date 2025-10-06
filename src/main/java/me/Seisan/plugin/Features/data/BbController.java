package me.Seisan.plugin.Features.data;

import me.Seisan.plugin.Features.PlayerData.PlayerInfo;
import me.Seisan.plugin.Features.ability.Ability;
import me.Seisan.plugin.Features.objectnum.ArtNinja;
import me.Seisan.plugin.Features.objectnum.ChakraType;
import me.Seisan.plugin.Features.skill.Skill;
import me.Seisan.plugin.Features.skill.SkillLevel;
import me.Seisan.plugin.Main;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

public class BbController {
	
	public static List<Skill> listLearnableSkills(PlayerInfo player) {
		List<Skill> learnableSkills = new ArrayList<>();
		
		Set<ChakraType> chakraTypes = player.getChakraType().keySet();
		ArtNinja playerVoie = player.getVoieNinja();
		int playerLvl = player.getLvL(playerVoie.getName());
		boolean isGenjutsu = playerVoie.getName().equals("Genjutsu");
		
		for (ChakraType ct :
				chakraTypes) {
			learnableSkills.addAll(Skill.getSkillByCategory(ct.name));
		}
		if (isGenjutsu) learnableSkills.addAll(Skill.getSkillByCategory("Genjutsu"));
		
		// On enlève les techniques privées et les techniques déjà apprises
		learnableSkills.removeIf(s -> !s.isPublique());
		learnableSkills.removeIf(skill -> player.getSkills().containsKey(skill));
		
		// On supprime tous les Ninjutsus de rangs non maîtrisés pour tous les éléments
		for (ChakraType ct :
				chakraTypes) {
			SkillLevel skillMaxLevel = getSkillMaxLevel("Ninjutsu", playerVoie, playerLvl);
			learnableSkills.removeIf(skill -> skill.getElement().equals(ct.name) && skill.getLevel().getLevelOrder() > skillMaxLevel.getLevelOrder());
		}
		
		if (isGenjutsu) {
			// On supprime tous les Genjutsus de rangs non maîtrisés
			SkillLevel skillMaxLevel = getSkillMaxLevel("Genjutsu", playerVoie, playerLvl);
			learnableSkills.removeIf(skill -> skill.getElement().equals(playerVoie.getName()) && skill.getLevel().getLevelOrder() > skillMaxLevel.getLevelOrder());
			
			// On supprime tous les Genjutsus de sens non maîtrisés
			removeSensesNotMastered(learnableSkills, player);
			if (playerLvl < Main.CONFIG.getInt("skillLevels.genjutsu.irréel"))
				learnableSkills.removeIf(skill -> skill.getName().contains(("Irréel")));
		}
		
		return learnableSkills;
	}
	
	private static SkillLevel getSkillMaxLevel(String targetedVoieNinja, ArtNinja currentVoieNinja, int playerLvl) {
		String configPath = "skillLevels." + targetedVoieNinja.toLowerCase() + ".";
		SkillLevel base = SkillLevel.getByCharName(Main.CONFIG.getString(configPath + "base"));
		
		if ((targetedVoieNinja != "Genjutsu" && targetedVoieNinja != "Ninjutsu") || (targetedVoieNinja == "Ninjutsu" && currentVoieNinja.equals(ArtNinja.getFromName("Genjutsu"))))
			return base;
		
		for (int i = 3; i > 0; i--) {
			if (playerLvl >= Main.CONFIG.getInt(configPath + "step" + i + ".rank"))
				return SkillLevel.getByCharName(Main.CONFIG.getString(configPath + "step" + i + ".level"));
		}
		
		return base;
	}
	
	private static void removeSensesNotMastered(List<Skill> learnableSkills, PlayerInfo player) {
		List<String> sensesList = new ArrayList<>();
		
		for (Ability a :
				player.getAbilities()) {
			if (a.getNameInPlugin().contains("genjutsu_sens_")) {
				String[] words = a.getName().split(" ");
				
				sensesList.add(words[words.length-1].trim());
			}
		}
		
		for (Skill s :
				learnableSkills) {
			if (s.getElement().equals("Genjutsu")) {
				String skillName = s.getName();
				if (Pattern.matches("\\[.+\\]", skillName)) {
					String[] senses = s.getName().substring(skillName.indexOf('['), skillName.indexOf(']')).split(",");
					
					for (int i = 0; i < senses.length; i++) {
						if (!sensesList.contains(senses[i].trim())) {
							learnableSkills.remove(s);
							break;
						}
					}
				}
			}
		}
	}
}
