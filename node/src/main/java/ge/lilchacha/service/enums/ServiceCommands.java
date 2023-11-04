package ge.lilchacha.service.enums;

public enum ServiceCommands {
    START("/start"),
    CANCEL("/cancel"),
    HELP("/help"),
    PROFILE("/profile"),
    PAYMENT("/payment"),
    SETTINGS("/settings"),
    GDZ("/gdz"),
    ACTIVATE("/activate"),
    GPT("/gpt");

    private final String value;

    ServiceCommands(String value) {
        this.value = value;
    }

    public static ServiceCommands fromValue(String v){
        for (ServiceCommands c: ServiceCommands.values()){
            if(c.value.equals(v)){
                return c;
            }
        }
        return null;
    }
    @Override
    public String toString(){return value;}


}
