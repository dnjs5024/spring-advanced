package org.example.expert.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.time.LocalDateTime;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.example.expert.domain.auth.exception.AuthException;
import org.example.expert.domain.user.enums.UserRole;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
public class UserAuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
        Object handler) throws Exception {

        UserRole userRole = Optional.ofNullable((String) request.getAttribute("userRole"))// 유저 권한 가져오고 없으면 Exception
            .map(UserRole::of)
            .orElseThrow(() -> new AuthException("유저 권한 정보가 없습니다.")
            );

        if (!UserRole.ADMIN.equals(userRole)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "관리자 권한이 없습니다.");
            return false;
        }

        log.info(" 요청 url -> {} ",request.getRequestURI());
        log.info(" 요청 시각 -> {} ", LocalDateTime.now());

        return true;
    }
}
