package com.managerbot.configuration;

import com.managerbot.controller.BotGeneral;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Slf4j
@Component
public class Initializer {
    private final BotGeneral botGeneral;

    @Autowired
    public Initializer(BotGeneral botGeneral){
        this.botGeneral = botGeneral;
    }

    @EventListener({ContextRefreshedEvent.class})
    public void init() {
        try {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(botGeneral);
            log.info("TelegramAPI started. Look for messages");
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }
}
