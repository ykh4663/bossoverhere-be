package com.dontgojunbao.bossoverhere.domain.user.service;

import com.dontgojunbao.bossoverhere.domain.user.dao.UserRepository;
import com.dontgojunbao.bossoverhere.domain.user.domain.User;
import com.dontgojunbao.bossoverhere.global.error.ApplicationException;
import com.dontgojunbao.bossoverhere.global.error.UserErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);


    }
    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ApplicationException(UserErrorCode.NOTFOUND_USER));
    }

}