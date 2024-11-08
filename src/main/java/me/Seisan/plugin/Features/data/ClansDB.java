package me.Seisan.plugin.Features.data;

import me.Seisan.plugin.Features.ability.Ability;
import me.Seisan.plugin.Features.objectnum.Clan;
import me.Seisan.plugin.Main;
import org.bukkit.Material;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

public class ClansDB {
	public static void loadAllClansFromDB() {
		Main.LOG.info("Chargement des clans depuis la base de données...");
		
		if (loadAllClans())
			Main.LOG.info("Chargement des clans réussi.");
	}
	
	private static boolean loadAllClans() {
		try {
			PreparedStatement pst = Main.dbManager.getConnection()
					.prepareStatement("SELECT * FROM Clans");
			
			pst.executeQuery();
			ResultSet result = pst.getResultSet();
			
			int i = 0;
			while (result.next()) {
				int id = result.getInt("id");
				String name = result.getString("name");
				String tag = result.getString("tag");
				String identifiant = result.getString("identifiant");
				String colorHexa = result.getString("colorHexa");
				
				new Clan(name, id, tag, identifiant, colorHexa);
				
				i++;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			Main.LOG.log(Level.SEVERE, "Chargement des clans échoué. Veuillez vérifier la console pour plus d'informations.");
			return false;
		}
		return true;
	}
}
