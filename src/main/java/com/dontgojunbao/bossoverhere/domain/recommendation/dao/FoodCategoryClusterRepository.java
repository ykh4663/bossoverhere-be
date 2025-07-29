package com.dontgojunbao.bossoverhere.domain.recommendation.dao;


import com.dontgojunbao.bossoverhere.domain.recommendation.domain.FoodCategoryCluster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FoodCategoryClusterRepository extends JpaRepository<FoodCategoryCluster, Long> {
    @Query("select fcc.cluster.id from FoodCategoryCluster fcc where fcc.foodCategory.id = :categoryId")
    List<Long> findClusterIdsByCategoryId(@Param("categoryId") Long categoryId);
}
