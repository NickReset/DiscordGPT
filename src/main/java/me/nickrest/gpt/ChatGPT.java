package me.nickrest.gpt;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import me.nickrest.Main;
import me.nickrest.discord.command.commands.GPTCommand;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.net.*;
import java.nio.charset.StandardCharsets;

@AllArgsConstructor
@Getter @Setter
public class ChatGPT {
    private String apiKey;

    public String sendRequest(String message, String id, String system) {
        try {
            JSONObject body = new JSONObject();
            body.put("model", "gpt-3.5-turbo");
            body.put("temperature", 1);
            body.put("max_tokens", 500);
            GPTCommand gptCommand = (GPTCommand) Main.getDiscord().getCommandManager().getCommand("gpt");
            JSONArray messages = new JSONArray();
            JSONObject systemMessage = new JSONObject();
            systemMessage.put("role", "system");
            systemMessage.put("content", system);
            messages.put(systemMessage);
            if (gptCommand.getMemory().get(id) != null) {
                for (String s : gptCommand.getMemory().get(id)) {
                    String[] splitStrings = s.split("\n", 2);
                    splitStrings[0] = splitStrings[0].replace("Q: ", "");
                    splitStrings[1] = splitStrings[1].replace("A: ", "");
                    JSONObject userMessage = new JSONObject();
                    userMessage.put("role", "user");
                    userMessage.put("content", splitStrings[0]);
                    JSONObject botMessage = new JSONObject();
                    botMessage.put("role", "assistant");
                    botMessage.put("content", splitStrings[1]);
                    messages.put(userMessage);
                    messages.put(botMessage);
                }
            }
            JSONObject messageObj = new JSONObject();
            messageObj.put("role", "user");
            messageObj.put("content", message);
            messages.put(messageObj);
            body.put("messages", messages);
            System.out.println(body);
            URL chatGPT = new URL("https://api.openai.com/v1/chat/completions");
            HttpURLConnection connection = (HttpURLConnection) chatGPT.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json; utf-8");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Authorization", "Bearer " + apiKey);
            connection.setDoOutput(true);

            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = body.toString().getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            System.out.println(connection.getResponseCode());
            System.out.println(connection.getResponseMessage());
            // get the response
            JSONObject response = new JSONObject(new String(connection.getInputStream().readAllBytes(), StandardCharsets.UTF_8));
            // choices.get(0).message.content
            JSONObject choices = response.getJSONArray("choices").getJSONObject(0);
            return choices.getJSONObject("message").getString("content");
        } catch (Exception e) {
            Main.getLogger().warn("Failed to send request to OpenAI API");
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
            Main.getLogger().warn("Failed to send coding request to OpenAI API");
            e.printStackTrace();
        }
        return null;
    }

    public long ping() {
        try(Socket socket = new Socket()) {
            long start = System.currentTimeMillis();
            socket.connect(new InetSocketAddress("api.openai.com", 443), 1000);
            return System.currentTimeMillis() - start;
        } catch (Exception e) {
            Main.getLogger().warn("Failed to ping OpenAI API");
            e.printStackTrace();
            return -1;
        }
    }

}
