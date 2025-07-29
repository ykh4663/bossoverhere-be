package com.dontgojunbao.bossoverhere.domain.auth.dao;

import com.dontgojunbao.bossoverhere.domain.auth.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByUserId(Long userId);
}
