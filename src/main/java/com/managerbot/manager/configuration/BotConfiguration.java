package com.managerbot.manager.configuration;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration

//@Data
@PropertySource("application.properties")
public class BotConfiguration {
    @Getter
    @Value("${bot.name}") String botName;

    @Getter
    @Value("${bot.token}") String token;
}
