package com.dontgojunbao.bossoverhere.domain.spot.controller;

import com.dontgojunbao.bossoverhere.domain.spot.dto.SpotDto;
import com.dontgojunbao.bossoverhere.domain.spot.service.SpotService;
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

@RestController
@RequestMapping("/api/spots")
@RequiredArgsConstructor
@Tag(name = "Spot", description = "스팟 API")
public class SpotController {
    private final SpotService spotService;

    @Operation(summary = "전체 스팟 조회")
    @GetMapping
    public ResponseEntity<CommonResponse<List<SpotDto>>> getAllSpots(@AuthenticationPrincipal Long userId) {
        return ResponseEntity.ok(CommonResponse.createSuccess(spotService.findAll(userId)));
    }

    @Operation(summary = "스팟 단건 조회")
    @GetMapping("/{spotId}")
    public ResponseEntity<CommonResponse<SpotDto>> getSpot(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long spotId) {
        return ResponseEntity.ok(CommonResponse.createSuccess(spotService.findById(userId, spotId)));
    }
}