package ge.lilchacha.service.impl;

import ge.lilchacha.service.ProducerService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendSticker;

import static ge.lilchacha.model.RabbitQueue.ANSWER_MESSAGE;
import static ge.lilchacha.model.RabbitQueue.ANSWER_STICKER_MESSAGE;

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


}
