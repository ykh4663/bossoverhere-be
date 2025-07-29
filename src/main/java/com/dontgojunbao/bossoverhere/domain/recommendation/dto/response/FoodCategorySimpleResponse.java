package com.dontgojunbao.bossoverhere.domain.recommendation.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@Schema(description = "음식 카테고리 간단 응답")
public class FoodCategorySimpleResponse {
    @Schema(description = "카테고리 ID", example = "0")
    private Long id;

    @Schema(description = "카테고리 이름", example = "분식")
    private String name;

    @Schema(description = "이 카테고리에 매핑된 클러스터 ID 리스트", example = "[0,2,3,5]")
    private List<Long> clusterIds;
}