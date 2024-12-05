package ru.itis.dis301.lab07.filters;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

@WebFilter("/*")
public class AutentificationFilter implements Filter {
private final static String USER_NAME = "username";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        Map<String, Long> authenticationData =
                (Map<String, Long>) request.getServletContext().getAttribute("AUTH_DATA");

        if (httpServletRequest.getServletPath().startsWith("/static/") ||
                httpServletRequest.getServletPath().startsWith("/usercheck")){

            filterChain.doFilter(request, response);
        } else {
            if (httpServletRequest.getCookies() != null && !authenticationData.isEmpty()) {

                Cookie[] cookies = httpServletRequest.getCookies();
                Optional<Cookie> cookieLogin =
                        Arrays.stream(cookies)
                                .filter(c -> c.getName().equals("LOGIN"))
                                .findFirst();
                String user_value = cookieLogin.get().getValue();
                Long loginId = authenticationData.get(user_value);
                Optional<Cookie> cookieId =
                        Arrays.stream(cookies)
                                .filter(c -> c.getName().equals("LOGIN_ID")
                                        && c.getValue().equals(loginId.toString()))
                                .findFirst();
                if (cookieId.isPresent()) {
                    filterChain.doFilter(request, response);
                } else {
                    request.getRequestDispatcher("/login").forward(request, response);
                }
            } else {
                request.getRequestDispatcher("/login").forward(request, response);
            }
        }
    }
}
