package com.dontgojunbao.bossoverhere.global.error;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.FieldError;

import java.util.List;

@Getter
@Builder
@RequiredArgsConstructor
@Schema(name = "ErrorResponse", description = "표준 에러 응답")
public class ErrorResponse {

    @Schema(description = "에러 코드(ENUM 이름)", example = "INVALID_PARAMETER")
    private final String code;
    @Schema(description = "메시지", example = "유효하지 않은 요청 파라미터가 포함되어 있습니다.")
    private final String message;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @Schema(description = "필드 검증 실패 목록", nullable = true)
    private final List<ValidationError> errors;

    @Getter
    @Builder
    @RequiredArgsConstructor
    public static class ValidationError {
        @Schema(description = "필드명", example = "startAt")
        private final String field;
        @Schema(description = "실패 사유", example = "빈 값은 올 수 없습니다.")
        private final String message;

        public static ValidationError of(final FieldError fieldError) {
            return ValidationError.builder()
                    .field(fieldError.getField())
                    .message(fieldError.getDefaultMessage())
                    .build();
        }
    }
}