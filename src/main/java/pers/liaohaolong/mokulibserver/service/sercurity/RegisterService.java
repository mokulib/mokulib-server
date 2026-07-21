package pers.liaohaolong.mokulibserver.service.sercurity;

import org.jspecify.annotations.NonNull;
import org.springframework.security.core.Authentication;

public interface RegisterService {

    void register(@NonNull Authentication authentication);

}
