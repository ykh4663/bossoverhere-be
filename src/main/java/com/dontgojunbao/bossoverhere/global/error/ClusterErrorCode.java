package com.dontgojunbao.bossoverhere.global.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ClusterErrorCode implements ErrorCode {
    NOTFOUND_CLUSTER(HttpStatus.NOT_FOUND, "해당 클러스터를 찾을 수 없습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
