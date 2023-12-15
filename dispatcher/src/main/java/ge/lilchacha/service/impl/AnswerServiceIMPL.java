package ge.lilchacha.service.impl;

import ge.lilchacha.controller.UpdateProcessor;
import ge.lilchacha.service.AnswerService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendSticker;

import static ge.lilchacha.model.RabbitQueue.*;

@Service
public class AnswerServiceIMPL implements AnswerService {
    private final UpdateProcessor updateProcessor;

    public AnswerServiceIMPL(UpdateProcessor updateProcessor) {
        this.updateProcessor = updateProcessor;
    }

    @Override
    @RabbitListener(queues = ANSWER_MESSAGE)
    public void consumeTEXT(SendMessage message) {
        updateProcessor.setView(message);
    }

    @Override
    @RabbitListener(queues = ANSWER_STICKER_MESSAGE)
    public void consumeSTICKER(SendSticker sticker) {
        updateProcessor.setViewSticker(sticker);
    }

    @Override
    @RabbitListener(queues = ANSWER_INLINE_MESSAGE)
    public void consumeInline(SendMessage message) {
        updateProcessor.setViewInline(message);
    }
}
