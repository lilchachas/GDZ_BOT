package ge.lilchacha.service;

import ge.lilchacha.entity.AppUser;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

public interface InlineCommandAssemblingService {
    InlineKeyboardMarkup homeMenuInlineBuilder(AppUser appUser);
    InlineKeyboardMarkup registerInlineBuilder(AppUser appUser);
    InlineKeyboardMarkup profileInlineBuilder(AppUser appUser);
    InlineKeyboardMarkup paymentInlineBuilder(AppUser appUser);
    InlineKeyboardMarkup gptInlineBuilder(AppUser appUser);
    InlineKeyboardMarkup gdzInlineBuilder(AppUser appUser);
    InlineKeyboardMarkup helpInlineBuilder(AppUser appUser);
    InlineKeyboardMarkup activationInlineBuilder(AppUser appUser);
}
