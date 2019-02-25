package bootybot.games;

import bootybot.util.Util;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.ValueRange;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SheetsAPI {

    private static final String APPLICATION_NAME = "IPA Games Sheet";
    private FileDataStoreFactory DATA_STORE_FACTORY;
    private static final File DATA_STORE_DIR = new File(Util.botPath, "sheets");
    private final JsonFactory JSON_FACTORY =
            JacksonFactory.getDefaultInstance();
    private HttpTransport HTTP_TRANSPORT;

    private Sheets sheetsService;
    private List<List<Object>> rawData;

    /**
     * Global instance of the scopes required by this quickstart.
     * <p>
     * If modifying these scopes, delete your previously saved credentials
     * at ~/.credentials/sheets.googleapis.com-java-quickstart
     */
    private static final List<String> SCOPES =
            Arrays.asList(SheetsScopes.SPREADSHEETS_READONLY);

    public SheetsAPI() {

        try {
            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            DATA_STORE_FACTORY = new FileDataStoreFactory(DATA_STORE_DIR);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Sheets getSheetsService() throws IOException {
        if (this.sheetsService == null) {
            Credential credential = authorize();
            this.sheetsService = new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
                    .setApplicationName(APPLICATION_NAME)
                    .build();
        }
        return this.sheetsService;
    }

    /**
     * Creates an authorized Credential object.
     *
     * @return an authorized Credential object.
     * @throws IOException
     */
    private Credential authorize() throws IOException {
        InputStream in = new FileInputStream(new File(DATA_STORE_DIR, "client_secret.json"));
        GoogleClientSecrets clientSecrets =
                GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow =
                new GoogleAuthorizationCodeFlow.Builder(
                        HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                        .setDataStoreFactory(DATA_STORE_FACTORY)
                        .setAccessType("offline")
                        .build();
        Credential credential = new AuthorizationCodeInstalledApp(
                flow, new LocalServerReceiver()).authorize("user");
        System.out.println(
                "Credentials saved to " + DATA_STORE_DIR.getAbsolutePath());
        return credential;
    }

    public List<List<Object>> getRawData() {
        if (this.rawData == null) updateData();
        return rawData;
    }

    public List<List<Object>> updateData() {
        try {
            // Build a new authorized API client service.
            Sheets service = getSheetsService();

            // Prints the names and majors of students in a sample spreadsheet:
            // https://docs.google.com/spreadsheets/d/15D1pIMSU8vinguVjTwiUu1OxLt2D4VkDKOdSZEouAbk/edit
            String spreadsheetId = "15D1pIMSU8vinguVjTwiUu1OxLt2D4VkDKOdSZEouAbk";
            String range = "games";
            ValueRange response = service.spreadsheets().values()
                    .get(spreadsheetId, range)
                    .execute();
            List<List<Object>> values = response.getValues();
            this.rawData = values;
        } catch (Exception e) {
            this.rawData = new ArrayList<>();
            e.printStackTrace();
        }
        return this.rawData;
    }
}
