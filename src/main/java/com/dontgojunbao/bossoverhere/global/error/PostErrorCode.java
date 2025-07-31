package com.dontgojunbao.bossoverhere.global.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum PostErrorCode implements ErrorCode {
    NOT_FOUND_POST(HttpStatus.NOT_FOUND, "기록을 찾을 수 없습니다."),
    FORBIDDEN_POST(HttpStatus.FORBIDDEN, "해당 기록에 대한 변경 권한이 없습니다."),
    BAD_WORD_DETECTED(HttpStatus.BAD_REQUEST, "비속어는 문구에 포함할 수 없습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
