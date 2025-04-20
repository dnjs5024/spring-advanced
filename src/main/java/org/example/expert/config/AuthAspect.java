package org.example.expert.config;

import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Slf4j
@Aspect //	이 클래스는 AOP 기능을 가진 클래스임을 명시
@Component
public class AuthAspect {

    //org.example.expert.domain.user.controller.UserAdminController.changeUserRole()

    /**
     * 설명 @Around * -> 와일드 카드 모든 값 리턴 즉 Object org.뭐시기 -> 경로, deleteComment(..)의 .. 와일드 카드 0개 이상의 매개변수
     *
     * @param point 대상 메서드 실행을 제어할 때 사용
     * @return 응답 본문
     */
    @Around("execution(* org.example.expert.domain.comment.controller.CommentAdminController.deleteComment(..))")
    public Object deleteCommentAuthCheck(ProceedingJoinPoint point) throws Throwable {
        return setLog(point);
    }
    @Around("execution(* org.example.expert.domain.user.controller.UserAdminController.changeUserRole(..))")
    public Object changeUserAuthCheck(ProceedingJoinPoint point) throws Throwable {
        return setLog(point);
    }

    /**
     * log 출력 해주는 메소드
     * @param point 대상 메서드 실행을 제어하기 위해 받아옴
     * @return 응답 본문
     * @throws Throwable 뭘까
     */
    public Object setLog(ProceedingJoinPoint point) throws Throwable {
        // 타겟 메소드 실행전
        HttpServletRequest request = // 스프링이 현재 요청 컨텍스트에서 자동으로 꺼내줌
            ((ServletRequestAttributes) Objects.requireNonNull(
                RequestContextHolder.getRequestAttributes())).getRequest();

        Object[] arg = point.getArgs();
        log.info("요청한 사용자의 ID -> {}", arg[0]); // 대상 메소드의 매개변수에 들어온 값 가져옴 배열형태
        log.info("API 요청 시각 -> {}", LocalDateTime.now());
        log.info("API 요청 URL -> {}", request.getRequestURI());
        log.info("요청 본문 -> {}", arg);
        Object responseObj = point.proceed(); // 대상 메소드 실행
        log.info("응답 본문 -> {}", responseObj); // 타겟 메소드 실행 후
        return responseObj;
    }
}
