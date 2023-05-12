package com.hometask.citylist.config.jwt;

import com.hometask.citylist.exception.BadRequestException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 * @author Elnur Mammadov
 */
public class JwtTokenFilter extends GenericFilterBean {
    private JwtTokenGenerator jwtTokenGenerator;

    public JwtTokenFilter(JwtTokenGenerator jwtTokenGenerator) {
        this.jwtTokenGenerator = jwtTokenGenerator;
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain) {
        try {
            String token = jwtTokenGenerator.resolveToken((HttpServletRequest) req);
            if (token != null && jwtTokenGenerator.validateToken(token)) {
                Authentication auth = jwtTokenGenerator.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
            filterChain.doFilter(req, res);
        } catch (Exception ex) {
            throw new BadRequestException("Error: " + ex.getMessage());
        }

    }

}
