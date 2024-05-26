package com.climingo.climingoApi.global.auth;

import com.climingo.climingoApi.auth.util.CookieUtils;
import com.climingo.climingoApi.auth.util.JwtUtil;
import com.climingo.climingoApi.member.domain.Member;
import com.climingo.climingoApi.member.domain.MemberRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.util.NoSuchElementException;
import java.util.Optional;
import javax.naming.AuthenticationException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@RequiredArgsConstructor
@Component
public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {

    private final MemberRepository memberRepository;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean isLoginMemberAnnotation = parameter.getParameterAnnotation(LoginMember.class) != null;
        boolean isMemberClass = Member.class.equals(parameter.getParameterType());
        return isLoginMemberAnnotation && isMemberClass;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory)
        throws AuthenticationException {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        String token;
        String authId;
        String providerType;

        Optional<Cookie> accessTokenCookie = CookieUtils.getCookie(request, JwtUtil.ACCESS_TOKEN_NAME);
        if (accessTokenCookie.isPresent()) {
            token = accessTokenCookie.get().getValue();
            authId = JwtUtil.getAuthId(token);
            providerType = JwtUtil.getProviderType(token);

            return memberRepository.findByAuthIdAndProviderType(authId, providerType).orElseThrow(() -> new NoSuchElementException("존재하지 않는 회원: " + authId + " " + providerType));
        }

        Optional<Cookie> refreshTokenCookie = CookieUtils.getCookie(request, JwtUtil.REFRESH_TOKEN_NAME);
        token = refreshTokenCookie.orElseThrow(AuthenticationException::new).getValue();
        authId = JwtUtil.getAuthId(token);
        providerType = JwtUtil.getProviderType(token);

        return memberRepository.findByAuthIdAndProviderType(authId, providerType).orElseThrow(() -> new NoSuchElementException("존재하지 않는 회원: " + authId + " " + providerType));
    }
}
