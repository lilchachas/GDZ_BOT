package ge.lilchacha.service;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendSticker;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;

import java.util.List;

public interface AnswerService {
    void consumeTEXT(SendMessage message);
    void consumeSTICKER(SendSticker sticker);
    void consumeInline(List<BotApiMethod<?>> spisok);
}
