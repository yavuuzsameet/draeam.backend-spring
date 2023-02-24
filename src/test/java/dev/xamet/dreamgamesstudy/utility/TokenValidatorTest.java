package dev.xamet.dreamgamesstudy.utility;

import com.auth0.jwt.JWT;
import dev.xamet.dreamgamesstudy.exception.UnauthorizedException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TokenValidatorTest {

    @Test
    void validateTokenTrue() {
        // Arrange
        int id = 0;
        String token = "Bearer " + JWT.create()
                .withSubject(String.valueOf(id))
                .sign(TokenUtil.algo);


        // Act
        boolean result = TokenValidator.validateToken(token, id);

        // Assert
        assertTrue(result);
    }

    @Test
    void validateTokenFalse() {
        // Arrange
        int id = 1;
        String token = "Bearer " + JWT.create()
                .withSubject("0")
                .sign(TokenUtil.algo);


        // Act
        boolean result = TokenValidator.validateToken(token, id);

        // Assert
        assertFalse(result);
    }

    @Test
    void validateTokenInvalid() {
        // Arrange
        String token = "invalid token";
        int id = 1;

        // Act
        Throwable exception = assertThrows(UnauthorizedException.class, () -> {
            TokenValidator.validateToken(token, id);
        });

        // Assert
        assertEquals("Token cannot be verified, make sure the token is not expired", exception.getMessage());
    }
}