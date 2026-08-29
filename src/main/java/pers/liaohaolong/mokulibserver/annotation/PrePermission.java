package pers.liaohaolong.mokulibserver.annotation;

import pers.liaohaolong.mokulibserver.security.Permission;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Arrays;
import java.util.Set;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface PrePermission {

    Permission[] value() default {};

    MatchStrategy strategy() default MatchStrategy.ANY;

    enum MatchStrategy {
        ANY {
            @Override
            public boolean match(Permission[] required, Set<String> owned) {
                return Arrays.stream(required).anyMatch(p -> owned.contains(p.name()));
            }
        },
        ALL {
            @Override
            public boolean match(Permission[] required, Set<String> owned) {
                return Arrays.stream(required).allMatch(p -> owned.contains(p.name()));
            }
        },
        ;

        public abstract boolean match(Permission[] required, Set<String> owned);
    }

}
