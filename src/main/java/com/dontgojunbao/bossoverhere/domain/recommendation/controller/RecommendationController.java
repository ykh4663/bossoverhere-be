package com.dontgojunbao.bossoverhere.domain.recommendation.controller;

import com.dontgojunbao.bossoverhere.domain.recommendation.dto.request.RecommendationRequestDto;
import com.dontgojunbao.bossoverhere.domain.recommendation.dto.response.RecommendationSegmentDto;
import com.dontgojunbao.bossoverhere.domain.recommendation.service.RecommendationService;
import com.dontgojunbao.bossoverhere.global.common.dto.CommonResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<CommonResponse<List<RecommendationSegmentDto>>> recommend(
            @RequestBody @Valid RecommendationRequestDto dto,
            @AuthenticationPrincipal Long userId
    ) {
        List<RecommendationSegmentDto> plan = recommendationService.recommend(userId, dto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(CommonResponse.createSuccess(plan));
    }


}
