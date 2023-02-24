package dev.xamet.dreamgamesstudy.filter;

import dev.xamet.dreamgamesstudy.exception.BadRequestException;
import dev.xamet.dreamgamesstudy.exception.UnauthorizedException;
import dev.xamet.dreamgamesstudy.utility.TokenValidator;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationFilterTest {

    private AuthenticationFilter authenticationFilter;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        authenticationFilter = new AuthenticationFilter();
    }

    @Test
    public void doFilterInternal_ValidToken() throws Exception {
        // Arrange
        int userId = 1;
        String token = "valid_token";
        String authorizationHeader = "Bearer " + token;

        when(request.getServletPath()).thenReturn("/user/1");
        when(request.getHeader(AUTHORIZATION)).thenReturn(authorizationHeader);

        MockedStatic<TokenValidator> tokenValidatorMockedStatic = mockStatic(TokenValidator.class);
        tokenValidatorMockedStatic.when(() -> TokenValidator.validateToken(token, userId)).thenReturn(true);

        // Act
        authenticationFilter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(filterChain).doFilter(request, response);

        tokenValidatorMockedStatic.close();
    }

    @Test
    public void doFilterInternal_InvalidToken() throws Exception {
        // Arrange
        int userId = 1;
        String token = "invalid_token";
        String authorizationHeader = "Bearer " + token;

        when(request.getServletPath()).thenReturn("/user/1");
        when(request.getHeader(AUTHORIZATION)).thenReturn(authorizationHeader);

        MockedStatic<TokenValidator> tokenValidatorMockedStatic = mockStatic(TokenValidator.class);
        tokenValidatorMockedStatic.when(() -> TokenValidator.validateToken(token, userId)).thenReturn(false);

        // Assert
        Throwable exception = assertThrows(UnauthorizedException.class, () -> {
            authenticationFilter.doFilterInternal(request, response, filterChain);
        });

        assertEquals("User is not authorized", exception.getMessage());

        tokenValidatorMockedStatic.close();
    }

    @Test
    public void doFilterInternal_TokenNotGiven() throws Exception {
        // Arrange
        int userId = 1;

        when(request.getServletPath()).thenReturn("/user/1");
        when(request.getHeader(AUTHORIZATION)).thenReturn(null);

        // Assert
        Throwable exception = assertThrows(UnauthorizedException.class, () -> {
            authenticationFilter.doFilterInternal(request, response, filterChain);
        });

        assertEquals("User is not authorized", exception.getMessage());
    }

    @Test
    public void doFilterInternal_AllowPassUser() throws ServletException, IOException {
        when(request.getServletPath()).thenReturn("/user");
        authenticationFilter.doFilterInternal(request, response, filterChain);
        verify(filterChain).doFilter(request, response);
    }

    @Test
    public void doFilterInternal_AllowPassTeam() throws ServletException, IOException {
        when(request.getServletPath()).thenReturn("/team");
        when(request.getMethod()).thenReturn("GET");
        authenticationFilter.doFilterInternal(request, response, filterChain);
        verify(filterChain).doFilter(request, response);
    }

    @Test
    public void doFilterInternal_InvalidUserId() {
        when(request.getServletPath()).thenReturn("/team");
        when(request.getMethod()).thenReturn("POST");

        Throwable exception = assertThrows(BadRequestException.class, () -> {
            authenticationFilter.doFilterInternal(request, response, filterChain);
        });

        assertEquals("Invalid user id", exception.getMessage());
    }

    @Test
    public void doFilterInternal_TeamNameNotGiven(){
        when(request.getServletPath()).thenReturn("/team");
        when(request.getMethod()).thenReturn("POST");
        when(request.getParameter("userId")).thenReturn("1");

        Throwable exception = assertThrows(BadRequestException.class, () -> {
            authenticationFilter.doFilterInternal(request, response, filterChain);
        });

        assertEquals("Team name is not specified", exception.getMessage());
    }

    @Test
    public void doFilterInternal_TeamIdNotGiven(){
        when(request.getServletPath()).thenReturn("/team");
        when(request.getMethod()).thenReturn("PUT");
        when(request.getParameter("userId")).thenReturn("1");

        Throwable exception = assertThrows(BadRequestException.class, () -> {
            authenticationFilter.doFilterInternal(request, response, filterChain);
        });

        assertEquals("Team id is not specified", exception.getMessage());
    }

}