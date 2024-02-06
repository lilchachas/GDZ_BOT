package ge.lilchacha.service.impl;

import ge.lilchacha.service.ProducerService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendSticker;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;

import java.util.List;

import static ge.lilchacha.model.RabbitQueue.*;

@Component
public class ProducerServiceIMPL implements ProducerService {
    private final RabbitTemplate rabbitTemplate;

    public ProducerServiceIMPL(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void produceAnswer(SendMessage message) {
        rabbitTemplate.convertAndSend(ANSWER_MESSAGE, message);
    }

    @Override
    public void produceStickerAnswer(SendSticker sticker) {
        rabbitTemplate.convertAndSend(ANSWER_STICKER_MESSAGE, sticker);
    }

    @Override
    public void produceEditInlineAnswer(List<BotApiMethod<?>> spisok) {
        rabbitTemplate.convertAndSend(ANSWER_INLINE_MESSAGE, spisok);
    }


}
