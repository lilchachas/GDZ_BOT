package ge.lilchacha.service;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface MainInlineService {
    void processCallBack(Update update);
}
