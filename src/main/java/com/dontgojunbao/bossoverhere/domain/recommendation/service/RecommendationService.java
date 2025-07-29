package com.dontgojunbao.bossoverhere.domain.recommendation.service;


import com.dontgojunbao.bossoverhere.domain.ai.AiClient;
import com.dontgojunbao.bossoverhere.domain.ai.dto.AiPlanResponse;

import com.dontgojunbao.bossoverhere.domain.recommendation.dto.request.RecommendationRequestDto;
import com.dontgojunbao.bossoverhere.domain.recommendation.dto.response.RecommendationSegmentDto;
import com.dontgojunbao.bossoverhere.domain.spot.dao.SpotRepository;

import com.dontgojunbao.bossoverhere.domain.spot.dto.SpotDto;


import com.dontgojunbao.bossoverhere.domain.user.service.UserService;
import com.dontgojunbao.bossoverhere.global.error.ApplicationException;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.stream.Collectors;

import static com.dontgojunbao.bossoverhere.global.error.SpotErrorCode.NOTFOUND_SPOT;

@RequiredArgsConstructor
@Service
public class RecommendationService {

    private final UserService userService;
    private final AiClient aiClient;
    private final SpotRepository spotRepository;


    public List<RecommendationSegmentDto> recommend(Long userId, RecommendationRequestDto reqDto) {
        userService.getUserById(userId);
        List<AiPlanResponse> aiPlans = aiClient.callAiForPlan(reqDto);

        return aiPlans.stream()
                .map(plan -> {
                    SpotDto from = spotRepository.findById(plan.getFromSpotId())
                            .map(SpotDto::from)
                            .orElseThrow(() -> new ApplicationException(NOTFOUND_SPOT));

                    SpotDto to = spotRepository.findById(plan.getToSpotId())
                            .map(SpotDto::from)
                            .orElseThrow(() -> new ApplicationException(NOTFOUND_SPOT));

                    return new RecommendationSegmentDto(plan.getTime(), from, to);
                })
                .collect(Collectors.toList());
    }
}