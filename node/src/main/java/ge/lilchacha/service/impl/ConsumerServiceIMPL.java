package ge.lilchacha.service.impl;

import ge.lilchacha.service.ConsumerService;
import ge.lilchacha.service.MainService;
import lombok.extern.log4j.Log4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import static ge.lilchacha.model.RabbitQueue.STICKER_MESSAGE_UPDATE;
import static ge.lilchacha.model.RabbitQueue.TEXT_MESSAGE_UPDATE;

@Component
@Log4j
public class ConsumerServiceIMPL implements ConsumerService {
    private final MainService mainService;

    public ConsumerServiceIMPL(MainService mainService) {
        this.mainService = mainService;
    }

    @Override
    @RabbitListener(queues = TEXT_MESSAGE_UPDATE)
    public void consumeTextMessage(Update update) {
        log.debug("NODE: TEXT MESSAGE UPDATE RECEIVED");
        mainService.processTextMessage(update);
    }

    @Override
    @RabbitListener(queues = STICKER_MESSAGE_UPDATE)
    public void consumeStickerMessage(Update update) {
        log.debug("NODE: STICKER MESSAGE UPDATE RECEIVED");
        mainService.processStickerMessage(update);
    }
}
