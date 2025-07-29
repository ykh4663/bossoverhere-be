package com.dontgojunbao.bossoverhere.domain.spot.dao;

import com.dontgojunbao.bossoverhere.domain.spot.domain.Spot;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpotRepository extends JpaRepository<Spot, Long> {

}
