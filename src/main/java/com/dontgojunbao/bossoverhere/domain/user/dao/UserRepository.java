package com.dontgojunbao.bossoverhere.domain.user.dao;

import com.dontgojunbao.bossoverhere.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByOauthId(String oauthId);
}
