package ge.lilchacha.service.enums;

import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

public enum CallBackAnswers {
    MAIN_MENU("main"),
    HELP_MENU("help"),
    ACTIVATION_MENU("activation"),
    PROFILE_MENU("profile"),
    GPT_MENU("gpt"),
    GDZ_MENU("gdz");

    private final String value;

    CallBackAnswers(String value) {
        this.value = value;
    }

    public static CallBackAnswers fromValueCALL(String v){
        for (CallBackAnswers c: CallBackAnswers.values()){
            if(c.value.equals(v)){
                return c;
            }
        }
        return null;
    }

}
