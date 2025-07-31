package com.dontgojunbao.bossoverhere.domain.post.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Optional;

@Schema(description = "게시글 업데이트 요청 DTO")
public record PostUpdateDto(
        @Schema(description = "변경할 스팟 ID (Optional)", example = "7")
        Optional<Long> spotId,

        @Schema(description = "변경할 영업 시작 일시 (Optional, YYYY-MM-DDThh:mm:ss)", example = "2025-07-30T09:00:00")
        Optional<LocalDateTime> startAt,

        @Schema(description = "변경할 영업 종료 일시 (Optional, YYYY-MM-DDThh:mm:ss)", example = "2025-07-30T17:00:00")
        Optional<LocalDateTime> endAt,

        @Schema(description = "변경할 매출 (Optional)", example = "120000")
        Optional<Long> revenue,

        @Schema(description = "변경할 지출 (Optional)", example = "60000")
        Optional<Long> expense,

        @Schema(description = "변경할 메모 (Optional, 비속어는 자동 마스킹, 일부 금칙어는 등록 불가)", example = "변경된 메모 내용")
        Optional<String> memo,

        @Schema(type = "string", format = "binary", description = "변경할 첨부 이미지/파일 (Optional)")
        Optional<MultipartFile> uploadFile
) {}
