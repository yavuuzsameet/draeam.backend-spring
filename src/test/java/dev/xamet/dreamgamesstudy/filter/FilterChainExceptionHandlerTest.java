package dev.xamet.dreamgamesstudy.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FilterChainExceptionHandlerTest {

    @Mock private HandlerExceptionResolver resolver;
    @Mock private FilterChain filterChain;
    @InjectMocks private FilterChainExceptionHandler filterChainExceptionHandler;

    @Test
    void doFilterInternalResolvesExceptions() throws ServletException, IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        ServletException exception = new ServletException("Test Exception");

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("status", HttpStatus.INTERNAL_SERVER_ERROR);
        modelAndView.addObject("message", "Error Message");

        doThrow(exception).when(filterChain).doFilter(request, response);
        when(resolver.resolveException(request, response, null, exception)).thenReturn(modelAndView);

        filterChainExceptionHandler.doFilterInternal(request, response, filterChain);

        verify(resolver).resolveException(request, response, null, exception);
    }
}