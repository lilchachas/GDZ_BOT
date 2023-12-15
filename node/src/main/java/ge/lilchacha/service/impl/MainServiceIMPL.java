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
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendSticker;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

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
        var chatID = update.getMessage().getChatId();
        var text = update.getMessage().getText();

        //TODO интегрировать сервис по всасыванию текста
        var serviceCommand = fromValue(text);
        if(serviceCommand.equals(CANCEL)){
            message.setReplyMarkup(inlineCommandAssemblingService.homeMenuInlineBuilder(appUser));
        }else {

        }

        message.setChatId(chatID);
        sendAnswer(message);

    }


    private SendMessage processInlineAsk(AppUser appUser,Update update) {
        SendMessage sendMessage = new SendMessage();
        String data = update.getCallbackQuery().getData();
        var value = fromValueCALL(data);
        if(value.equals(MAIN_MENU)){
            sendMessage.setText("Главное меню ( ◍•㉦•◍ )");
            sendMessage.setReplyMarkup(inlineCommandAssemblingService.homeMenuInlineBuilder(appUser));
        } else if (value.equals(HELP_MENU)) {
            sendMessage.setText("Это страница помощи выбери что ты хочешь сделать");
            sendMessage.setReplyMarkup(inlineCommandAssemblingService.helpInlineBuilder(appUser));
        } else if (value.equals(ACTIVATION_MENU)) {
            sendMessage.setText("Это страница активации пользователя нажми на кнопку ниже, чтобы активировать свой профиль");
            sendMessage.setReplyMarkup(inlineCommandAssemblingService.activationInlineBuilder(appUser));
        } else if (value.equals(PROFILE_MENU)) {
            sendMessage.setText("Это твой профиль:");
            sendMessage.setReplyMarkup(inlineCommandAssemblingService.profileInlineBuilder(appUser));
        } else if (value.equals(GPT_MENU)) {
            sendMessage.setText("Это страница отправки вопроса к чатгпт");
            sendMessage.setReplyMarkup(inlineCommandAssemblingService.gptInlineBuilder(appUser));
        } else if (value.equals(GDZ_MENU)) {
            sendMessage.setText("Это страница запроса гдз");
            sendMessage.setReplyMarkup(inlineCommandAssemblingService.gdzInlineBuilder(appUser));
        }else {
            sendMessage.setText("ошибка нажмите /cancel");
        }
        return sendMessage;
    }

    private AppUser findOrSaveAppUser(Update update) {
        User telegramUser = null;
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
    private void sendInlineAnswer(SendMessage message) {
        producerService.produceInlineAnswer(message);
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




    private void saveRawData(Update update) {
        RawData rawData = RawData.builder()
                .event(update)
                .build();
        rawDataDAO.save(rawData);
    }
}
