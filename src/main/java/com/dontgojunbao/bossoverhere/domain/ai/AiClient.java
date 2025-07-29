package com.dontgojunbao.bossoverhere.domain.ai;

import com.dontgojunbao.bossoverhere.domain.ai.dto.AiPlanResponse;
import com.dontgojunbao.bossoverhere.domain.ai.dto.AiRecommendationPayload;
import com.dontgojunbao.bossoverhere.domain.recommendation.dto.request.RecommendationRequestDto;
import com.dontgojunbao.bossoverhere.global.error.ApplicationException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static com.dontgojunbao.bossoverhere.global.error.AiErrorCode.AI_CALL_FAILURE;

@Service
@RequiredArgsConstructor
public class AiClient {
    private final RestTemplate rt;

    @Value("${ai.server.url}")
    private String aiServerUrl;

    public List<AiPlanResponse> callAiForPlan(RecommendationRequestDto reqDto) {
        // 1) 변환
        AiRecommendationPayload payload = AiRecommendationPayload.from(reqDto);

        // 2) 헤더 세팅
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<AiRecommendationPayload> request = new HttpEntity<>(payload, headers);

        try {
            ResponseEntity<List<AiPlanResponse>> resp = rt.exchange(
                    aiServerUrl,
                    HttpMethod.POST,
                    request,
                    new ParameterizedTypeReference<List<AiPlanResponse>>() {}
            );
            return resp.getBody();
        } catch (RestClientException ex) {
            throw new ApplicationException(AI_CALL_FAILURE);
        }
    }


}