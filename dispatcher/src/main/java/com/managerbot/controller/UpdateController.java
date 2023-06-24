package com.managerbot.controller;
import com.managerbot.service.UpdateProducer;
import com.managerbot.utils.MessageUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import static com.managerbot.model.RabbitQueue.*;

@Slf4j
@Component
public class UpdateController {
    /**
     * Распределяет сообщения от telegram
     */
    private BotGeneral botGeneral;
    private final MessageUtils messageUtils;
    private final UpdateProducer updateProducer;

    public UpdateController(MessageUtils messageUtils, UpdateProducer updateProducer) {
        this.messageUtils = messageUtils;
        this.updateProducer = updateProducer;
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
            log.error("Unsupported message type is received: " + update);
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
        updateProducer.produce(PHOTO_MESSAGE_UPDATE, update);
        setFileIsReceivedView(update);
    }

    private void processDocMessage(Update update) {
        updateProducer.produce(DOC_MESSAGE_UPDATE, update);
        setFileIsReceivedView(update);
    }

    private void processTextMessage(Update update) {
        updateProducer.produce(TEXT_MESSAGE_UPDATE, update);
    }

    private void setUnsupportedMessageTypeView(Update update) {
        var sendMessage = messageUtils.generateSendMessageWithText(update, "Неподдерживаемый тип сообщения!");
        setView(sendMessage);
    }

    private void setFileIsReceivedView(Update update) {
        var sendMessage = messageUtils.generateSendMessageWithText(update, "Файл получен! Обрабатывается...");
        setView(sendMessage);
    }

    private void setView(SendMessage sendMessage) {
        botGeneral.sendAnswerMessage(sendMessage);
    }
}
