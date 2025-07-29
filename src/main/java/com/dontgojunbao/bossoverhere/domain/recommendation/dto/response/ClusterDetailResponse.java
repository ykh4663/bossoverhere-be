package com.dontgojunbao.bossoverhere.domain.recommendation.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ClusterDetailResponse {
    @Schema(description = "클러스터 ID", example = "0")
    private Long id;
    @Schema(description = "클러스터 제목", example = "동네 일상과 젊음의 중심")
    private String title;
    @Schema(description = "클러스터 별칭", example = "활력 가득한 생활권 거점")
    private String nickname;
    @Schema(description = "클러스터 설명")
    private String description;
    @Schema(description = "어울리는 방문 상황", example = "출퇴근 후 산책, 운동, 친구와의 약속")
    private String situations;
}