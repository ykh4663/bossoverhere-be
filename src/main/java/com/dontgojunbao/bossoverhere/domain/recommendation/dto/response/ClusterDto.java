package com.dontgojunbao.bossoverhere.domain.recommendation.dto.response;

import com.dontgojunbao.bossoverhere.domain.recommendation.domain.Cluster;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "클러스터 조회 응답")
public class ClusterDto {
    @Schema(description = "클러스터 ID", example = "0")
    private Long id;

    @Schema(description = "클러스터 제목", example = "동네 일상과 젊음의 중심")
    private String title;
    public static ClusterDto from(Cluster c) {
        return new ClusterDto(
                c.getId(),
                c.getTitle()

        );
    }

}