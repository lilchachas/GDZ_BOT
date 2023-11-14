package ge.lilchacha.service.impl;

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
        var userState = appUser.getState();
        var text = update.getMessage().getText();
        String output = "INTERNAL ERROR 500";
        InlineKeyboardMarkup keyboard = null;
        var serviceCommand = fromValue(text);

        if(update.hasCallbackQuery()){
            //TODO СЮДА МЫ ПИХАЕМ КНОПКИ
        }
        if(serviceCommand.equals(CANCEL)){
            output = cancelProccess(appUser);
        }else {
            output = processServiceCommand(appUser,text);
            keyboard = processInlineAsk(appUser, text);
        }

        var chatID = update.getMessage().getChatId();


        sendAnswer(output, chatID,keyboard);
    }

    private InlineKeyboardMarkup processInlineAsk(AppUser appUser,String text) {
        if(START.toString().equals(text)){
            return inlineCommandAssemblingService.homeMenuInlineBuilder(appUser);
        }else {
            return null;
        }
    }

    private AppUser findOrSaveAppUser(Update update) {
        User telegramUser = update.getMessage().getFrom();
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


    private String processServiceCommand(AppUser appUser, String text) {
        if(START.toString().equals(text)){
            return "сначала команда /activate, потом команда /profile хелпа по коамандам -> /help";
//            return appUserService.registerUser(appUser);
        } else if (ACTIVATE.toString().equals(text)) {
            return appUserService.checkUser(appUser);
        } else if (PROFILE.toString().equals(text)) {
            return appUserService.fillUserProfile(appUser);
        }else if (PAYMENT.toString().equals((text))){
            return "Открывается вьюшка с статусами и прочими дрочами + кнопка оплаты";
        } else if (GDZ.toString().equals(text)) {
            return "Открывается менюшка по составлению запроса в бд";
        }else if(SETTINGS.toString().equals(text)){
            return "Открывается менюшка по изменению предметов/класса";
        }else if (GPT.toString().equals(text)){
            return "Открывается менюшка по составлению запроса в гпт";
        } else if (HELP.toString().equals(text)) {
            return help();
        } else {
            return "Неизвестная команда чекни /help для того чтобы узнать доступные тебе команды";
        }
    }

    private String help() {
        return "Доступные комманды:\n"
                + "❌ /cancel - отменить текущую команду\n"
                +"❔ /help - список комманд который ты видишь сейчас\n"
                +"\uD83D\uDC7D /profile - список твоих предметов\n"
                +"\uD83D\uDCB6 /payment - твоя подписка\n"
                +"\uD83D\uDCDD /gdz - команда для получения гдз\n"
                +"⚙\uFE0F /settings - изменить твои предметы\n"
                +"\uD83E\uDDBE\uD83E\uDD16 /gpt - команда для составления запроса ChatGPT \n"
                +"Временое гойский текст по команде активации -> /activate"
                ;
    }

    private String cancelProccess(AppUser appUser) {
        appUser.setView(UserView.HOME_MENU_VIEW);
        appUserDAO.save(appUser);
        return "команда отменена";
    }

    private void sendAnswer(String output, Long chatId, InlineKeyboardMarkup keyboard) {
        //все работает с этим кодом
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(output);
        sendMessage.setReplyMarkup(keyboard);
        producerService.produceAnswer(sendMessage);

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
