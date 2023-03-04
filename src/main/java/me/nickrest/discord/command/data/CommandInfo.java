package me.nickrest.discord.command.data;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface CommandInfo {
    String name();
    String description();
    boolean guildOnly() default true;
    boolean devOnly() default false;
}
