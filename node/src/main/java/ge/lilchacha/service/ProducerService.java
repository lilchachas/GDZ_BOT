package ge.lilchacha.service;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendSticker;

import java.util.List;

public interface ProducerService {
    void produceAnswer(SendMessage message);
    void produceStickerAnswer(SendSticker sticker);
    void produceEditInlineAnswer(List<BotApiMethod<?>> spisok);
}
