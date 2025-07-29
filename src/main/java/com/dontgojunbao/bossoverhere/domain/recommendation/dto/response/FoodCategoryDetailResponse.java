package com.dontgojunbao.bossoverhere.domain.recommendation.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import io.swagger.v3.oas.annotations.media.Schema;
@Data
@AllArgsConstructor
@Schema(description = "음식 카테고리 상세 응답")
public class FoodCategoryDetailResponse {
    @Schema(description = "카테고리 ID", example = "1")
    private Long id;

    @Schema(description = "카테고리 이름", example = "한식")
    private String name;

    @Schema(description = "카테고리 설명", example = "덮밥류, 제육, 불고기 등")
    private String description;

    @Schema(description = "이 카테고리에 매핑된 클러스터 ID 리스트", example = "[0,1,2,5]")
    private List<Long> clusterIds;
}