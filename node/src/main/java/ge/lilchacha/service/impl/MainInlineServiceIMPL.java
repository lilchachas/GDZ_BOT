package ge.lilchacha.service.impl;

import ge.lilchacha.dao.AppUserDAO;
import ge.lilchacha.entity.AppUser;
import ge.lilchacha.service.InlineCommandAssemblingService;
import ge.lilchacha.service.MainInlineService;
import ge.lilchacha.service.ProducerService;
import ge.lilchacha.service.dao.RawDataDAO;
import ge.lilchacha.service.entity.RawData;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import static ge.lilchacha.entity.enums.UserState.NON_REGISTERED_GOI_STATE;
import static ge.lilchacha.entity.enums.UserView.REGISTER_VIEW;
import static ge.lilchacha.service.enums.CallBackAnswers.*;

public class MainInlineServiceIMPL implements MainInlineService {
    private final AppUserDAO appUserDAO;
    private final InlineCommandAssemblingService inlineCommandAssemblingService;
    private final ProducerService producerService;
    private final RawDataDAO rawDataDAO;

    public MainInlineServiceIMPL(AppUserDAO appUserDAO, InlineCommandAssemblingService inlineCommandAssemblingService, ProducerService producerService, RawDataDAO rawDataDAO) {
        this.appUserDAO = appUserDAO;
        this.inlineCommandAssemblingService = inlineCommandAssemblingService;
        this.producerService = producerService;
        this.rawDataDAO = rawDataDAO;
    }

    @Override
    public void processCallBack(Update update) {
        saveRawData(update);
        var appUser = findOrSaveAppUser(update);
        SendMessage message = new SendMessage();

        if (update.getCallbackQuery() != null) {
            var chatID = update.getCallbackQuery().getMessage().getChatId();
            message = processInlineAsk(appUser, update);
            message.setChatId(chatID);
        }else {
            message.setText("500 mainservice processCallBack");
        }

        sendInlineAnswer(message);
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

    private void sendInlineAnswer(SendMessage message) {
        producerService.produceInlineAnswer(message);
    }

    private void saveRawData(Update update) {
        RawData rawData = RawData.builder()
                .event(update)
                .build();
        rawDataDAO.save(rawData);
    }
}
