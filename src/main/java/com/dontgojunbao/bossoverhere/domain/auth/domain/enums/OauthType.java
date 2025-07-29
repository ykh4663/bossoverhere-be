package com.dontgojunbao.bossoverhere.domain.auth.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OauthType {

    KAKAO("kakao");


    private final String typeName;
}