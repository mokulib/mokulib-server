package pers.liaohaolong.mokulibserver.exception;

import lombok.experimental.StandardException;
import org.springframework.security.core.AuthenticationException;

@StandardException
public class EmailCaptchaNotFoundException extends AuthenticationException {
}
