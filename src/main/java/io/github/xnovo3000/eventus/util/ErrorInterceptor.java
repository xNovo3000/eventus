package io.github.xnovo3000.eventus.util;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Interceptor that runs for each HTTP request, removes the error attribute
 * from the session and injects it in the request attribute. This way
 * the error can be intercepted from Thymeleaf
 */
public class ErrorInterceptor extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(
            @NotNull HttpServletRequest request,
            @NotNull HttpServletResponse response,
            @NotNull FilterChain filterChain
    ) throws ServletException, IOException {
        // Get session
        val session = request.getSession();
        // Remove error from session and set to the single request
        val error = session.getAttribute("error");
        if (error != null) {
            request.setAttribute("error", error);
            session.removeAttribute("error");
        }
        // Continue filter chain
        filterChain.doFilter(request, response);
    }

}