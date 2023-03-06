package io.github.xnovo3000.eventus.util;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.val;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class ErrorInterceptor extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
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