package pers.liaohaolong.mokulibserver.service.business;

import pers.liaohaolong.mokulibserver.dto.GetCaptchaDTO;

import java.io.IOException;

public interface CaptchaService {

    GetCaptchaDTO getCaptcha() throws IOException;

}
