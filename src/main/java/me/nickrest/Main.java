package me.nickrest;

import lombok.Getter;
import me.nickrest.discord.Discord;
import me.nickrest.util.ChatGPT;
import me.nickrest.util.config.Config;
import me.nickrest.util.Hastebin;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Main {

    @Getter
    public static ChatGPT chatGPT;

    @Getter
    public static Discord discord;

    @Getter
    public static Hastebin hastebin;

    @Getter
    public static Logger logger;

    @Getter
    public static Config config;

    public static void main(String[] args) throws IOException {
        // start log4j logger
        logger = Logger.getLogger(Main.class);
        BasicConfigurator.configure();
        File configJson = new File("config.json");
        if(!configJson.exists()) {
            logger.info("config.json not found!");

            createNewFile(configJson);

            JSONObject configJsonObj = new JSONObject();
            configJsonObj.put("discord-token", "TOKEN_GOES_HERE");
            configJsonObj.put("gpt-token", "TOKEN_GOES_HERE");
            configJsonObj.put("devs", new ArrayList<>());

            logger.info("config.json created! Please fill in the tokens and restart the bot.");

            FileWriter writer = new FileWriter(configJson);
            writer.write(configJsonObj.toString());
            writer.close();
            return;
        }

        StringBuilder tokensJsonString = new StringBuilder();
        try(FileReader reader = new FileReader(configJson)) {
            int c;
            while((c = reader.read()) != -1) {
                tokensJsonString.append((char) c);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        JSONObject configJsonObj = new JSONObject(tokensJsonString.toString());

        if(!configJsonObj.has("discord-token") || !configJsonObj.has("gpt-token")) {
            logger.error("Missing token(s) in config.json!");
            return;
        }

        if (configJsonObj.getString("discord-token").equals("TOKEN_GOES_HERE") || configJsonObj.getString("gpt-token").equals("TOKEN_GOES_HERE")) {
            logger.error("Please fill in the tokens in config.json!");
            return;
        }

        config = new Config();
        for(String key : configJsonObj.keySet()) {
            if(configJsonObj.get(key) instanceof JSONObject) {
                config.set(key, configJsonObj.getJSONObject(key).toMap());
                continue;
            }

            if(configJsonObj.get(key) instanceof JSONArray) {
                config.set(key, configJsonObj.getJSONArray(key).toList());
                continue;
            }

            config.set(key, configJsonObj.get(key));
        }

        discord = new Discord(config.getString("discord-token"));
        chatGPT = new ChatGPT(config.getString("gpt-token"));
        hastebin = new Hastebin("https://api.paste.gg/v1");

        // handle errors
        Thread.setDefaultUncaughtExceptionHandler((t, e) -> logger.error("Uncaught exception in thread " + t.getName(), e));
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void createNewFile(File file) throws IOException {
        file.createNewFile();
    }
}
