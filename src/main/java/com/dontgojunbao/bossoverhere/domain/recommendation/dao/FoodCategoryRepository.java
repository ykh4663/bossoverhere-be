package com.dontgojunbao.bossoverhere.domain.recommendation.dao;

import com.dontgojunbao.bossoverhere.domain.recommendation.domain.FoodCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FoodCategoryRepository extends JpaRepository<FoodCategory, Long> {


}
