package ge.lilchacha.controller;

import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendSticker;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.annotation.PostConstruct;
@Component
@Log4j
public class TelegramBot extends TelegramWebhookBot {
    @Value("${bot.token}")
    private String botToken;
    @Value("${bot.name}")
    private String botName;
    @Value("${bot.uri}")
    private String botUri;

    private UpdateProcessor updateProcessor;

    public TelegramBot(UpdateProcessor updateProcessor) {
        this.updateProcessor = updateProcessor;
    }

    @PostConstruct
    public void init(){

        updateProcessor.registerBot(this);

        try {
            var setWebhook = SetWebhook.builder()
                    .url(botUri)
                    .build();
            this.setWebhook(setWebhook);
        }catch (TelegramApiException e){
            log.error(e);
        }
    }
    @Override
    public String getBotUsername() {
        return botName;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        return null;
    }

    @Override
    public String getBotPath() {
        return "/update";
    }

    public void sendAnswerMessage(SendMessage message){
        if(message!=null){
            try{
                if(message!=null){
                    execute(message);
                }
            }catch (TelegramApiException e){
                log.error(e);
            }
        }
    }
    public void sendAnswerStickerMessage(SendSticker sticker){
        if(sticker!=null){
            try{
                if(sticker!=null){
                    execute(sticker);
                }
            }catch (TelegramApiException e){
                log.error(e);
            }
        }
    }

}
