package ge.lilchacha.service;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface ConsumerService {
    void consumeTextMessage(Update update);
    void consumeStickerMessage(Update update);
    void consumeCallBack(Update update);

}
