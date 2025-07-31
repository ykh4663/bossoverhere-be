package com.dontgojunbao.bossoverhere.domain.post.dto;

import com.dontgojunbao.bossoverhere.domain.post.domain.Post;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Schema(description = "게시글 조회 응답")
public class PostInfoDto {

    @Schema(description = "게시글 ID", example = "1")
    private Long postId;

    @Schema(description = "작성자 회원 ID", example = "42")
    private Long writerId;

    @Schema(description = "스팟 ID", example = "7")
    private Long spotId;

    @Schema(description = "영업 시작 일시 (YYYY-MM-DDThh:mm:ss)", example = "2025-07-30T09:00:00")
    private LocalDateTime startAt;

    @Schema(description = "영업 종료 일시 (YYYY-MM-DDThh:mm:ss)", example = "2025-07-30T17:00:00")
    private LocalDateTime endAt;

    @Schema(description = "매출", example = "100000")
    private Long revenue;

    @Schema(description = "지출", example = "50000")
    private Long expense;

    @Schema(description = "이익 (매출 - 지출)", example = "50000")
    private Long profit;

    @Schema(description = "메모 내용", example = "하루 매출 기록")
    private String memo;

    @Schema(description = "업로드된 이미지 URL", example = "https://example-bucket.s3.amazonaws.com/image.png")
    private String filePath;

    @Schema(description = "생성 일시 (YYYY-MM-DDThh:mm:ss)", example = "2025-07-30T18:00:00")
    private LocalDateTime createdAt;

    @Schema(description = "수정 일시 (YYYY-MM-DDThh:mm:ss)", example = "2025-07-30T18:30:00")
    private LocalDateTime updatedAt;

    public PostInfoDto(Post post) {
        this.postId = post.getId();
        this.writerId = post.getWriter().getId();
        this.spotId = post.getSpot().getId();
        this.startAt = post.getStartAt();
        this.endAt = post.getEndAt();
        this.revenue = post.getRevenue();
        this.expense = post.getExpense();
        this.profit = post.getProfit();
        this.memo = post.getMemo();
        this.filePath = post.getImageUrl();
        this.createdAt = post.getCreatedAt();
        this.updatedAt = post.getUpdatedAt();
    }
}