package ge.lilchacha.service.impl;

import ge.lilchacha.dao.AppUserDAO;
import ge.lilchacha.entity.AppUser;
import ge.lilchacha.service.InlineCommandAssemblingService;
import ge.lilchacha.service.MainInlineService;
import ge.lilchacha.service.MainService;
import ge.lilchacha.service.ProducerService;
import ge.lilchacha.service.dao.RawDataDAO;
import ge.lilchacha.service.entity.RawData;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.List;

import static ge.lilchacha.entity.enums.UserState.NON_REGISTERED_GOI_STATE;
import static ge.lilchacha.entity.enums.UserView.REGISTER_VIEW;
import static ge.lilchacha.service.enums.CallBackAnswers.*;
@Component
@Log4j
public class MainInlineServiceIMPL implements MainInlineService {
    private final AppUserDAO appUserDAO;
    private final InlineCommandAssemblingService inlineCommandAssemblingService;
    private final ProducerService producerService;
    private final RawDataDAO rawDataDAO;

    private final MainService mainService;
    public MainInlineServiceIMPL(AppUserDAO appUserDAO, InlineCommandAssemblingService inlineCommandAssemblingService, ProducerService producerService, RawDataDAO rawDataDAO, MainService mainService) {
        this.appUserDAO = appUserDAO;
        this.inlineCommandAssemblingService = inlineCommandAssemblingService;
        this.producerService = producerService;
        this.rawDataDAO = rawDataDAO;
        this.mainService = mainService;
    }

    @Override
    public void processCallBack(Update update) {
        saveRawData(update);
        var appUser = findOrSaveAppUser(update);
        var chatID = update.getCallbackQuery().getMessage().getChatId();
        DeleteMessage deleteMessage = new DeleteMessage();
        SendMessage message = new SendMessage();

        deleteMessage.setMessageId(update.getCallbackQuery().getMessage().getMessageId());

        if (update.getCallbackQuery() != null) {
            message = processInlineAsk(appUser, update);
            message.setChatId(chatID);

            deleteMessage.setChatId(chatID);

            sendInlineAnswer(deleteMessage ,message);
        }else {
            message.setText("500 mainservice processCallBack");
            message.setChatId(chatID);
            log.error("NODE: MAIN_INLINE_SERVICE - Received update doesn't have callBackQuery");
            mainService.sendErrMessage(message);
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

    private SendMessage processInlineAsk(AppUser appUser,Update update) {
        SendMessage sendMessage = new SendMessage();
        String data = update.getCallbackQuery().getData();
        if(data!=null){
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

        }else {
            sendMessage.setText("Произошла внутренняя ошибка сервера напишите /cancel и попробуйте снова");
        }
        return sendMessage;
    }

    private void sendInlineAnswer(DeleteMessage deleteMessage, SendMessage message) {
        List<BotApiMethod<?>> spisok = null;
        spisok.add(deleteMessage);
        spisok.add(message);

        producerService.produceEditInlineAnswer(spisok);
    }

    private void saveRawData(Update update) {
        RawData rawData = RawData.builder()
                .event(update)
                .build();
        rawDataDAO.save(rawData);
    }
}
