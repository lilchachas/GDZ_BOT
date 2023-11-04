package ge.lilchacha.service;

import ge.lilchacha.entity.AppUser;

public interface AppUserService {
    String checkUser(AppUser appUser);
    String fillUserProfile(AppUser appUser);
}
