package com.dontgojunbao.bossoverhere.global.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum RecommendErrorCode implements ErrorCode {
    NOT_FOUND_HISTORY(HttpStatus.NOT_FOUND,     "추천 이력을 찾을 수 없습니다."),
    FORBIDDEN_HISTORY(HttpStatus.FORBIDDEN,     "본인 것이 아닌 추천 이력입니다.");


    private final HttpStatus httpStatus;
    private final String message;
}
