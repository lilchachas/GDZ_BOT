package ge.lilchacha.controller;

import ge.lilchacha.service.UpdateProducer;
import ge.lilchacha.utils.MessageUtils;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

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
        if (update != null) {
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
            processTextMesssage(update);
        }else if(message.hasSticker()){
            processStickerMessage(update);
        }else {
            setUnsupportedMessageTypeView(update);
        }
    }

    private void setUnsupportedMessageTypeView(Update update) {
        var sendMessage = messageUtils.generateSendMessageWithText(update, "Я не поддерживаю этот тип сообщения!");
        setView(sendMessage);
    }

    private void setView(SendMessage sendMessage) {
        //TODO 1) Надо доделать интеграцию RabbitMQ и проверить ее работу
        // 2) написать node а конкретно найти модуль который хуярит ответы и заполнить его с командами
        // 3) заставить бота ответить на сообщение без добавления статусов DATABASE и прочей залупы
        // 4) прочитать TelegramBOTAPI по теме стикеров + менюшка с командами
        telegramBot.sendAnswerMessage(sendMessage);
    }

    private void processStickerMessage(Update update) {
    }

    private void processTextMesssage(Update update) {
        updateProducer.produce(TEXT_MESSAGE_UPDATE,update);
    }
}
