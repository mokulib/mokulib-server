package pers.liaohaolong.mokulibserver.security;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public enum Permission {

    ALL("*:*"),
    ;

    private final String value;

    Permission(String value) {
        this.value = value;
    }

}
