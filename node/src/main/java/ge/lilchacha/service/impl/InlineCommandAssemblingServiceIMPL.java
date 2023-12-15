package ge.lilchacha.service.impl;

import ge.lilchacha.dao.AppUserDAO;
import ge.lilchacha.entity.AppUser;
import ge.lilchacha.service.InlineCommandAssemblingService;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.webapp.WebAppInfo;

import java.util.ArrayList;
import java.util.List;

import static ge.lilchacha.entity.enums.UserState.NON_REGISTERED_GOI_STATE;
import static ge.lilchacha.entity.enums.UserView.*;
@Service
public class InlineCommandAssemblingServiceIMPL implements InlineCommandAssemblingService {

    @Override
    public InlineKeyboardMarkup homeMenuInlineBuilder(AppUser appUser) {
        WebAppInfo webApp = new WebAppInfo("https://telegra.ph/ReCePt-OgUrCHiKoV-11-18");

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        // Создание списка кнопок
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        List<InlineKeyboardButton> row1 = new ArrayList<>();
        List<InlineKeyboardButton> row2 = new ArrayList<>();

        InlineKeyboardButton button1 = new InlineKeyboardButton();
        button1.setText("Активация");
        button1.setCallbackData("activation");
        row1.add(button1);

        InlineKeyboardButton button2 = new InlineKeyboardButton();
        button2.setText("Профиль");
        button2.setCallbackData("profile");
        row1.add(button2);

        InlineKeyboardButton button3 = new InlineKeyboardButton();
        button3.setText("ЧТО Я УМЕЮ");
        button3.setWebApp(webApp);
        row1.add(button3);

        // Создание и пересоздание кнопок for row2
        InlineKeyboardButton button4 = new InlineKeyboardButton();
        button4.setText("ПОЛУЧИТЬ ДОМАШКУ");
        button4.setCallbackData("gdz");
        row2.add(button4);

        InlineKeyboardButton button5 = new InlineKeyboardButton();
        button5.setText("ОБЬЯСНЕНИЕ ОТ ЧАТГПТ");
        button5.setCallbackData("gpt");
        row2.add(button5);

        InlineKeyboardButton button6 = new InlineKeyboardButton();
        button6.setText("ОПЛАТА");
        button6.setCallbackData("payment");
        row2.add(button6);

        // Добавление рядов в клавиатуру
        keyboard.add(row1);
        keyboard.add(row2);

        inlineKeyboardMarkup.setKeyboard(keyboard);

        // Возврат клавиатуры пользователю
        return inlineKeyboardMarkup;
    }
    @Override
    public InlineKeyboardMarkup  helpInlineBuilder(AppUser appUser) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        WebAppInfo webApp = new WebAppInfo("https://sun9-17.userapi.com/impg/WaNRVMaPhzrg06heo9hY6RoW3mqf8BryWid9aw/zAAzbpi-X74.jpg?size=2560x1707&quality=95&sign=bc98e95fbf1a642f935794172aa053ab&type=album");

        // Создание списка кнопок
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        List<InlineKeyboardButton> row1 = new ArrayList<>();
        List<InlineKeyboardButton> row2 = new ArrayList<>();

        InlineKeyboardButton button1 = new InlineKeyboardButton();
        button1.setText("F.A.Q");
        button1.setWebApp(webApp);
        row1.add(button1);

        InlineKeyboardButton button2 = new InlineKeyboardButton();
        button2.setText("Поддержка");
        button2.setWebApp(webApp);
        row1.add(button2);

        InlineKeyboardButton button3 = new InlineKeyboardButton();
        button3.setText("ОТМЕНА");
        button3.setCallbackData("main");
        row2.add(button3);

        keyboard.add(row1);
        keyboard.add(row2);

        inlineKeyboardMarkup.setKeyboard(keyboard);


