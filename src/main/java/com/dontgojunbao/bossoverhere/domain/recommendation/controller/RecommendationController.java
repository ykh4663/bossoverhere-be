package com.dontgojunbao.bossoverhere.domain.recommendation.controller;

import com.dontgojunbao.bossoverhere.domain.recommendation.dto.request.RecommendationRequestDto;
import com.dontgojunbao.bossoverhere.domain.recommendation.dto.response.RecommendationDetailDto;
import com.dontgojunbao.bossoverhere.domain.recommendation.dto.response.RecommendationResponse;
import com.dontgojunbao.bossoverhere.domain.recommendation.dto.response.RecommendationSimpleDto;
import com.dontgojunbao.bossoverhere.domain.recommendation.service.RecommendationService;
import com.dontgojunbao.bossoverhere.global.common.dto.CommonResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Recommendation", description = "추천 API")
@RestController
@RequestMapping("/api/recommendations")
@RequiredArgsConstructor
public class RecommendationController {
    private final RecommendationService recommendationService;
    @Operation(
            summary = "추천 요청",
            description = "문항, 스팟, 날짜·시간을 전달하면 AI 추천 플랜 리스트를 반환합니다."
    )
    @PostMapping
    public ResponseEntity<CommonResponse<List<RecommendationResponse>>> recommend(
            @RequestBody @Valid RecommendationRequestDto dto,
            @AuthenticationPrincipal Long userId
    ) {
        List<RecommendationResponse> plan = recommendationService.recommend(userId, dto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(CommonResponse.createSuccess(plan));
    }

    @Operation(summary = "내 추천 요청 내역 조회", description = "내가 만든 추천 요청 이력(페이징)")
    @GetMapping("/history")
    public ResponseEntity<CommonResponse<Page<RecommendationSimpleDto>>> getHistory(
            @AuthenticationPrincipal Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<RecommendationSimpleDto> result = recommendationService.findAll(
                userId,
                PageRequest.of(page, size, Sort.by("date").descending())
        );
        return ResponseEntity.ok(CommonResponse.createSuccess(result));
    }

    @Operation(summary = "추천 이력 상세 조회", description = "단일 추천 이력과 선택한 클러스터, 구간 정보를 반환합니다.")
    @GetMapping("/{requestId}")
    public ResponseEntity<CommonResponse<RecommendationDetailDto>> getHistoryDetail(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long requestId
    ) {
        RecommendationDetailDto dto =
                recommendationService.findOne(userId, requestId);
        return ResponseEntity.ok(CommonResponse.createSuccess(dto));
    }


}
