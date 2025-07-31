package com.dontgojunbao.bossoverhere.domain.recommendation.domain;

import com.dontgojunbao.bossoverhere.global.common.BaseEntity;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "food_category")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FoodCategory extends BaseEntity {
    @Id
    @Column(name = "food_category_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    @NotBlank
    private String name;

    @Column(nullable = true, columnDefinition = "TEXT")
    private String description;

}
