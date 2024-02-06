package ge.lilchacha.service;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface MainService {
    void processTextMessage(Update update);
    void processStickerMessage(Update update);

    void sendErrMessage(SendMessage message);
}
