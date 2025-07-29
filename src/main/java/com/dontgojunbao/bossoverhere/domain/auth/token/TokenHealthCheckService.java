package com.dontgojunbao.bossoverhere.domain.auth.token;

import com.dontgojunbao.bossoverhere.domain.auth.jwt.JwtUtil;
import com.dontgojunbao.bossoverhere.global.error.ApplicationException;
import com.dontgojunbao.bossoverhere.global.error.SecurityErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenHealthCheckService {
    private final JwtUtil jwtUtil;

    public void healthCheck(String token) {
        try {
            jwtUtil.isExpired(token);
        } catch (Exception e) {
            throw new ApplicationException(SecurityErrorCode.EXPIRED_TOKEN);
        }
    }
}
