package ge.lilchacha.service.impl;

import ge.lilchacha.entity.enums.UserMessageStatus;
import ge.lilchacha.service.AppUserService;
import ge.lilchacha.service.InlineCommandAssemblingService;
import ge.lilchacha.service.MainService;
import ge.lilchacha.service.ProducerService;
import ge.lilchacha.service.dao.RawDataDAO;
import ge.lilchacha.service.entity.RawData;
import ge.lilchacha.dao.AppUserDAO;
import ge.lilchacha.entity.AppUser;
import ge.lilchacha.entity.enums.UserView;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendSticker;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.util.List;

import static ge.lilchacha.entity.enums.UserView.HOME_MENU_VIEW;
import static ge.lilchacha.service.enums.CallBackAnswers.*;
import static ge.lilchacha.service.enums.ServiceCommands.*;
import static ge.lilchacha.entity.enums.UserState.NON_REGISTERED_GOI_STATE;
import static ge.lilchacha.entity.enums.UserView.REGISTER_VIEW;

@Component
@Log4j
public class MainServiceIMPL implements MainService {

    private final RawDataDAO rawDataDAO;
    private final ProducerService producerService;

    private final AppUserDAO appUserDAO;
    private final AppUserService appUserService;

    private final InlineCommandAssemblingService inlineCommandAssemblingService;

    public MainServiceIMPL(RawDataDAO rawDataDAO, ProducerService producerService, AppUserDAO appUserDAO, AppUserService appUserService, InlineCommandAssemblingService inlineCommandAssemblingService) {
        this.rawDataDAO = rawDataDAO;
        this.producerService = producerService;
        this.appUserDAO = appUserDAO;
        this.appUserService = appUserService;
        this.inlineCommandAssemblingService = inlineCommandAssemblingService;
    }


    @Override
    public void processTextMessage(Update update) {
        saveRawData(update);
        var appUser = findOrSaveAppUser(update);
        SendMessage message = new SendMessage();
        DeleteMessage deleteMessage = new DeleteMessage();
        var chatID = update.getMessage().getChatId();
        var text = update.getMessage().getText();

        deleteMessage.setChatId(chatID);
        deleteMessage.setMessageId(update.getMessage().getMessageId());

        //TODO интегрировать сервис по всасыванию текста
        var serviceCommand = fromValue(text);
        if(serviceCommand.equals(CANCEL)){
            message.setText("Главное меню");
            message.setReplyMarkup(inlineCommandAssemblingService.homeMenuInlineBuilder(appUser));
            List<BotApiMethod<?>> spisok = null;
            spisok.add(deleteMessage);
            spisok.add(message);

            producerService.produceEditInlineAnswer(spisok);
        }else if (serviceCommand.equals(START)){
            message.setText("Главное меню");
            message.setReplyMarkup(inlineCommandAssemblingService.homeMenuInlineBuilder(appUser));

            message.setChatId(chatID);
            sendAnswer(message);
        }

    }



    private AppUser findOrSaveAppUser(Update update) {
        User telegramUser;
        if(update.hasCallbackQuery()){
            telegramUser = update.getCallbackQuery().getFrom();
        }else {
            telegramUser = update.getMessage().getFrom();
        }
        var optional = appUserDAO.findByTelegramUserId(telegramUser.getId());
        if(optional.isEmpty()){
            AppUser transientAppUser = AppUser.builder()
                    .telegramUserId(telegramUser.getId())
                    .username(telegramUser.getUserName())
                    .firstName(telegramUser.getFirstName())
                    .lastName(telegramUser.getLastName())
                    .isActive(false)
                    .state(NON_REGISTERED_GOI_STATE)
                    .view(REGISTER_VIEW)
                    .build();
            return appUserDAO.save(transientAppUser);
        }
        return optional.get();
    }





    private void sendAnswer(SendMessage message) {
        producerService.produceAnswer(message);
    }
    private void sendAnswerSticker(Update update) {

        InputFile sticker = new InputFile("CAACAgIAAxkBAAEBfIFlLVK9AYidDuUm6D9IcxejBqMuzAACgxAAAiOC6EsWw3lKDQVdHjAE");
        SendSticker sendSticker = new SendSticker();
        sendSticker.setChatId(update.getMessage().getChatId());
        sendSticker.setSticker(sticker);

        producerService.produceStickerAnswer(sendSticker);
    }

    @Override
    public void processStickerMessage(Update update) {

        sendAnswerSticker(update);
    }

    @Override
    public void sendErrMessage(SendMessage message) {
        producerService.produceAnswer(message);
    }


    private void saveRawData(Update update) {
        RawData rawData = RawData.builder()
                .event(update)
                .build();
        rawDataDAO.save(rawData);
    }
}
