package com.dontgojunbao.bossoverhere.domain.post.dto;

import com.dontgojunbao.bossoverhere.domain.post.domain.Post;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Optional;

@Schema(description = "게시글 작성 요청 DTO")
public record PostSaveDto(
        @Schema(description = "영업 시작 일시 (YYYY-MM-DDThh:mm:ss)", example = "2025-07-30T09:00:00")
        LocalDateTime startAt,

        @Schema(description = "영업 종료 일시 (YYYY-MM-DDThh:mm:ss)", example = "2025-07-30T17:00:00")
        LocalDateTime endAt,

        @Schema(description = "스팟 ID", example = "7")
        Long spotId,

        @Schema(description = "매출", example = "100000")
        Long revenue,

        @Schema(description = "지출", example = "50000")
        Long expense,

        @Schema(description = "메모 (비속어는 자동 마스킹, 일부 금칙어는 등록 불가)", example = "하루 매출 기록")
        String memo,

        @Schema(type = "string", format = "binary", description = "첨부 이미지/파일 (Optional)")
        Optional<MultipartFile> uploadFile
) {

    /**
     * Service 레이어에서 user, spot, imageUrl 을 채워서 사용합니다.
     */
    public Post toEntity() {
        return Post.builder()
                .startAt(startAt)
                .endAt(endAt)
                .revenue(revenue)
                .expense(expense)
                .memo(memo)
                .build();
    }
}