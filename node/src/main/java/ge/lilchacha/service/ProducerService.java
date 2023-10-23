package ge.lilchacha.service;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendSticker;

public interface ProducerService {
    void produceAnswer(SendMessage message);
    void produceStickerAnswer(SendSticker sticker);
}
