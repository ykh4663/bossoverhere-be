package com.dontgojunbao.bossoverhere.domain.recommendation.service;

import com.dontgojunbao.bossoverhere.domain.ai.AiClient;
import com.dontgojunbao.bossoverhere.domain.ai.dto.AiPlanResponse;
import com.dontgojunbao.bossoverhere.domain.recommendation.dto.request.RecommendationRequestDto;
import com.dontgojunbao.bossoverhere.domain.recommendation.dto.response.RecommendationResponse;
import com.dontgojunbao.bossoverhere.domain.spot.dao.SpotRepository;
import com.dontgojunbao.bossoverhere.domain.spot.domain.Spot;

import com.dontgojunbao.bossoverhere.domain.user.domain.User;
import com.dontgojunbao.bossoverhere.domain.user.service.UserService;
import com.dontgojunbao.bossoverhere.global.error.ApplicationException;
import com.dontgojunbao.bossoverhere.global.error.SpotErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class RecommendationServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private AiClient aiClient;

    @Mock
    private SpotRepository spotRepository;

    @InjectMocks
    private RecommendationService recommendationService;

    @Test
    @DisplayName("recommend: 정상 플랜 반환")
    void recommend_success() {
        // given
        Long userId = 1L;
        RecommendationRequestDto dto = new RecommendationRequestDto();
        dto.setDate(LocalDate.of(2025, 7, 29));
        dto.setStartTime(LocalTime.of(9, 0));
        dto.setEndTime(LocalTime.of(18, 0));
        dto.setFoodCategoryId(5L);
        dto.setSpotId(10L);
        dto.setClusterIds(List.of(1L, 2L));

        // 로그인 검증 stub
        given(userService.getUserById(userId))
                .willReturn(User.builder().id(userId).build());

        // AI 응답 stub
        AiPlanResponse p1 = AiPlanResponse.builder()
                .time("09:00")
                .fromSpotId(10L)
                .toSpotId(20L)
                .build();
        AiPlanResponse p2 = AiPlanResponse.builder()
                .time("12:00")
                .fromSpotId(20L)
                .toSpotId(30L)
                .build();
        given(aiClient.callAiForPlan(dto))
                .willReturn(List.of(p1, p2));

        // 스팟 조회 stub
        given(spotRepository.findById(10L))
                .willReturn(Optional.of(
                        Spot.builder()
                                .id(10L)
                                .name("A지점")
                                .address("주소A")
                                .latitude(0.0)
                                .longitude(0.0)
                                .build()
                ));
        given(spotRepository.findById(20L))
                .willReturn(Optional.of(
                        Spot.builder()
                                .id(20L)
                                .name("B지점")
                                .address("주소B")
                                .latitude(0.0)
                                .longitude(0.0)
                                .build()
                ));
        given(spotRepository.findById(30L))
                .willReturn(Optional.of(
                        Spot.builder()
                                .id(30L)
                                .name("C지점")
                                .address("주소C")
                                .latitude(0.0)
                                .longitude(0.0)
                                .build()
                ));
        // when
        List<RecommendationResponse> result = recommendationService.recommend(userId, dto);

        // then
        assertThat(result).hasSize(2);

        RecommendationResponse r1 = result.get(0);
        assertThat(r1.getTime()).isEqualTo("09:00");
        assertThat(r1.getFrom().getSpotId()).isEqualTo(10L);
        assertThat(r1.getTo().getSpotId()).isEqualTo(20L);

        RecommendationResponse r2 = result.get(1);
        assertThat(r2.getTime()).isEqualTo("12:00");
        assertThat(r2.getFrom().getSpotId()).isEqualTo(20L);
        assertThat(r2.getTo().getSpotId()).isEqualTo(30L);

        then(userService).should().getUserById(userId);
        then(aiClient).should().callAiForPlan(dto);
        // 10L 은 한 번
        then(spotRepository).should(times(1)).findById(10L);
        // 20L 은 두 번 (첫 플랜의 to, 두 번째 플랜의 from)
        then(spotRepository).should(times(2)).findById(20L);
        // 30L 은 한 번
        then(spotRepository).should(times(1)).findById(30L);
    }

    @Test
    @DisplayName("recommend: 출발지 스팟을 찾지 못하면 예외 발생")
    void recommend_fromSpotNotFound() {
        // given
        Long userId = 2L;
        RecommendationRequestDto dto = new RecommendationRequestDto();

        // 로그인 검증 stub
        given(userService.getUserById(userId))
                .willReturn(User.builder().id(userId).build());

        // AI 응답 stub
        AiPlanResponse p = AiPlanResponse.builder()
                .time("09:00")
                .fromSpotId(99L)
                .toSpotId(100L)
                .build();
        given(aiClient.callAiForPlan(dto))
                .willReturn(List.of(p));

        // 출발지 스팟이 없을 때
        given(spotRepository.findById(99L))
                .willReturn(Optional.empty());

        // when / then
        ApplicationException ex = catchThrowableOfType(
                () -> recommendationService.recommend(userId, dto),
                ApplicationException.class
        );
        assertThat(ex.getErrorCode()).isEqualTo(SpotErrorCode.NOTFOUND_SPOT);

        then(userService).should().getUserById(userId);
        then(aiClient).should().callAiForPlan(dto);
        then(spotRepository).should().findById(99L);
        then(spotRepository).should(never()).findById(100L);
    }

    @Test
    @DisplayName("recommend: 도착지 스팟을 찾지 못하면 예외 발생")
    void recommend_toSpotNotFound() {
        // given
        Long userId = 3L;
        RecommendationRequestDto dto = new RecommendationRequestDto();

        // 로그인 검증 stub
        given(userService.getUserById(userId))
                .willReturn(User.builder().id(userId).build());

        // AI 응답 stub
        AiPlanResponse p = AiPlanResponse.builder()
                .time("12:00")
                .fromSpotId(11L)
                .toSpotId(22L)
                .build();
        given(aiClient.callAiForPlan(dto))
                .willReturn(List.of(p));

        // 출발지 스팟은 존재하지만
        given(spotRepository.findById(11L))
                .willReturn(Optional.of(
                        Spot.builder()
                                .id(11L)
                                .name("A지점")
                                .address("주소A")
                                .latitude(0.0)
                                .longitude(0.0)
                                .build()
                ));

// 도착지 스팟이 없을 때
        given(spotRepository.findById(22L))
                .willReturn(Optional.empty());

        // when / then
        ApplicationException ex = catchThrowableOfType(
                () -> recommendationService.recommend(userId, dto),
                ApplicationException.class
        );
        assertThat(ex.getErrorCode()).isEqualTo(SpotErrorCode.NOTFOUND_SPOT);

        then(userService).should().getUserById(userId);
        then(aiClient).should().callAiForPlan(dto);
        then(spotRepository).should().findById(11L);
        then(spotRepository).should().findById(22L);
    }
}