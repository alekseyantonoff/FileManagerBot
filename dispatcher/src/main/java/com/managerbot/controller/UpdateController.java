package com.managerbot.controller;
import com.managerbot.utils.MessageUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Slf4j
@Component
public class UpdateController {
    /**
     * Распределяет сообщения от telegram
     */
    private BotGeneral botGeneral;
    private MessageUtils messageUtils;

    public UpdateController(MessageUtils messageUtils) {
        this.messageUtils = messageUtils;
    }

    public void registerBot(BotGeneral botGeneral){
        this.botGeneral = botGeneral;
    }

    public void processUpdate(Update update){
        if(update == null){
            log.error("Received update is null");
            return;
        }
        if(update.getMessage() != null){
            distributeMessageByType(update);
        } else {
            log.error("Received unsupported message type" + update);
        }

    }

    private void distributeMessageByType(Update update) {
        var message = update.getMessage();
        if(message.getText() != null){
            processTextMessage(update);
        } else if(message.getDocument() != null){
            processDocMessage(update);
        } else if(message.getPhoto() != null){
            processPhotoMessage(update);
        } else {
            setUnsupportedMessageTypeView(update);
        }

    }

    private void processPhotoMessage(Update update) {
    }

    private void processDocMessage(Update update) {
    }

    private void processTextMessage(Update update) {
    }

    private void setUnsupportedMessageTypeView(Update update) {
        var sendMessage = messageUtils.generateSendMessageWithText(update, "Неподдерживаемый тип сообщения!");
        setView(sendMessage);
    }

    private void setView(SendMessage sendMessage) {
        botGeneral.sendAnswerMessage(sendMessage);
    }
}
