package ge.lilchacha.controller;

import ge.lilchacha.service.UpdateProducer;
import ge.lilchacha.utils.MessageUtils;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendSticker;
import org.telegram.telegrambots.meta.api.objects.Update;

import static ge.lilchacha.model.RabbitQueue.STICKER_MESSAGE_UPDATE;
import static ge.lilchacha.model.RabbitQueue.TEXT_MESSAGE_UPDATE;

@Component
@Log4j
public class UpdateProcessor {
    private TelegramBot telegramBot;
    private final MessageUtils messageUtils;

    private final UpdateProducer updateProducer;

    public UpdateProcessor(MessageUtils messageUtils, UpdateProducer updateProducer) {
        this.messageUtils = messageUtils;
        this.updateProducer = updateProducer;
    }

    public Object processUpdate(Update update){
        if(update == null){
            log.debug("Received update is null");
            return null;
        }
        if (update.getMessage() != null) {
            distributeMessageByType(update);
        }else {
            log.error("Received unsupported message type" + update);
        }
        return null;
    }

    public void registerBot(TelegramBot telegramBot){
        this.telegramBot = telegramBot;
    }

    private void distributeMessageByType(Update update) {
        var message = update.getMessage();

        if(message.hasText()){
            processTextMessage(update);
        } else if (message.hasSticker()) {
            processStickerMessage(update);
        }else {
            setUnsupportedMessageTypeView(update);
        }
    }

    private void setUnsupportedMessageTypeView(Update update) {
        var sendMessage = messageUtils.generateSendMessageWithText(update, "Я не поддерживаю этот тип сообщения!");
        setView(sendMessage);
    }

    public void setView(SendMessage sendMessage) {
        telegramBot.sendAnswerMessage(sendMessage);
    }

    public void setViewSticker(SendSticker sticker){
        telegramBot.sendAnswerStickerMessage(sticker);
    }


    private void processStickerMessage(Update update) {
        updateProducer.produce(STICKER_MESSAGE_UPDATE,update);
    }

    private void processTextMessage(Update update) {
        updateProducer.produce(TEXT_MESSAGE_UPDATE,update);
    }
}
