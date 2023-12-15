package ge.lilchacha.service.impl;

import ge.lilchacha.service.UpdateProducer;
import lombok.extern.log4j.Log4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@Log4j
public class UpdateProducerIMPL implements UpdateProducer {
    private final RabbitTemplate rabbitTemplate;

    public UpdateProducerIMPL(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void produce(String rabbitQueue, Update update) {
//        if(update.getMessage()!=null){
//            if(update.getMessage().hasSticker()){
//                log.debug("UpdateProducer: Был получен стикер из чата " + update.getMessage().getChatId());
//            }else if(update.getMessage().hasText()) {
//                log.debug("UpdateProducer: Было получено текстовое сообщение из чата " + update.getMessage().getChatId());
//            }else {
//                log.error("UpdateProducer: Был получен неизвестный тип сообщения типа message");
//            }
//        } else if (update.getCallbackQuery().getData()!=null) {
//            log.debug("UpdateProducer: Была получена CallBackData из чата " + update.getCallbackQuery().getMessage().getChatId());
//        }else {
//            log.error("UpdateProducer: Был получен неизвестный тип сообщения");
//        }


        rabbitTemplate.convertAndSend(rabbitQueue, update);
    }

}
