package co.edu.uts.inmobiliaria.filter;

import co.edu.uts.inmobiliaria.controller.SessionState;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebFilter("/app/*")
public final class AuthFilter implements Filter {
    @Override
    public void init(javax.servlet.FilterConfig filterConfig) throws ServletException {
        // No requiere configuracion adicional.
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        Object userId = httpRequest.getSession(false) == null
                ? null : httpRequest.getSession(false).getAttribute("usuarioId");
        if (userId == null) {
            httpRequest.getSession(true).setAttribute(SessionState.LOGIN_TARGET,
                    httpRequest.getRequestURI().substring(httpRequest.getContextPath().length()));
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/auth/login.jsp");
            return;
        }
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // No mantiene recursos propios.
    }
}
