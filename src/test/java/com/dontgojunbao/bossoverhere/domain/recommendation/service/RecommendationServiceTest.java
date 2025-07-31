package com.dontgojunbao.bossoverhere.domain.recommendation.service;

import com.dontgojunbao.bossoverhere.domain.ai.AiClient;
import com.dontgojunbao.bossoverhere.domain.ai.dto.AiPlanResponse;
import com.dontgojunbao.bossoverhere.domain.recommendation.dao.FoodCategoryClusterRepository;
import com.dontgojunbao.bossoverhere.domain.recommendation.dao.RecommendationRequestRepository;
import com.dontgojunbao.bossoverhere.domain.recommendation.domain.Cluster;
import com.dontgojunbao.bossoverhere.domain.recommendation.domain.FoodCategory;
import com.dontgojunbao.bossoverhere.domain.recommendation.domain.RecommendationRequest;
import com.dontgojunbao.bossoverhere.domain.recommendation.dto.request.RecommendationRequestDto;
import com.dontgojunbao.bossoverhere.domain.recommendation.dto.response.RecommendationDetailDto;
import com.dontgojunbao.bossoverhere.domain.recommendation.dto.response.RecommendationResponse;
import com.dontgojunbao.bossoverhere.domain.recommendation.dto.response.RecommendationSimpleDto;

import com.dontgojunbao.bossoverhere.domain.spot.domain.Spot;

