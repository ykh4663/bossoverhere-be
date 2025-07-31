package com.dontgojunbao.bossoverhere.domain.recommendation.dto.response;

import com.dontgojunbao.bossoverhere.domain.recommendation.domain.FoodCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "음식 카테고리 조회 응답")
public class FoodCategoryDto {
    @Schema(description = "카테고리 ID", example = "0")
    private Long id;

    @Schema(description = "카테고리 이름", example = "분식")
    private String name;


    public static FoodCategoryDto from(FoodCategory cat) {
        return new FoodCategoryDto(
                cat.getId(),
                cat.getName()
        );
    }
}