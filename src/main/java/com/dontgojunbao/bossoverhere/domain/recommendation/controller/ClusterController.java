package com.dontgojunbao.bossoverhere.domain.recommendation.controller;

import com.dontgojunbao.bossoverhere.domain.recommendation.dto.response.ClusterDetailResponse;
import com.dontgojunbao.bossoverhere.domain.recommendation.dto.response.ClusterDto;
import com.dontgojunbao.bossoverhere.domain.recommendation.service.ClusterService;
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

@Tag(name = "cluster", description = "cluster API")
@RestController
@RequestMapping("/api/clusters")
@RequiredArgsConstructor
public class ClusterController {
    private final ClusterService clusterService;

    @Operation(summary = "전체 클러스터 조회", description = "클러스터 리스트 조회")
    @GetMapping
    public ResponseEntity<CommonResponse<List<ClusterDto>>> getClusters(
            @AuthenticationPrincipal Long userId
    ) {
        List<ClusterDto> clusters = clusterService.findAll(userId);
        return ResponseEntity.ok(CommonResponse.createSuccess(clusters));
    }

    @Operation(summary = "클러스터 상세 조회", description = "ID로 단건 조회")
    @GetMapping("/{clusterId}")
    public ResponseEntity<CommonResponse<ClusterDetailResponse>> getCluster(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long clusterId
    ) {
        return ResponseEntity.ok(
                CommonResponse.createSuccess(clusterService.findById(userId, clusterId))
        );
    }


}