        return inlineKeyboardMarkup;
    }
    @Override
    public InlineKeyboardMarkup  activationInlineBuilder(AppUser appUser) {
        SendMessage message = new SendMessage();

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        WebAppInfo webApp = new WebAppInfo("https://sun9-17.userapi.com/impg/WaNRVMaPhzrg06heo9hY6RoW3mqf8BryWid9aw/zAAzbpi-X74.jpg?size=2560x1707&quality=95&sign=bc98e95fbf1a642f935794172aa053ab&type=album");

        // Создание списка кнопок
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        List<InlineKeyboardButton> row1 = new ArrayList<>();
        List<InlineKeyboardButton> row2 = new ArrayList<>();

        InlineKeyboardButton button1 = new InlineKeyboardButton();
        button1.setText("Активируй \n меня");
        button1.setCallbackData("activate_user");
        row1.add(button1);

        InlineKeyboardButton button2 = new InlineKeyboardButton();
        button2.setText("ОТМЕНА");
        button2.setCallbackData("main");
        row2.add(button2);

        keyboard.add(row1);
        keyboard.add(row2);

        inlineKeyboardMarkup.setKeyboard(keyboard);


        return inlineKeyboardMarkup;
    }
    @Override
    public InlineKeyboardMarkup  gptInlineBuilder(AppUser appUser) {

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        WebAppInfo webApp = new WebAppInfo("https://sun9-17.userapi.com/impg/WaNRVMaPhzrg06heo9hY6RoW3mqf8BryWid9aw/zAAzbpi-X74.jpg?size=2560x1707&quality=95&sign=bc98e95fbf1a642f935794172aa053ab&type=album");

        // Создание списка кнопок
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        List<InlineKeyboardButton> row1 = new ArrayList<>();
        List<InlineKeyboardButton> row2 = new ArrayList<>();

        InlineKeyboardButton button1 = new InlineKeyboardButton();
        button1.setText("НАПИСТАТЬ GPT");
        button1.setCallbackData("gpt_request");
        row1.add(button1);

        InlineKeyboardButton button2 = new InlineKeyboardButton();
        button2.setText("ОТМЕНА");
        button2.setCallbackData("main");
        row2.add(button2);

        inlineKeyboardMarkup.setKeyboard(keyboard);
        return inlineKeyboardMarkup;
    }

    @Override
    public InlineKeyboardMarkup  gdzInlineBuilder(AppUser appUser) {

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        // Создание списка кнопок
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        List<InlineKeyboardButton> row1 = new ArrayList<>();
        List<InlineKeyboardButton> row2 = new ArrayList<>();

        InlineKeyboardButton button1 = new InlineKeyboardButton();
        button1.setText("ПРИСЛАТЬ ДОМАШКУ");
        button1.setCallbackData("gdz_request");
        row1.add(button1);

        InlineKeyboardButton button2 = new InlineKeyboardButton();
        button2.setText("ОТМЕНА");
        button2.setCallbackData("main");
        row2.add(button2);

        inlineKeyboardMarkup.setKeyboard(keyboard);

        return inlineKeyboardMarkup;
    }
    @Override
    public InlineKeyboardMarkup  profileInlineBuilder(AppUser appUser) {

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        // Создание списка кнопок
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        List<InlineKeyboardButton> row1 = new ArrayList<>();
        List<InlineKeyboardButton> row2 = new ArrayList<>();

        InlineKeyboardButton button1 = new InlineKeyboardButton();
        button1.setText("ПОКАЖИ МОЙ ПРОФИЛЬ");
        button1.setCallbackData("profile_request");
        row1.add(button1);

        InlineKeyboardButton button2 = new InlineKeyboardButton();
        button2.setText("ОТМЕНА");
        button2.setCallbackData("main");
        row2.add(button2);

        inlineKeyboardMarkup.setKeyboard(keyboard);


        return inlineKeyboardMarkup;
    }

    @Override
    public InlineKeyboardMarkup  paymentInlineBuilder(AppUser appUser) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        // Создание списка кнопок
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        List<InlineKeyboardButton> row1 = new ArrayList<>();
        List<InlineKeyboardButton> row2 = new ArrayList<>();

        InlineKeyboardButton button1 = new InlineKeyboardButton();
        button1.setText("МОЯ ПОДПИСКА");
        button1.setCallbackData("payment_profile_request");
        row1.add(button1);

        InlineKeyboardButton button2 = new InlineKeyboardButton();
        button2.setText("ЗАПЛАТИТЬ");
        button2.setCallbackData("payment_request");
        row1.add(button2);

        InlineKeyboardButton button3 = new InlineKeyboardButton();
        button3.setText("ЗАПЛАТИТЬ");
        button3.setCallbackData("payment_request");
        row2.add(button3);

        inlineKeyboardMarkup.setKeyboard(keyboard);

        return inlineKeyboardMarkup;
    }
    @Override
    public InlineKeyboardMarkup  registerInlineBuilder(AppUser appUser) {
        // Создание списка кнопок
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        List<InlineKeyboardButton> row1 = new ArrayList<>();
        List<InlineKeyboardButton> row2 = new ArrayList<>();

        InlineKeyboardButton button1 = new InlineKeyboardButton();
        button1.setText("АКТИВИРУЙ МЕНЯ");
        button1.setCallbackData("first_activation_request");
        row1.add(button1);

        InlineKeyboardButton button2 = new InlineKeyboardButton();
        button2.setText("ОТМЕНА");
        button2.setCallbackData("main");
        row2.add(button2);

        inlineKeyboardMarkup.setKeyboard(keyboard);

        return inlineKeyboardMarkup;
    }

}
