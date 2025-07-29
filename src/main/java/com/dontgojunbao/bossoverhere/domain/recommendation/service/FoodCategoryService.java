package com.dontgojunbao.bossoverhere.domain.recommendation.service;

import com.dontgojunbao.bossoverhere.domain.recommendation.dao.FoodCategoryClusterRepository;
import com.dontgojunbao.bossoverhere.domain.recommendation.dao.FoodCategoryRepository;
import com.dontgojunbao.bossoverhere.domain.recommendation.domain.FoodCategory;
import com.dontgojunbao.bossoverhere.domain.recommendation.dto.response.FoodCategoryDetailResponse;
import com.dontgojunbao.bossoverhere.domain.recommendation.dto.response.FoodCategorySimpleResponse;
import com.dontgojunbao.bossoverhere.domain.user.service.UserService;
import com.dontgojunbao.bossoverhere.global.error.ApplicationException;
import com.dontgojunbao.bossoverhere.global.error.FoodCategoryErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.dontgojunbao.bossoverhere.global.error.FoodCategoryErrorCode.NOTFOUND_FOOD_CATEGORY;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FoodCategoryService {
    private final UserService userService;
    private final FoodCategoryRepository categoryRepo;
    private final FoodCategoryClusterRepository mappingRepo;

    public List<FoodCategorySimpleResponse> findAll(Long userId) {
        userService.getUserById(userId);
        List<FoodCategory> cats = categoryRepo.findAll();
        return cats.stream().map(cat -> {
            List<Long> clusterIds = mappingRepo.findClusterIdsByCategoryId(cat.getId());
            return new FoodCategorySimpleResponse(cat.getId(), cat.getName(), clusterIds);
        }).toList();
    }

    public FoodCategoryDetailResponse findById(Long userId, Long categoryId) {
        userService.getUserById(userId);
        FoodCategory cat = categoryRepo.findById(categoryId)
                .orElseThrow(() -> new ApplicationException(NOTFOUND_FOOD_CATEGORY));
        List<Long> clusterIds = mappingRepo.findClusterIdsByCategoryId(categoryId);
        return new FoodCategoryDetailResponse(
                cat.getId(), cat.getName(), cat.getDescription(), clusterIds
        );
    }
}