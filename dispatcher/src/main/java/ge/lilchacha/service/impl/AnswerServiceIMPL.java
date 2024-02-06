package ge.lilchacha.service.impl;

import ge.lilchacha.controller.UpdateProcessor;
import ge.lilchacha.service.AnswerService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendSticker;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;

import java.util.List;

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
    public void consumeInline(List<BotApiMethod<?>> spisok) {
        DeleteMessage deleteMessage = (DeleteMessage) spisok.get(0);
        SendMessage message = (SendMessage) spisok.get(1);
        updateProcessor.setViewInline(deleteMessage ,message);
    }
}
