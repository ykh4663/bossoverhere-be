package com.dontgojunbao.bossoverhere.domain.spot.domain;

import com.dontgojunbao.bossoverhere.global.common.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "spot")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Spot extends BaseEntity {
    @Id
    @Column(name = "spot_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    @NotBlank
    private String name;

    @Column(nullable = false, length = 200)
    @NotBlank
    private String address;

    @Column(nullable = false)
    @NotNull
    private double latitude;

    @Column(nullable = false)
    @NotNull
    private double longitude;
}

