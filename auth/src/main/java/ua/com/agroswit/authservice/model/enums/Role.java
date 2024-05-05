package ua.com.agroswit.authservice.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
    ADMIN(Values.ADMIN),
    MANAGER(Values.MANAGER),
    USER(Values.USER);

    final String value;

    public static class Values {
        public final static String ADMIN = "ADMIN";
        public final static String MANAGER = "MANAGER";
        public final static String USER = "USER";
    }
}
