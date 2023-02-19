package me.nickrest;

import lombok.Getter;
import me.nickrest.discord.Discord;
import me.nickrest.gpt.ChatGPT;
import org.json.JSONObject;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Main {

    @Getter
    public static ChatGPT chatGPT;
    public static Discord discord;

    public static void main(String[] args) {
        File tokensJson = new File("tokens.json");
        if(!tokensJson.exists()) {
            System.out.println("tokens.json not found!");
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
        discord = new Discord(tokensJsonObj.getString("discord"));
        chatGPT = new ChatGPT(tokensJsonObj.getString("gpt"));
    }
}
