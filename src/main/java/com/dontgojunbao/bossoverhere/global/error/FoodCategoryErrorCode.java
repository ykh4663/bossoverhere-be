package com.dontgojunbao.bossoverhere.global.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum FoodCategoryErrorCode implements ErrorCode {
    NOTFOUND_FOOD_CATEGORY(HttpStatus.NOT_FOUND, "해당 음식을 찾을 수 없습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
