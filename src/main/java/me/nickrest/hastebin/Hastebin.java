package me.nickrest.hastebin;

import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class Hastebin {

    private final String API_URL;

    public Hastebin(String API_URL) {
        this.API_URL = API_URL;
    }

    public String sendRequest(String text, String author) {
        try {
            URL url = new URL(API_URL + "/pastes");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            JSONObject options = new JSONObject();
            // array of files
            JSONObject file = new JSONObject();
            JSONObject content = new JSONObject();
            content.put("format", "text");
            content.put("value", text);
            file.put("content", content);
            file.put("name", "text.txt");
            options.put("files", new JSONObject[]{file});
            options.put("name", author);
            options.put("description", "Created by " + author + " since the text was too long to send in chat.");

            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = options.toString().getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            if (connection.getResponseCode() != 201) {
                return null;
            }

            // get the response
            JSONObject response = new JSONObject(new String(connection.getInputStream().readAllBytes(), StandardCharsets.UTF_8));
            String id = response.getJSONObject("result").getString("id");
            return "https://paste.gg/p/anonymous/" + id;


        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
