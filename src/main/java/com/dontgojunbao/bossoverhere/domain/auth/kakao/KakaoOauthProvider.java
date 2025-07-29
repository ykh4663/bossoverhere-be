package com.dontgojunbao.bossoverhere.domain.auth.kakao;

import com.dontgojunbao.bossoverhere.domain.auth.OauthClient;
import com.dontgojunbao.bossoverhere.domain.auth.OauthProvider;
import com.dontgojunbao.bossoverhere.domain.auth.domain.enums.OauthType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KakaoOauthProvider implements OauthProvider {
    private final KakaoOauthClient kakaoOauthClient;

    @Override
    public OauthType getOauthType() {
        return OauthType.KAKAO;
    }

    @Override
    public OauthClient getOAuthClient() {
        return kakaoOauthClient;
    }
}