package dev.xamet.dreamgamesstudy.filter;

import dev.xamet.dreamgamesstudy.exception.BadRequestException;
import dev.xamet.dreamgamesstudy.exception.UnauthorizedException;
import dev.xamet.dreamgamesstudy.utility.TokenValidator;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

public class AuthenticationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        if (request.getServletPath().equals("/user") ||                                             // Allow to create new user
                (request.getServletPath().equals("/team") && request.getMethod().equals("GET"))) {  // Allow to get all teams
            filterChain.doFilter(request, response); //continue
        } else {

            // Get user id from request
            int id;
            try{
                id = request.getParameter("userId") != null ?
                        Integer.parseInt(request.getParameter("userId")) :
                        Integer.parseInt(request.getServletPath().substring("/user/".length()));
            }catch (Exception e){
                throw new BadRequestException("Invalid user id");
            }

            if(request.getServletPath().equals("/team")){
                // Check if team name is specified
                if(request.getMethod().equals("POST")){
                    if(request.getParameter("name") == null){
                        throw new BadRequestException("Team name is not specified");
                    }
                // Check if team id is specified
                }else if(request.getMethod().equals("PUT")){
                    if(request.getParameter("teamId") == null){
                        throw new BadRequestException("Team id is not specified");
                    }
                //Probably these controls need to be moved from this class
                }
            }

            // Get token from request and check if it is valid
            String authorizationHeader = request.getHeader(AUTHORIZATION);
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                String token = authorizationHeader.substring("Bearer ".length());
                if (TokenValidator.validateToken(token, id)) {
                    filterChain.doFilter(request, response);
                } else {
                    throw new UnauthorizedException("User is not authorized");
                }
            }else {
                throw new UnauthorizedException("User is not authorized");
            }
        }
    }
}
