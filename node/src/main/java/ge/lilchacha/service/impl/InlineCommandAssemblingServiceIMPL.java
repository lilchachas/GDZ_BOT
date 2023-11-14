package ge.lilchacha.service.impl;

import ge.lilchacha.dao.AppUserDAO;
import ge.lilchacha.entity.AppUser;
import ge.lilchacha.entity.enums.UserMessageStatus;
import ge.lilchacha.service.InlineCommandAssemblingService;
import ge.lilchacha.service.MainService;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.webapp.WebAppData;
import org.telegram.telegrambots.meta.api.objects.webapp.WebAppInfo;

import java.util.ArrayList;
import java.util.List;

import static ge.lilchacha.entity.enums.UserState.NON_REGISTERED_GOI_STATE;
import static ge.lilchacha.entity.enums.UserView.*;
@Service
public class InlineCommandAssemblingServiceIMPL implements InlineCommandAssemblingService {
    private final AppUserDAO appUserDAO;

    public InlineCommandAssemblingServiceIMPL(AppUserDAO appUserDAO) {

        this.appUserDAO = appUserDAO;
    }


//    public InlineKeyboardMarkup assembleInline(Update update) {
//        var message = update.getMessage();
//        var appUser = findOrSaveAppUser(update);
//
//        InlineKeyboardMarkup output = new InlineKeyboardMarkup();
//        if(appUser.getUserMessageStatus().equals(UserMessageStatus.BASIC)){
//            output = distributeByView(message,appUser);
//        }
//        return homeMenuInlineBuilder(appUser);
//    }

    private InlineKeyboardMarkup distributeByView(Message message, AppUser appUser) {
        if(appUser.getView().equals(HOME_MENU_VIEW)){
            return homeMenuInlineBuilder(appUser);
        } else if (appUser.getView().equals(REGISTER_VIEW)) {
            return registerInlineBuilder(appUser);
        }else if (appUser.getView().equals(PROFILE_VIEW_HOME)){
            return profileInlineBuilder(appUser);
        } else if (appUser.getView().equals(PAYMENT_VIEW)) {
            return paymentInlineBuilder(appUser);
        }else if(appUser.getView().equals(GPT_CALL_VIEW_INCALL)){
            return gptInlineBuilder(appUser);
        } else if (appUser.getView().equals(GPT_CALL_VIEW_OUTCALL)) {
            return gptInlineBuilder(appUser);
        } else if (appUser.getView().equals(ACTIVATION_VIEW_1)) {
            return activationInlineBuilder(appUser);
        } else if (appUser.getView().equals(ACTIVATION_VIEW_2)) {
            return activationInlineBuilder(appUser);
        } else if (appUser.getView().equals(HELP_VIEW)) {
            return helpInlineBuilder(appUser);
        }

        return null;
    }
    @Override
    public InlineKeyboardMarkup helpInlineBuilder(AppUser appUser) {
        return null;
    }
    @Override
    public InlineKeyboardMarkup activationInlineBuilder(AppUser appUser) {
        return null;
    }
    @Override
    public InlineKeyboardMarkup gptInlineBuilder(AppUser appUser) {
        return null;
    }

    @Override
    public InlineKeyboardMarkup gdzInlineBuilder(AppUser appUser) {
        return null;
    }
    @Override
    public InlineKeyboardMarkup profileInlineBuilder(AppUser appUser) {
        return null;
    }

    @Override
    public InlineKeyboardMarkup paymentInlineBuilder(AppUser appUser) {
        return null;
    }
    @Override
    public InlineKeyboardMarkup registerInlineBuilder(AppUser appUser) {
        return null;
    }
    @Override
    public InlineKeyboardMarkup homeMenuInlineBuilder(AppUser appUser) {
        SendMessage message = new SendMessage();
        message.setText("Может первая вьюшка которая наконец дошла до чата (ﾉ◕ヮ◕)ﾉ*:･ﾟ✧ ");
        message.setChatId(appUser.getTelegramUserId());

        WebAppInfo webApp = new WebAppInfo();
        webApp.setUrl("https://telegra.ph/Recept-11-14-6");

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        // Создание списка кнопок
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        List<InlineKeyboardButton> row1 = new ArrayList<>();
        List<InlineKeyboardButton> row2 = new ArrayList<>();

        InlineKeyboardButton button1 = new InlineKeyboardButton();
        button1.setText("Активация");
        button1.setCallbackData("activation_view");
        row1.add(button1);

        InlineKeyboardButton button2 = new InlineKeyboardButton();
        button2.setText("Профиль");
        button2.setCallbackData("profile_view");
        row1.add(button2);

        InlineKeyboardButton button3 = new InlineKeyboardButton();
        button3.setText("ЧТО Я УМЕЮ");
        button3.setCallbackData("help_view");
        button3.setWebApp(webApp);
        row1.add(button3);

        // Создание и пересоздание кнопок for row2
        InlineKeyboardButton button4 = new InlineKeyboardButton();
        button4.setText("ПОЛУЧИТЬ ДОМАШКУ");
        button4.setCallbackData("gdz_view");
        row2.add(button4);

        InlineKeyboardButton button5 = new InlineKeyboardButton();
        button5.setText("ОБЬЯСНЕНИЕ ОТ ЧАТГПТ");
        button5.setCallbackData("gpt_view");
        row2.add(button5);

        InlineKeyboardButton button6 = new InlineKeyboardButton();
        button6.setText("ОПЛАТА");
        button6.setCallbackData("payment_view");
        row2.add(button6);

        // Добавление рядов в клавиатуру
        keyboard.add(row1);
        keyboard.add(row2);

        inlineKeyboardMarkup.setKeyboard(keyboard);

        // Возврат клавиатуры пользователю
        return inlineKeyboardMarkup;
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
}
