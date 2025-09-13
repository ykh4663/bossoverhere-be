package com.dontgojunbao.bossoverhere.domain.spot.dto;

import com.dontgojunbao.bossoverhere.domain.spot.domain.Spot;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SpotDto {
    private Long spotId;
    private String spotName;
    private String spotAddress;
    private double spotLatitude;
    private double spotLongitude;
    public static SpotDto from(Spot s) {
        return new SpotDto(s.getId(), s.getName(), s.getAddress(), s.getLatitude(), s.getLongitude());
    }
}
