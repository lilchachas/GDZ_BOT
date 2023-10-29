package ge.lilchacha.service.impl;

import ge.lilchacha.service.MainService;
import ge.lilchacha.service.ProducerService;
import ge.lilchacha.service.enums.ServiceCommands;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendSticker;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import static ge.lilchacha.service.enums.ServiceCommands.*;

@Component
@Log4j
public class MainServiceIMPL implements MainService {
    private final ProducerService producerService;

    public MainServiceIMPL(ProducerService producerService) {
        this.producerService = producerService;
    }

    @Override
    public void processTextMessage(Update update) {
        var text= update.getMessage().getText();
        String output = "не заробил";
        var serviceCommand = ServiceCommands.fromValue(text);
        if(serviceCommand.equals(ServiceCommands.CANCEL)){
            output = "добавить реализацию отмены";
        }else {
            output = processServiceCommand(text);
        }

        var chatID = update.getMessage().getChatId();
        sendAnswer(output,chatID);
    }

    private String processServiceCommand(String text) {
        if(START.toString().equals(text)){
            return "Приветствую тебя в своем УЕботе по работе с ГДЗ!" + " *вторым сообщением открывается вьюшка по регистрации и заполнении профиля*"
                    + "После заполнения профиля появляется сообщение HEГР";
        } else if (PROFILE.toString().equals(text)) {
            return "Открывается вьюшка с профилем юзера";
        }else if (PAYMENT.toString().equals((text))){
            return "Открывается вьюшка с статусами и прочими дрочами + кнопка оплаты";
        } else if (GDZ.toString().equals(text)) {
            return "Открывается менюшка по составлению запроса в бд";
        }else if(SETTINGS.toString().equals(text)){
            return "Открывается менюшка по изменению предметов/класса";
        }else if (GPT.toString().equals(text)){
            return "Открывается менюшка по составлению запроса в гпт";
        } else if (HELP.toString().equals(text)) {
            return help();
        }else {
            return "Неизвестная команда чекни /help для того чтобы узнать доступные тебе команды";
        }
    }

    private String help() {
        return "Доступные комманды:\n"
                + "❌ /cancel - отменить текущую команду\n"
                +"❔ /help - список комманд который ты видишь сейчас\n"
                +"\uD83D\uDC7D /profile - список твоих предметов\n"
                +"\uD83D\uDCB6 /payment - твоя подписка\n"
                +"\uD83D\uDCDD /gdz - команда для получения гдз\n"
                +"⚙\uFE0F /settings - изменить твои предметы\n"
                +"\uD83E\uDDBE\uD83E\uDD16 /gpt - команда для составления запроса ChatGPT \n"
                ;
    }

    private String cancelProccess(Update update) {
        return "*типа отменил и вернул на базовую вьюшку*";
    }

    private void sendAnswer(String output, Long chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(output);
        producerService.produceAnswer(sendMessage);
    }
    private void sendAnswerSticker(Long chatId) {

        InputFile sticker = new InputFile("CAACAgIAAxkBAAEBfIFlLVK9AYidDuUm6D9IcxejBqMuzAACgxAAAiOC6EsWw3lKDQVdHjAE");
        SendSticker sendSticker = new SendSticker();
        sendSticker.setChatId(chatId);
        sendSticker.setSticker(sticker);

        producerService.produceStickerAnswer(sendSticker);
    }

    @Override
    public void processStickerMessage(Update update) {
        var chatId = update.getMessage().getChatId();
        sendAnswerSticker(chatId);
    }
}
