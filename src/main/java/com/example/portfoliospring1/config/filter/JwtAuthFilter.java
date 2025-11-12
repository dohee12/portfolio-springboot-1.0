package com.example.portfoliospring1.config.filter;

import com.example.portfoliospring1.Util.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String accessToken = request.getHeader("Access-Token");

        if (accessToken != null) {
            try {
                Jws<Claims> jws = jwtUtil.parseToken(accessToken);
                Claims body = jws.getBody();
//                String nickname = body.get("nickname").toString();
//                String email = body.get("email").toString();

                Authentication authentication = new UsernamePasswordAuthenticationToken(
                        body.getSubject(),
                        null,
                        Collections.emptyList() // List.of(new SimpleGrantedAuthority("ROLE_USER"))
                );
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (JwtException e) {
                // 시크릿 키 잘 못 됨. 서명이 유효하지 않은 경우
                // 만료
                e.printStackTrace();
            }
        }

        filterChain.doFilter(request, response);
    }
}