package ge.lilchacha.service;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendSticker;

public interface AnswerService {
    void consumeTEXT(SendMessage message);
    void consumeSTICKER(SendSticker sticker);
    void consumeInline(SendMessage message);
}
