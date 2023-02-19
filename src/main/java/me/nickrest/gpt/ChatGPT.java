package me.nickrest.gpt;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

@AllArgsConstructor
@Getter @Setter
public class ChatGPT {
    private String apiKey;

    public String sendRequest(String message) {
        try {
            JSONObject json = new JSONObject();
            json.put("model", "text-davinci-003");
            json.put("prompt", message);
            json.put("temperature", 1);
            json.put("max_tokens", 500);

            URL chatGPT = new URL("https://api.openai.com/v1/completions");
            HttpURLConnection connection = (HttpURLConnection) chatGPT.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json; utf-8");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Authorization", "Bearer " + apiKey);
            connection.setDoOutput(true);

            //.send the json
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = json.toString().getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            JSONObject responded = new JSONObject(new String(connection.getInputStream().readAllBytes()));
            JSONObject choices = responded.getJSONArray("choices").getJSONObject(0);
            return choices.getString("text").trim();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String sendCodingRequest(String message) {
        try {
            JSONObject json = new JSONObject();
            json.put("model", "code-davinci-002");
            json.put("prompt", message);
            json.put("temperature", 0);
            json.put("max_tokens", 500);
            json.put("top_p", 1);
            json.put("frequency_penalty", 0.5);
            json.put("presence_penalty", 0);

            URL chatGPT = new URL("https://api.openai.com/v1/completions");
            HttpURLConnection connection = (HttpURLConnection) chatGPT.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json; utf-8");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Authorization", "Bearer " + apiKey);
            connection.setDoOutput(true);

            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = json.toString().getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            JSONObject responded = new JSONObject(new String(connection.getInputStream().readAllBytes()));
            JSONObject choices = responded.getJSONArray("choices").getJSONObject(0);
            return choices.getString("text").trim();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