import com.dontgojunbao.bossoverhere.domain.spot.service.SpotService;
import com.dontgojunbao.bossoverhere.domain.user.domain.User;
import com.dontgojunbao.bossoverhere.domain.user.service.UserService;
import com.dontgojunbao.bossoverhere.global.error.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class RecommendationServiceTest {

    @Mock UserService userService;
    @Mock FoodCategoryService foodCategoryService;
    @Mock
    SpotService spotService;
    @Mock ClusterService clusterService;
    @Mock
    FoodCategoryClusterRepository mappingRepo;
    @Mock AiClient aiClient;
    @Mock
    RecommendationRequestRepository requestRepo;
    @Mock RecommendationSaveService requestSaveService;

    @InjectMocks RecommendationService service;

    // -- recommend() tests --

    @Test @DisplayName("recommend: 정상 플랜 생성 및 DTO 반환")
    void recommend_success() {
        // given
        Long userId = 1L;
        RecommendationRequestDto dto = new RecommendationRequestDto();
        dto.setDate(LocalDate.of(2025,7,29));
        dto.setStartTime(LocalTime.of(9,0));
        dto.setEndTime(LocalTime.of(18,0));
        dto.setFoodCategoryId(3L);
        dto.setSpotId(10L);
        dto.setClusterIds(List.of(100L, 200L));

        User user = User.builder().id(userId).build();
        FoodCategory cat = FoodCategory.builder().id(3L).build();
        Spot base = Spot.builder().id(10L).build();

        given(userService.getUserById(userId)).willReturn(user);
        given(foodCategoryService.getFoodCategoryById(3L)).willReturn(cat);
        given(spotService.getSpotById(10L)).willReturn(base);
        given(mappingRepo.findClusterIdsByCategoryId(3L))
                .willReturn(List.of(100L,200L));
        given(clusterService.getClusterById(100L))
                .willReturn(Cluster.builder().id(100L).build());
        given(clusterService.getClusterById(200L))
                .willReturn(Cluster.builder().id(200L).build());

        AiPlanResponse p1 = AiPlanResponse.builder()
                .time("09:00").fromSpotId(10L).toSpotId(20L).build();
        AiPlanResponse p2 = AiPlanResponse.builder()
                .time("12:00").fromSpotId(20L).toSpotId(30L).build();
        given(aiClient.callAiForPlan(dto))
                .willReturn(List.of(p1,p2));
        given(spotService.getSpotById(20L))
                .willReturn(Spot.builder().id(20L).build());
        given(spotService.getSpotById(30L))
                .willReturn(Spot.builder().id(30L).build());

        // capture save
        ArgumentCaptor<RecommendationRequest> captor = ArgumentCaptor.forClass(RecommendationRequest.class);
        doNothing().when(requestSaveService).save(captor.capture());

        // when
        List<RecommendationResponse> result = service.recommend(userId, dto);

        // then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getTime()).isEqualTo("09:00");
        assertThat(result.get(1).getFrom().getSpotId()).isEqualTo(20L);

        RecommendationRequest savedReq = captor.getValue();
        assertThat(savedReq.getClusterPicks()).hasSize(2);
        assertThat(savedReq.getSegments()).hasSize(2);
        then(requestSaveService).should().save(any());
    }

    @Test @DisplayName("recommend: 존재하지 않는 카테고리 입력 시 예외")
    void recommend_categoryNotFound() {
        Long userId = 2L;
        RecommendationRequestDto dto = new RecommendationRequestDto();
        dto.setFoodCategoryId(99L);

        given(userService.getUserById(userId)).willReturn(User.builder().id(userId).build());
        given(foodCategoryService.getFoodCategoryById(99L))
                .willThrow(new ApplicationException(FoodCategoryErrorCode.NOTFOUND_FOOD_CATEGORY));

        ApplicationException ex = catchThrowableOfType(
                () -> service.recommend(userId, dto),
                ApplicationException.class
        );
        assertThat(ex.getErrorCode()).isEqualTo(FoodCategoryErrorCode.NOTFOUND_FOOD_CATEGORY);
    }

    @Test @DisplayName("recommend: 유효하지 않은 clusterId 입력 시 예외")
    void recommend_invalidCluster() {
        Long userId = 3L;
        RecommendationRequestDto dto = new RecommendationRequestDto();
        dto.setDate(LocalDate.now());
        dto.setStartTime(LocalTime.now());
        dto.setEndTime(LocalTime.now());
        dto.setFoodCategoryId(1L);
        dto.setSpotId(5L);
        dto.setClusterIds(List.of(10L,99L));

        given(userService.getUserById(userId)).willReturn(User.builder().id(userId).build());
        given(foodCategoryService.getFoodCategoryById(1L))
                .willReturn(FoodCategory.builder().id(1L).build());
        given(spotService.getSpotById(5L))
                .willReturn(Spot.builder().id(5L).build());
        given(mappingRepo.findClusterIdsByCategoryId(1L))
                .willReturn(List.of(10L,20L));

        ApplicationException ex = catchThrowableOfType(
                () -> service.recommend(userId, dto),
                ApplicationException.class
        );
        assertThat(ex.getErrorCode()).isEqualTo(ClusterErrorCode.INVALID_CLUSTER_SELECTION);
    }

    @Test @DisplayName("recommend: clusterService 에서 NotFoundCluster 예외 전파")
    void recommend_clusterNotFound() {
        Long userId = 4L;
        RecommendationRequestDto dto = new RecommendationRequestDto();
        dto.setDate(LocalDate.now());
        dto.setStartTime(LocalTime.now());
        dto.setEndTime(LocalTime.now());
        dto.setFoodCategoryId(2L);
        dto.setSpotId(6L);
        dto.setClusterIds(List.of(100L));

        given(userService.getUserById(userId)).willReturn(User.builder().id(userId).build());
        given(foodCategoryService.getFoodCategoryById(2L))
                .willReturn(FoodCategory.builder().id(2L).build());
        given(spotService.getSpotById(6L))
                .willReturn(Spot.builder().id(6L).build());
        given(mappingRepo.findClusterIdsByCategoryId(2L))
                .willReturn(List.of(100L));
        given(clusterService.getClusterById(100L))
                .willThrow(new ApplicationException(ClusterErrorCode.NOTFOUND_CLUSTER));

        ApplicationException ex = catchThrowableOfType(
                () -> service.recommend(userId, dto),
                ApplicationException.class
        );
        assertThat(ex.getErrorCode()).isEqualTo(ClusterErrorCode.NOTFOUND_CLUSTER);
    }

    @Test @DisplayName("recommend: SpotService 예외 전파")
    void recommend_spotNotFound() {
        Long userId = 5L;
        RecommendationRequestDto dto = new RecommendationRequestDto();
        dto.setDate(LocalDate.now());
        dto.setStartTime(LocalTime.now());
        dto.setEndTime(LocalTime.now());
        dto.setFoodCategoryId(3L);
        dto.setSpotId(999L);

        given(userService.getUserById(userId)).willReturn(User.builder().id(userId).build());
        given(foodCategoryService.getFoodCategoryById(3L))
                .willReturn(FoodCategory.builder().id(3L).build());
        given(spotService.getSpotById(999L))
                .willThrow(new ApplicationException(SpotErrorCode.NOTFOUND_SPOT));

        ApplicationException ex = catchThrowableOfType(
                () -> service.recommend(userId, dto),
                ApplicationException.class
        );
        assertThat(ex.getErrorCode()).isEqualTo(SpotErrorCode.NOTFOUND_SPOT);
    }

    // -- findAll() tests --

    @Test @DisplayName("findAll: 히스토리 페이징 조회")
    void findAll_success() {
        Long userId = 6L;
        User user = User.builder().id(userId).build();
        FoodCategory cat = FoodCategory.builder().id(3L).build();
        Spot spot = Spot.builder().id(10L).name("A지점").address("주소A").build();

        RecommendationRequest r1 = RecommendationRequest.builder()
                .id(1L).user(user).date(LocalDate.of(2025,7,29))
                .startTime(LocalTime.of(9,0)).endTime(LocalTime.of(18,0))
                .foodCategory(cat).spot(spot).build();
        RecommendationRequest r2 = RecommendationRequest.builder()
                .id(2L).user(user).date(LocalDate.of(2025,7,30))
                .startTime(LocalTime.of(10,0)).endTime(LocalTime.of(19,0))
                .foodCategory(cat).spot(spot).build();
        Page<RecommendationRequest> page =
                new PageImpl<>(List.of(r1, r2), PageRequest.of(0,2), 2);

        given(userService.getUserById(userId)).willReturn(user);
        given(requestRepo.findAllByUserId(userId, PageRequest.of(0,2)))
                .willReturn(page);

        Page<RecommendationSimpleDto> result =
                service.findAll(userId, PageRequest.of(0,2));

        assertThat(result.getTotalElements()).isEqualTo(2);
        assertThat(result.getContent())
                .extracting(RecommendationSimpleDto::getRequestId)
                .containsExactlyInAnyOrder(1L, 2L);
    }

    // -- findOne() tests --

    @Test @DisplayName("findOne: 상세 조회 성공")
    void findOne_success() {
        Long userId = 7L, reqId = 77L;
        User user = User.builder().id(userId).build();
        FoodCategory cat = FoodCategory.builder().id(4L).name("카페").build();
        Spot spot = Spot.builder().id(20L).name("B지점").address("주소B").build();

        RecommendationRequest req = RecommendationRequest.builder()
                .id(reqId).user(user).date(LocalDate.of(2025,8,1))
                .startTime(LocalTime.of(8,0)).endTime(LocalTime.of(17,0))
                .foodCategory(cat).spot(spot).build();

        given(userService.getUserById(userId)).willReturn(user);
        given(requestRepo.findWithDetailsById(reqId))
                .willReturn(Optional.of(req));

        RecommendationDetailDto detail = service.findOne(userId, reqId);

        assertThat(detail.getRequestId()).isEqualTo(reqId);
        assertThat(detail.getDate()).isEqualTo(req.getDate());
        assertThat(detail.getFoodCategory().getId()).isEqualTo(cat.getId());
        assertThat(detail.getSpot().getSpotId()).isEqualTo(spot.getId());
    }

    @Test @DisplayName("findOne: 존재하지 않는 히스토리 예외")
    void findOne_notFound() {
        Long userId = 8L, reqId = 88L;
        given(userService.getUserById(userId)).willReturn(User.builder().id(userId).build());
        given(requestRepo.findWithDetailsById(reqId)).willReturn(Optional.empty());

        ApplicationException ex = catchThrowableOfType(
                () -> service.findOne(userId, reqId),
                ApplicationException.class
        );
        assertThat(ex.getErrorCode()).isEqualTo(RecommendErrorCode.NOT_FOUND_HISTORY);
    }

    @Test @DisplayName("findOne: 권한 없는 히스토리 조회 시 예외")
    void findOne_forbidden() {
        Long userId = 9L, reqId = 99L;
        RecommendationRequest req = RecommendationRequest.builder()
                .id(reqId)
                .user(User.builder().id(123L).build())
                .build();

        given(userService.getUserById(userId)).willReturn(User.builder().id(userId).build());
        given(requestRepo.findWithDetailsById(reqId)).willReturn(Optional.of(req));

        ApplicationException ex = catchThrowableOfType(
                () -> service.findOne(userId, reqId),
                ApplicationException.class
        );
        assertThat(ex.getErrorCode()).isEqualTo(RecommendErrorCode.FORBIDDEN_HISTORY);
    }
}
