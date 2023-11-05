package ge.lilchacha.service.impl;

import ge.lilchacha.service.AppUserService;
import ge.lilchacha.dao.AppUserDAO;
import ge.lilchacha.entity.AppUser;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import static ge.lilchacha.entity.enums.UserState.*;
@Service
@Log4j
public class AppUserServiceIMPL implements AppUserService {
    @Value("${refresh.all.in.seconds}")
    private String refreshNum;

    private final AppUserDAO appUserDAO;

    public AppUserServiceIMPL(AppUserDAO appUserDAO) {
        this.appUserDAO = appUserDAO;
    }



    @Override
    public String checkUser(AppUser appUser) {
        if(appUser.getIsActive().equals(true)){
            return "Ваш профиль уже активен";
        }else if(appUser.getIsActive().equals(false)){
 //TODO мкросервис который будет через дспахер отправлять сообщение где будет 5 чисел рандомных и надо будет выбрать то которое написано выше прим: Dv@тцатб PiАtb -> 25
            appUser.setIsActive(true);
            appUserDAO.save(appUser);
            return "Ваш профиль успешно активирован";
        }
        return "ERROR 500 INTERNAL ERROR";
    }

    @Override
    public String fillUserProfile(AppUser appUser) {
        if(appUser.getIsActive().equals(true)){
            if (appUser.getProfileById()==null){
                //TODO микросервис заполнения профиля который напрямую общается с пользователем через диспахер и заполняет профиль юзера
                appUser.setProfileById("TIME_ZAGLUSHKA_TEST");
                appUserDAO.save(appUser);
                return "Ваш профиль предметов временно заполнен говном пока я не напишу микросервис этой хуйни";
            } else {
                return "Ваш профиль уже заполнен предметами, хотите заполнить его заново? не надо я пока не придумал как это реализовать";
            }

        } else if (appUser.getIsActive().equals(false)){
            return "сначала пройдите активашку /activate";
        }
        return "Произошла хуйня 500";
    }

}
