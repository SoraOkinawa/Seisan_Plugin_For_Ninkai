package me.Seisan.plugin.Features.data;

import me.Seisan.plugin.Features.objectnum.ArtNinja;
import me.Seisan.plugin.Features.objectnum.Clan;
import me.Seisan.plugin.Main;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

public class NinjaArtsDB {
	public static void loadAllNinjaArtsFromDB() {
		Main.LOG.info("Chargement des arts ninjas depuis la base de données...");
		
		if (loadAllClans())
			Main.LOG.info("Chargement des arts ninjas réussi.");
	}
	
	private static boolean loadAllClans() {
		try {
			PreparedStatement pst = Main.dbManager.getConnection()
					.prepareStatement("SELECT * FROM NinjaArts");
			
			pst.executeQuery();
			ResultSet result = pst.getResultSet();
			
			int i = 0;
			while (result.next()) {
				int id = result.getInt("id");
				String name = result.getString("name");
				String tag = result.getString("tag");
				String identifiant = result.getString("identifiant");
				String colorHexa = result.getString("colorHexa");
				
				new ArtNinja(name, id, tag, identifiant, colorHexa);
				
				i++;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			Main.LOG.log(Level.SEVERE, "Chargement des arts ninjas échoué. Veuillez vérifier la console pour plus d'informations.");
			return false;
		}
		return true;
	}
}
