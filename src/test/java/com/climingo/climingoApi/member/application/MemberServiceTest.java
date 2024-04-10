package com.climingo.climingoApi.member.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.climingo.climingoApi.auth.api.request.SignUpRequest;
import com.climingo.climingoApi.auth.api.response.MemberInfo;
import com.climingo.climingoApi.auth.application.SignUpService;
import com.climingo.climingoApi.member.domain.Member;
import com.climingo.climingoApi.member.domain.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("MemberService Unit Tests")
class MemberServiceTest {

    private SignUpService signUpService;
    private MemberRepository mockedMemberRepository;

    @BeforeEach
    void setUp() {
        mockedMemberRepository = mock(MemberRepository.class);
        signUpService = new MemberService(mockedMemberRepository);
    }

    @Test
    @DisplayName("회원가입 테스트 - 중복되지 않은 provider, authId, nickname이 입력으로 들어온 경우 정상적으로 회원가입이 된다.")
    void test_sign_up() {
        String authId = "1234";
        String providerType = "kakao";
        String nickname = "nickname";

        SignUpRequest request = SignUpRequest.builder()
            .authId(authId)
            .providerType(providerType)
            .nickname(nickname)
            .build();

        Member expected = mock(Member.class);
        when(expected.getId()).thenReturn(1L);
        when(expected.getNickname()).thenReturn(nickname);

        when(mockedMemberRepository.existsByAuthIdAndProviderType(any(), any()))
            .thenReturn(false);

        when(mockedMemberRepository.existsByNickname(any()))
            .thenReturn(false);

        when(mockedMemberRepository.save(any()))
            .thenReturn(expected);

        MemberInfo actual = signUpService.signUp(request);

        assertEquals(actual.getId(), expected.getId());
        assertEquals(actual.getNickname(), expected.getNickname());
    }

    @Test
    @DisplayName("회원가입 테스트 - 중복된 nickname이 입력으로 들어온 경우 회원가입을 할 수 없다.")
    void test_sign_up_when_duplicated_nickname_then_throw_error() {
        String authId = "1234";
        String providerType = "kakao";
        String nickname = "nickname";

        SignUpRequest request = SignUpRequest.builder()
            .authId(authId)
            .providerType(providerType)
            .nickname(nickname)
            .build();

        Member expected = mock(Member.class);
        when(expected.getId()).thenReturn(1L);
        when(expected.getNickname()).thenReturn(nickname);

        when(mockedMemberRepository.existsByAuthIdAndProviderType(any(), any()))
            .thenReturn(false);

        when(mockedMemberRepository.existsByNickname(any()))
            .thenReturn(true);

        when(mockedMemberRepository.save(any()))
            .thenReturn(expected);

        Throwable exception = assertThrows(IllegalArgumentException.class,
            () -> signUpService.signUp(request));
        assertEquals("이미 존재하는 닉네임입니다.", exception.getMessage());
    }

    @Test
    @DisplayName("회원가입 테스트 - 중복된 providerType&authId가 입력으로 들어온 경우 회원가입을 할 수 없다.")
    void test_sign_up_when_duplicated_provider_and_authId_then_throw_error() {
        String authId = "1234";
        String providerType = "kakao";
        String nickname = "nickname";

        SignUpRequest request = SignUpRequest.builder()
            .authId(authId)
            .providerType(providerType)
            .nickname(nickname)
            .build();

        Member expected = mock(Member.class);
        when(expected.getId()).thenReturn(1L);
        when(expected.getNickname()).thenReturn(nickname);

        when(mockedMemberRepository.existsByAuthIdAndProviderType(any(), any()))
            .thenReturn(true);

        when(mockedMemberRepository.existsByNickname(any()))
            .thenReturn(false);

        when(mockedMemberRepository.save(any()))
            .thenReturn(expected);

        Throwable exception = assertThrows(IllegalArgumentException.class,
            () -> signUpService.signUp(request));
        assertEquals("이미 존재하는 회원입니다.", exception.getMessage());
    }

    @Test
    @DisplayName("회원가입 테스트 - authId는 중복이지만 providerType는 다른 케이스가 입력으로 들어온 경우 회원가입 가능하다.")
    void test_sign_up_when_duplicated_authId_but_difference_provider_then_success() {
        String authId = "1234";
        String providerType = "kakao";
        String nickname = "nickname";

        SignUpRequest request = SignUpRequest.builder()
            .authId(authId)
            .providerType(providerType)
            .nickname(nickname)
            .build();

        Member expected = mock(Member.class);
        when(expected.getId()).thenReturn(1L);
        when(expected.getNickname()).thenReturn(nickname);

        when(mockedMemberRepository.existsByAuthIdAndProviderType(any(), any()))
            .thenReturn(false);

        when(mockedMemberRepository.existsByNickname(any()))
            .thenReturn(false);

        when(mockedMemberRepository.save(any()))
            .thenReturn(expected);

        signUpService.signUp(request);
    }
}