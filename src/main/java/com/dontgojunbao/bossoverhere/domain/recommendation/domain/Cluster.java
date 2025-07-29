package com.dontgojunbao.bossoverhere.domain.recommendation.domain;

import com.dontgojunbao.bossoverhere.global.common.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "cluster")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cluster extends BaseEntity {
    @Id
    @Column(name = "cluster_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    @NotBlank
    private String title;

    @Column(nullable = false, length = 50)
    @NotBlank
    private String nickname;

    @Column(nullable = false, columnDefinition = "TEXT")
    @NotBlank
    private String description;

    @Column(nullable = false, columnDefinition = "TEXT")
    @NotBlank
    private String situations;

}
