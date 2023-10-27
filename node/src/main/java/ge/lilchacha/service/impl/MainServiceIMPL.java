package ge.lilchacha.service.impl;

import ge.lilchacha.service.MainService;
import ge.lilchacha.service.ProducerService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendSticker;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class MainServiceIMPL implements MainService {
    private final ProducerService producerService;

    public MainServiceIMPL(ProducerService producerService) {
        this.producerService = producerService;
    }

    @Override
    public void processTextMessage(Update update) {

        var message = update.getMessage();
        var chatId = update.getMessage().getChatId();
        String output = "что то пошло не так";
        if(message.toString().equals("тест")){
            output = "Тест дошел";
        } else if (message.toString().equals("тест 1")) {
            output = "цикл в мейнсервисе робит";
        }
        sendAnswer(output, chatId);
    }

    private void sendAnswer(String output, Long chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(output);
        producerService.produceAnswer(sendMessage);
    }
    private void sendAnswerSticker(Long chatId) {

        InputFile sticker = new InputFile("CAACAgIAAxkBAAEBfIFlLVK9AYidDuUm6D9IcxejBqMuzAACgxAAAiOC6EsWw3lKDQVdHjAE");
        SendSticker sendSticker = new SendSticker();
        sendSticker.setChatId(chatId);
        sendSticker.setSticker(sticker);

        producerService.produceStickerAnswer(sendSticker);
    }

    @Override
    public void processStickerMessage(Update update) {
        var chatId = update.getMessage().getChatId();
        sendAnswerSticker(chatId);
    }
}
