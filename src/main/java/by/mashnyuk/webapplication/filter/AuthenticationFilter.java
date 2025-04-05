package by.mashnyuk.webapplication.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Set;

@WebFilter("/*")
public class AuthenticationFilter implements Filter {

    // Команды, которые не требуют аутентификации
    private static final Set<String> PUBLIC_COMMANDS = Set.of("login", "register", "logout");

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpReq = (HttpServletRequest) request;
        HttpServletResponse httpRes = (HttpServletResponse) response;

        String contextPath = httpReq.getContextPath();
        String uri = httpReq.getRequestURI().substring(contextPath.length());
        String command = httpReq.getParameter("command");
        HttpSession session = httpReq.getSession(false);

        // Разрешаем доступ к login.jsp и статике без проверки
        boolean isLoginPage = uri.equals("/login.jsp");
        boolean isStatic = uri.startsWith("/css/") || uri.startsWith("/js/") || uri.startsWith("/images/");

        boolean isPublicCommand = uri.equals("/controller") && PUBLIC_COMMANDS.contains(command);

        if (isLoginPage || isStatic || isPublicCommand) {
            chain.doFilter(request, response);
            return;
        }

        // Проверяем авторизацию
        if (session == null || session.getAttribute("authenticated") == null) {
            httpRes.sendRedirect(contextPath + "/login.jsp");
            return;
        }

        // Предотвращаем кэширование защищённых страниц
        httpRes.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        httpRes.setHeader("Pragma", "no-cache");
        httpRes.setDateHeader("Expires", 0);

        chain.doFilter(request, response);
    }
}
