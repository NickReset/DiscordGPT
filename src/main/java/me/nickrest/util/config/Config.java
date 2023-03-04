package me.nickrest.util.config;

import lombok.Getter;

import java.util.HashMap;
import java.util.List;

/**
 * Config utility class.
 *
 * @author Nick
 * @since 3/4/23
 * */
@SuppressWarnings("unchecked")
public class Config {

    @Getter
    public final HashMap<String, Object> config = new HashMap<>();

    /**
     * Get an object from the config.
     *
     * @param key The key of the object.
     * */
    public Object get(String key) {
        return config.get(key);
    }

    /**
     * Set an object in the config.
     *
     * @param key The key of the object.
     * @param value The value of the object.
     * */
    public void set(String key, Object value) {
        config.put(key, value);
    }

    /**
     * Get a string from the config.
     *
     * @param key The key of the object.
     * */
    public String getString(String key) {
        return (String) get(key);
    }

    /**
     * check if a value exists in the config.
     *
     * @param key The key of the object.
     * */
    public boolean has(String key) {
        return config.containsKey(key);
    }

    /**
     * Get a list from the config.
     *
     * @param key The key of the object.
     * */
    public List<Object> getList(String key) {
        return (List<Object>) get(key);
    }
}
