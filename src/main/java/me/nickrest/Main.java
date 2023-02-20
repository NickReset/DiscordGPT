package me.nickrest;

import lombok.Getter;
import me.nickrest.discord.Discord;
import me.nickrest.gpt.ChatGPT;
import me.nickrest.hastebin.Hastebin;
import org.json.JSONObject;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Main {

    @Getter
    public static ChatGPT chatGPT;
    @Getter
    public static Discord discord;
    @Getter
    public static Hastebin hastebin;

    public static void main(String[] args) {
        File tokensJson = new File("tokens.json");
        if(!tokensJson.exists()) {
            System.out.println("tokens.json not found!");
            // create the file tokens.json
            try {
                tokensJson.createNewFile();
                JSONObject tokensJsonObj = new JSONObject();
                tokensJsonObj.put("discord", "TOKENGOESHERE");
                tokensJsonObj.put("gpt", "TOKENGOESHERE");
                System.out.println("tokens.json created! Please fill in the tokens and restart the bot.");
                // create a file writer to write the tokens to the file
                try (FileWriter writer = new FileWriter(tokensJson)) {
                    writer.write(tokensJsonObj.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }

        StringBuilder tokensJsonString = new StringBuilder();
        try(FileReader reader = new FileReader(tokensJson)) {
            int c;
            while((c = reader.read()) != -1) {
                tokensJsonString.append((char) c);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        JSONObject tokensJsonObj = new JSONObject(tokensJsonString.toString());

        if(!tokensJsonObj.has("discord") || !tokensJsonObj.has("gpt")) {
            System.err.println("Missing token(s) in tokens.json!");
            return;
        }

        if (tokensJsonObj.getString("discord").equals("TOKENGOESHERE") ||
                tokensJsonObj.getString("gpt").equals("TOKENGOESHERE")) {
            System.err.println("Please fill in the tokens in tokens.json!");
            return;
        }

        discord = new Discord(tokensJsonObj.getString("discord"));
        chatGPT = new ChatGPT(tokensJsonObj.getString("gpt"));
        hastebin = new Hastebin("https://api.paste.gg/v1");
    }
}
