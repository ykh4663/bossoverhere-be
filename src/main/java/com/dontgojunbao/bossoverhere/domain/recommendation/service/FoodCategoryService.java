package com.dontgojunbao.bossoverhere.domain.recommendation.service;

import com.dontgojunbao.bossoverhere.domain.recommendation.dao.FoodCategoryClusterRepository;
import com.dontgojunbao.bossoverhere.domain.recommendation.dao.FoodCategoryRepository;
import com.dontgojunbao.bossoverhere.domain.recommendation.domain.FoodCategory;
import com.dontgojunbao.bossoverhere.domain.recommendation.domain.FoodCategoryCluster;
import com.dontgojunbao.bossoverhere.domain.recommendation.dto.response.ClusterDetailResponse;
import com.dontgojunbao.bossoverhere.domain.recommendation.dto.response.FoodCategoryDetailResponse;
import com.dontgojunbao.bossoverhere.domain.recommendation.dto.response.FoodCategoryDto;
import com.dontgojunbao.bossoverhere.domain.user.service.UserService;
import com.dontgojunbao.bossoverhere.global.error.ApplicationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.dontgojunbao.bossoverhere.global.error.FoodCategoryErrorCode.NOTFOUND_FOOD_CATEGORY;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FoodCategoryService {
    private final UserService userService;
    private final FoodCategoryRepository categoryRepo;
    private final FoodCategoryClusterRepository mappingRepo;

    public List<FoodCategoryDetailResponse> findAll(Long userId) {
        userService.getUserById(userId);
        // 2) 카테고리 전체 조회 후 DTO 매핑
        return categoryRepo.findAll().stream()
                .map(FoodCategoryDetailResponse::from)
                .collect(Collectors.toList());
    }

    public List<ClusterDetailResponse> findClustersByCategory(Long userId, Long categoryId) {
        userService.getUserById(userId);
        FoodCategory cat = categoryRepo.findById(categoryId)
                .orElseThrow(() -> new ApplicationException(NOTFOUND_FOOD_CATEGORY));
        // 3) 매핑 테이블에서 클러스터 꺼내서 DTO로 매핑
        return mappingRepo.findAllByFoodCategory_Id(categoryId).stream()
                .map(FoodCategoryCluster::getCluster)
                .map(ClusterDetailResponse::from)
                .collect(Collectors.toList());
    }
    public FoodCategory getFoodCategoryById(Long categoryId) {
        return categoryRepo.findById(categoryId)
                .orElseThrow(() -> new ApplicationException(NOTFOUND_FOOD_CATEGORY));
    }
}