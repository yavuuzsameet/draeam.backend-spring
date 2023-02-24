package dev.xamet.dreamgamesstudy.utility;

import com.auth0.jwt.JWT;
import java.util.Date;

public class TokenGenerator implements TokenUtil {

    public static final String ACCESS_TOKEN = "access-token";
    public static final String REFRESH_TOKEN = "refresh-token";
    public static String generateToken(String type, int id) {
        long expirationTime = 0;
        switch (type) {
            case ACCESS_TOKEN:
                expirationTime = 1000 * 60 * 1000;
                break;
            case REFRESH_TOKEN:
                expirationTime = 100000 * 60 * 1000;
                break;
        }

        String token = JWT.create()
                .withSubject(String.valueOf(id))
                .withExpiresAt(new Date(System.currentTimeMillis() + expirationTime))
                .sign(algo);

        return token;
    }

}
