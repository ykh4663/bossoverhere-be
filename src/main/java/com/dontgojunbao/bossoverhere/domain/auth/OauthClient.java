package com.dontgojunbao.bossoverhere.domain.auth;

public interface OauthClient {
    String getOAuthProviderUserId(String accessToken);
}