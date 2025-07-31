package com.dontgojunbao.bossoverhere.domain.recommendation.service;


import com.dontgojunbao.bossoverhere.domain.ai.AiClient;
import com.dontgojunbao.bossoverhere.domain.ai.dto.AiPlanResponse;

import com.dontgojunbao.bossoverhere.domain.recommendation.dao.FoodCategoryClusterRepository;
import com.dontgojunbao.bossoverhere.domain.recommendation.dao.RecommendationRequestRepository;
import com.dontgojunbao.bossoverhere.domain.recommendation.domain.Cluster;
import com.dontgojunbao.bossoverhere.domain.recommendation.domain.FoodCategory;
import com.dontgojunbao.bossoverhere.domain.recommendation.domain.RecommendationRequest;
import com.dontgojunbao.bossoverhere.domain.recommendation.domain.RecommendationSegment;
import com.dontgojunbao.bossoverhere.domain.recommendation.dto.request.RecommendationRequestDto;
import com.dontgojunbao.bossoverhere.domain.recommendation.dto.response.RecommendationDetailDto;
import com.dontgojunbao.bossoverhere.domain.recommendation.dto.response.RecommendationResponse;
import com.dontgojunbao.bossoverhere.domain.recommendation.dto.response.RecommendationSimpleDto;
import com.dontgojunbao.bossoverhere.domain.spot.dao.SpotRepository;

import com.dontgojunbao.bossoverhere.domain.spot.domain.Spot;
import com.dontgojunbao.bossoverhere.domain.spot.dto.SpotDto;


import com.dontgojunbao.bossoverhere.domain.spot.service.SpotService;
import com.dontgojunbao.bossoverhere.domain.user.domain.User;
import com.dontgojunbao.bossoverhere.domain.user.service.UserService;
import com.dontgojunbao.bossoverhere.global.error.ApplicationException;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.stream.Collectors;

import static com.dontgojunbao.bossoverhere.global.error.ClusterErrorCode.INVALID_CLUSTER_SELECTION;
import static com.dontgojunbao.bossoverhere.global.error.RecommendErrorCode.FORBIDDEN_HISTORY;
import static com.dontgojunbao.bossoverhere.global.error.RecommendErrorCode.NOT_FOUND_HISTORY;
import static com.dontgojunbao.bossoverhere.global.error.SpotErrorCode.NOTFOUND_SPOT;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RecommendationService {

    private final UserService userService;
    private final FoodCategoryService foodCategoryService;
    private final SpotService spotService;
    private final ClusterService clusterService;
    private final RecommendationSaveService requestSaveService;
    private final AiClient aiClient;
    private final FoodCategoryClusterRepository mappingRepo;
    private final RecommendationRequestRepository requestRepo;


    public List<RecommendationResponse> recommend(Long userId, RecommendationRequestDto dto) {
        // 1) 유저·카테고리·스팟 로딩
        User user = userService.getUserById(userId);
        FoodCategory foodCategory = foodCategoryService.getFoodCategoryById(dto.getFoodCategoryId());
        Spot baseSpot = spotService.getSpotById(dto.getSpotId());


        // 2) Request 초기 생성
        RecommendationRequest request = buildRecommendationRequest(dto, user, foodCategory, baseSpot);
        // 3) Cluster 선택 검증 & 추가
        processClusterSelections(dto, foodCategory, request);

        // 4) AI 플랜 호출 & Segment 추가
        processAiPlans(dto, request);

        // 5) 저장
        requestSaveService.save(request);
        // 8) DTO 변환 후 반환
        return RecommendationResponse.fromEntityList(request.getSegments());
    }

    public Page<RecommendationSimpleDto> findAll(Long userId, Pageable pageable) {
        // 1) 인증
        userService.getUserById(userId);

        // 2) 페이징 조회 & DTO 변환
        return requestRepo.findAllByUserId(userId, pageable)
                .map(RecommendationSimpleDto::fromEntity);
    }

    public RecommendationDetailDto findOne(Long userId, Long requestId) {
        // 1) 로그인 검증
        userService.getUserById(userId);

        // 2) 상세 조회 (EntityGraph 로 미리 패치)
        RecommendationRequest req = requestRepo
                .findWithDetailsById(requestId)
                .orElseThrow(() -> new ApplicationException(NOT_FOUND_HISTORY));

        // 3) 본인 것인지 권한 검증
        if (!req.getUser().getId().equals(userId)) {
            throw new ApplicationException(FORBIDDEN_HISTORY);
        }

        // 4) DTO 변환
        return RecommendationDetailDto.fromEntity(req);
    }

    private void processAiPlans(RecommendationRequestDto dto, RecommendationRequest request) {
        List<AiPlanResponse> plans = aiClient.callAiForPlan(dto);
        for (AiPlanResponse p : plans) {
            Spot from = spotService.getSpotById(p.getFromSpotId());
            Spot to   = spotService.getSpotById(p.getToSpotId());
            // 도메인 팩토리 사용
            request.addSegment(RecommendationSegment.fromPlan(p.getTime(), from, to));
        }
    }

    private void processClusterSelections(RecommendationRequestDto dto, FoodCategory foodCategory, RecommendationRequest request) {
        List<Long> validClusterIds =
                mappingRepo.findClusterIdsByCategoryId(foodCategory.getId());

        // 5) clusterIds 검증 + 엔티티 로딩 + request 에 추가
        for (Long cid : dto.getClusterIds()) {
            if (!validClusterIds.contains(cid)) {
                throw new ApplicationException(INVALID_CLUSTER_SELECTION);
            }
            Cluster cluster = clusterService.getClusterById(cid); // 존재하지 않으면 NOTFOUND_CLUSTER
            request.addClusterPick(cluster);
        }
    }

    private RecommendationRequest buildRecommendationRequest(RecommendationRequestDto dto, User user, FoodCategory foodCategory, Spot baseSpot) {
        return RecommendationRequest.of(
                user,
                dto.getDate(),
                dto.getStartTime(),
                dto.getEndTime(),
                foodCategory,
                baseSpot
        );
    }









}
