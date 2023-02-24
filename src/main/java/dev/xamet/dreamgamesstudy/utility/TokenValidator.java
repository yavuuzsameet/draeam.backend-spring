package dev.xamet.dreamgamesstudy.utility;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.interfaces.DecodedJWT;
import dev.xamet.dreamgamesstudy.exception.UnauthorizedException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TokenValidator implements TokenUtil {

    public static boolean validateToken(String token, int id) throws UnauthorizedException {
        token = token.replace("Bearer ", "");

        JWTVerifier verifier = JWT.require(algo).build();

        DecodedJWT jwt;
        try{
            jwt = verifier.verify(token);
        }catch (Exception e){
            throw new UnauthorizedException("Token cannot be verified, make sure the token is not expired");
        }

        int jwtId = Integer.parseInt(jwt.getSubject());

        return jwtId == id;
    }

}
