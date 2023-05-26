package com.managerbot.controller;

import com.managerbot.configuration.BotConfiguration;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
@Component
public class BotGeneral extends TelegramLongPollingBot {

    private final BotConfiguration botConfiguration;
    private UpdateController updateController;

    public BotGeneral(BotConfiguration botConfiguration, UpdateController updateController) {
        this.botConfiguration = botConfiguration;
        this.updateController = updateController;
    }

    // Передаем ссылку BotGeneral в UpdateController.
    // Чтобы небыло круговой зависимости и мы могли обмениваться сообщениями между BotGeneral и UpdateController.
    @PostConstruct
    public void init(){
        updateController.registerBot(this);
    }

    @Override
    public String getBotToken() {
        return botConfiguration.getToken();
    }
    @Override
    public String getBotUsername() {
        return botConfiguration.getBotName();
    }

    @Override
    public void onUpdateReceived(Update update) {
        log.info("new Update recieve");
        if(update.hasMessage() && update.getMessage().hasText()){
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();
            String userName = update.getMessage().getFrom().getFirstName();

            switch (messageText){
                case "/start":
                    startBot(chatId, userName);
                    break;
                default: log.info("Unexpected message");
            }
        }
    }

    private void startBot(long chatId, String userName) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("Hello, " + userName + "! I'm a Telegram bot.");

        sendAnswerMessage(message);
    }

    public void sendAnswerMessage(SendMessage sendMessage) {
        try {
            execute(sendMessage);
            log.info("Reply sent");
        } catch (TelegramApiException e){
            log.error(e.getMessage());
        }
    }
}
