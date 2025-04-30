package by.mashnyuk.webapplication.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

public class EncodingFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;

        if (httpRequest.getQueryString() != null) {
            String decodedQuery = URLDecoder.decode(httpRequest.getQueryString(), StandardCharsets.UTF_8);
            httpRequest = new DecodedRequestWrapper(httpRequest, decodedQuery);
        }

        chain.doFilter(httpRequest, response);

    }
}


class DecodedRequestWrapper extends HttpServletRequestWrapper {
    private final String decodedQuery;

    public DecodedRequestWrapper(HttpServletRequest request, String decodedQuery) {
        super(request);
        this.decodedQuery = decodedQuery;
    }

    @Override
    public String getQueryString() {
        return decodedQuery;
    }
}