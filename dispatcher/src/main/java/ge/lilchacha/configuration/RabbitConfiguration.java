package ge.lilchacha.configuration;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static ge.lilchacha.model.RabbitQueue.*;

@Configuration
public class RabbitConfiguration {
    @Bean
    public MessageConverter jsonMessageConverter(){
        return new Jackson2JsonMessageConverter();
    }
    @Bean
    public Queue textMessageQueue(){
        return new Queue(TEXT_MESSAGE_UPDATE);
    }
    @Bean
    public Queue stickerMessageQueue(){
        return new Queue(STICKER_MESSAGE_UPDATE);
    }
    @Bean
    public Queue answerMessageQueue(){
        return new Queue(ANSWER_MESSAGE);
    }
    @Bean
    public Queue answerStickerMessageQueue(){
        return new Queue(ANSWER_GDZ_MESSAGE);
    }
    @Bean
    public Queue answerGDZMessageQueue(){
        return new Queue(ANSWER_STICKER_MESSAGE);
    }

}
