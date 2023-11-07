package lilchacha.service.impl;

import lilchacha.service.OwnCaptchaService;

import java.util.Random;

public class OwnCaptchaServiceIMPL implements OwnCaptchaService {

    @Override
    public String getInlineCaptchaBody() {
        int[] nums = {
                randomizeNumsForCaptcha(-1,100),
                randomizeNumsForCaptcha(-1,100),
                randomizeNumsForCaptcha(-1,100),
                randomizeNumsForCaptcha(-1,100)};

        String[][] variants = {
                {"n0Lь", "оdIn", "дV@", "тRи", "чеTyре", "ПyAть", "shеSт", "сеMь", "vосемь", "дeVYaть"},
                {"nоЛЬ", "Одиn", "dv@", "тri", "cHEтыRе", "Пyatь", "шEст", "Sем", "воseмь", "Dевят"},
                {"nоЛь", "ОдIн", "дв@", "TrI", "чETырE", "пяtь", "шeST", "sEмь", "в0Semь", "dеVyAть"},
                {"нОЛь", "ОdiN", "два", "Tri", "chеTyRе", "ПяT", "шeсть", "Sеm", "воsEm", "DevYat"},
                {"NОЛь", "оDiN", "дvа", "трi", "Chетyrе", "ПYAть", "шесt", "сем", "Vосeм", "деvyAть"},
                {"nОЛЬ", "Один", "DVа", "тrи", "чeтYrE", "пят", "ShеStь", "Sемь", "vосem", "дEVYATь"},
                {"нОЛЬ", "одiN", "dва", "tрI", "четыrE", "пYAть", "шesть", "Seмь", "воSEmь", "dEvyaTь"},
                {"NОЛЬ", "оDин", "DV@", "Tрi", "четыrE", "Пyat", "shесtь", "сeмь", "V0сем", "deVят"},
                {"NоЛЬ", "оDIn", "дv@", "три", "CHETырe", "пyat", "шeсTь", "SEмь", "восeмь", "дEвYat"},
                {"нОлЬ", "ОдиN", "Dв@", "Tри", "cHeTыre", "ПyAт", "шeст", "sемь", "восEм", "дEвyaTь"}
        };
        //выбираем рандомное число из массива nums
        int which = nums[randomizeNumsForCaptcha(-1,4)];

        //раскладываем число на два стринговских знака
        String digit1 = String.valueOf(which/10);
        String digit2 = String.valueOf(which%10);

        //собираем строку с помощью массива variants
        String urodChislo = variants[which-1][randomizeNumsForCaptcha(-1,10)] + " " + variants[which-1][randomizeNumsForCaptcha(-1,10)];

        //TODO пихаем все в боди http запроса к инлайн менюхе

        return null;
    }
    public int randomizeNumsForCaptcha(int origin, int bound) {
        Random random  = new Random();
        //возможно проебался с нижним пределом!!!!
        return random.nextInt(origin,bound);
    }


    public String textRightNumber() {
        return null;
    }


}
