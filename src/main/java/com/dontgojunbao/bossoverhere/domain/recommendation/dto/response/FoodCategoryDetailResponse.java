package com.dontgojunbao.bossoverhere.domain.recommendation.dto.response;

import com.dontgojunbao.bossoverhere.domain.recommendation.domain.FoodCategory;
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
    public static FoodCategoryDetailResponse from(FoodCategory cat) {
        return new FoodCategoryDetailResponse(
                cat.getId(),
                cat.getName(),
                cat.getDescription()
        );
    }

}