package com.dontgojunbao.bossoverhere.domain.recommendation.service;

import com.dontgojunbao.bossoverhere.domain.recommendation.dao.RecommendationRequestRepository;
import com.dontgojunbao.bossoverhere.domain.recommendation.domain.RecommendationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class RecommendationSaveService {
    private final RecommendationRequestRepository requestRepo;

    public void save(RecommendationRequest request) {
        requestRepo.save(request);
    }
}
