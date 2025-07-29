package com.dontgojunbao.bossoverhere.domain.recommendation.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
public class RecommendationRequestDto {
    @Schema(description = "추천 요청 날짜 (yyyy-MM-dd)", example = "2025-07-29")
    @NotNull
    @FutureOrPresent
    private LocalDate date;
    @Schema(description = "시작 시간 (HH:mm)", example = "09:00")
    @NotNull
    private LocalTime startTime;
    @Schema(description = "종료 시간 (HH:mm)", example = "18:00")
    @NotNull
    private LocalTime endTime;
    @Schema(description = "음식 카테고리 ID", example = "3")
    @NotNull
    private Long foodCategoryId;
    @Schema(description = "출발 스팟 ID", example = "10")
    @NotNull
    private Long spotId;
    @Schema(description = "선택한 클러스터 ID 목록", example = "[1,2,5]")
    @NotEmpty
    private List<@NotNull Long> clusterIds;
}