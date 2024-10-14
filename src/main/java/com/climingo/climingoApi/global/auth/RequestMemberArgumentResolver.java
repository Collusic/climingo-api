package com.climingo.climingoApi.global.auth;

import com.climingo.climingoApi.member.domain.Member;
import com.climingo.climingoApi.member.domain.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@RequiredArgsConstructor
@Component
public class RequestMemberArgumentResolver implements HandlerMethodArgumentResolver {

    private final MemberRepository memberRepository;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean isLoginMemberAnnotation =
            parameter.getParameterAnnotation(RequestMember.class) != null;
        boolean isMemberClass = Member.class.equals(parameter.getParameterType());
        return isLoginMemberAnnotation && isMemberClass;
    }

    @Override
    public Object resolveArgument(
        MethodParameter parameter, ModelAndViewContainer mavContainer,
        NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (isAnonymousUser(authentication.getPrincipal())) {
            return Member.createGuest();
        }

        if (authentication.isAuthenticated()) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

            return memberRepository.findById(userDetails.getMemberId())
                .orElseThrow(() -> new EntityNotFoundException(
                    "id가 " + userDetails.getMemberId() + "인 회원은 존재하지 않습니다"));
        }

        return Member.createGuest();
    }

    private boolean isAnonymousUser(Object principal) {
        return principal.equals("anonymousUser");
    }
}
