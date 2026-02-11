package com.climingo.climingoApi.member.api;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.climingo.climingoApi.global.auth.RequestMember;
import com.climingo.climingoApi.global.exception.GlobalExceptionHandler;
import com.climingo.climingoApi.member.api.response.MemberInfoResponse;
import com.climingo.climingoApi.member.application.MemberService;
import com.climingo.climingoApi.member.domain.Member;
import com.climingo.climingoApi.member.domain.PhysicalInfo;
import com.climingo.climingoApi.member.domain.UserRole;
import com.climingo.climingoApi.message.error.ErrorAlertMessageProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@DisplayName("MemberController 단위 테스트")
class MemberControllerTest {

    private MockMvc mockMvc;
    private MemberService memberService;
    private ObjectMapper objectMapper;
    private Member loginMember;

    @BeforeEach
    void setUp() {
        memberService = mock(MemberService.class);
        objectMapper = new ObjectMapper();

        loginMember = Member.builder()
                .id(1L)
                .authId("auth123")
                .providerType("kakao")
                .nickname("testUser")
                .email("test@test.com")
                .profileUrl("http://profile.url")
                .physicalInfo(new PhysicalInfo(new BigDecimal("175.5"), new BigDecimal("70.0"), new BigDecimal("180.0")))
                .role(UserRole.USER)
                .build();

        HandlerMethodArgumentResolver requestMemberResolver = new HandlerMethodArgumentResolver() {
            @Override
            public boolean supportsParameter(MethodParameter parameter) {
                return parameter.getParameterAnnotation(RequestMember.class) != null
                        && Member.class.equals(parameter.getParameterType());
            }

            @Override
            public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                          NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
                return loginMember;
            }
        };

        mockMvc = MockMvcBuilders.standaloneSetup(new MemberController(memberService))
                .setCustomArgumentResolvers(requestMemberResolver)
                .setControllerAdvice(new GlobalExceptionHandler(mock(ErrorAlertMessageProvider.class)))
                .build();
    }

    @Test
    @DisplayName("GET /members - 로그인한 회원이 자신의 정보를 정상 조회한다")
    void findMyInfo_success() throws Exception {
        MemberInfoResponse response = new MemberInfoResponse(loginMember);
        when(memberService.findMemberInfo(eq(1L))).thenReturn(response);

        mockMvc.perform(get("/members"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.memberId").value(1L))
                .andExpect(jsonPath("$.nickname").value("testUser"))
                .andExpect(jsonPath("$.email").value("test@test.com"))
                .andExpect(jsonPath("$.profileUrl").value("http://profile.url"))
                .andExpect(jsonPath("$.providerType").value("kakao"))
                .andExpect(jsonPath("$.physicalInfo.height").value(175.5))
                .andExpect(jsonPath("$.physicalInfo.weight").value(70.0))
                .andExpect(jsonPath("$.physicalInfo.armSpan").value(180.0));
    }

    @Test
    @DisplayName("GET /members - 존재하지 않는 회원 조회 시 EntityNotFoundException이 발생한다")
    void findMyInfo_notFound() throws Exception {
        when(memberService.findMemberInfo(eq(1L)))
                .thenThrow(new EntityNotFoundException("id가 1인 회원은 존재하지 않습니다"));

        mockMvc.perform(get("/members"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("PATCH /member/{memberId} - 유효한 요청으로 신체 정보를 업데이트한다")
    void updatePhysicalInfo_success() throws Exception {
        doNothing().when(memberService).updatePhysicalInfo(any(Member.class), eq(1L), any(PhysicalInfo.class));

        String requestBody = objectMapper.writeValueAsString(
                java.util.Map.of("physicalInfo", java.util.Map.of(
                        "height", 180.0,
                        "weight", 75.5,
                        "armSpan", 185.0
                ))
        );

        mockMvc.perform(patch("/member/{memberId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("PATCH /member/{memberId} - physicalInfo 없이 호출 시 400 에러가 발생한다")
    void updatePhysicalInfo_missingBody() throws Exception {
        mockMvc.perform(patch("/member/{memberId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }
}
