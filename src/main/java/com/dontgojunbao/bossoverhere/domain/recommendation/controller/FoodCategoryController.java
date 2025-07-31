package com.dontgojunbao.bossoverhere.domain.recommendation.controller;

import com.dontgojunbao.bossoverhere.domain.recommendation.dto.response.ClusterDetailResponse;
import com.dontgojunbao.bossoverhere.domain.recommendation.dto.response.FoodCategoryDetailResponse;
import com.dontgojunbao.bossoverhere.domain.recommendation.dto.response.FoodCategoryDto;
import com.dontgojunbao.bossoverhere.domain.recommendation.service.FoodCategoryService;
import com.dontgojunbao.bossoverhere.global.common.dto.CommonResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Food-Category", description = "food API")
@RestController
@RequestMapping("/api/food-categories")
@RequiredArgsConstructor
public class FoodCategoryController {
    private final FoodCategoryService foodCategoryService;

    @Operation(summary = "모든 음식 카테고리 + 클러스터 ID 조회")
    @GetMapping
    public ResponseEntity<CommonResponse<List<FoodCategoryDetailResponse>>> getFoodCategories(
            @AuthenticationPrincipal Long userId
    ) {
        return ResponseEntity.ok(
                CommonResponse.createSuccess(foodCategoryService.findAll(userId))
        );
    }

    @Operation(summary = "카테고리별 클러스터 조회")
    @GetMapping("/{categoryId}/clusters")
    public ResponseEntity<CommonResponse<List<ClusterDetailResponse>>> getClustersByCategory(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long categoryId
    ) {
        return ResponseEntity.ok(
                CommonResponse.createSuccess(
                        foodCategoryService.findClustersByCategory(userId, categoryId)
                )
        );
    }
}