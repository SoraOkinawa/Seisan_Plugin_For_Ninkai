package me.Seisan.plugin.Features.Sheets;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.ValueRange;

import java.io.*;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import me.Seisan.plugin.Main;
import org.bukkit.command.CommandSender;

public class SheetsReader {
	private static final String APPLICATION_NAME = "Google Sheets API Java Quickstart";
	private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
	private static final String TOKENS_DIRECTORY_PATH = "tokens";
	
	/**
	 * Global instance of the scopes required by this quickstart.
	 * If modifying these scopes, delete your previously saved tokens/ folder.
	 */
	private static final List<String> SCOPES = Collections.singletonList(SheetsScopes.SPREADSHEETS_READONLY);
	private static final String CREDENTIALS_FILE_PATH = "credentials.json";
	/**
	 * Creates an authorized Credential object.
	 *
	 * @param HTTP_TRANSPORT The network HTTP Transport.
	 * @return An authorized Credential object.
	 * @throws IOException If the credentials.json file cannot be found.
	 */
	private static GoogleCredentials getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
		// Load client secrets.
		File credentialsFile = new File(Main.plugin().getDataFolder().getCanonicalPath(), CREDENTIALS_FILE_PATH);
		InputStream in = new FileInputStream(credentialsFile);
		if (in == null) {
			throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
		}
		GoogleCredentials credential = GoogleCredentials.fromStream(in).createScoped(SCOPES);
		return credential;
	}
	
	private static Sheets getSheetsService() throws IOException, GeneralSecurityException {
		// Build a new authorized API client service.
		final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
		return new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, new HttpCredentialsAdapter(getCredentials(HTTP_TRANSPORT)))
				.setApplicationName(APPLICATION_NAME)
				.build();
	}
	
	public static List<List<Object>> ReadSheet(CommandSender sender, String spreadsheetId, String range) throws IOException, GeneralSecurityException {
		Sheets service = getSheetsService();
		
		ValueRange response = service.spreadsheets().values()
				.get(spreadsheetId, range)
				.execute();
		
		List<List<Object>> values = response.getValues();
		
		if (values == null || values.isEmpty()) {
			System.out.println("No data found.");
			return null;
		} else {
			return values;
		}
	}
}
