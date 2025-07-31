package com.dontgojunbao.bossoverhere.domain.post.domain;

import com.dontgojunbao.bossoverhere.domain.spot.domain.Spot;
import com.dontgojunbao.bossoverhere.domain.user.domain.User;
import com.dontgojunbao.bossoverhere.global.common.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;


import java.time.LocalDateTime;


@Entity
@Table(name = "post")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Post extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User writer;

    @Column(name = "start_at", nullable = false)
    private LocalDateTime startAt;

    /** 장사 종료 시점 (선택) */
    @Column(name = "end_at")
    private LocalDateTime endAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "spot_id", nullable = false)
    private Spot spot;


    @NotNull(message = "매출은 필수값입니다.")
    @Column(name = "revenue")
    private Long revenue;

    @NotNull(message = "지출은 필수값입니다.")
    @Column(name = "expense")
    private Long expense;

    @Column(name = "profit")
    private Long profit;

    @Lob
    @Column(length = 300)
    private String memo;

    @Column(nullable = true)
    private String imageUrl;

    public void confirmWriter(User writer) {
        //writer는 변경이 불가능하므로 이렇게만 해주어도 될듯
        this.writer = writer;
        writer.addPost(this);
    }

    public void calculateProfit() {
        if (revenue == null || expense == null) {
            // 비정상적인 상태이므로 IllegalStateException 으로 대체
            throw new IllegalStateException("매출 또는 지출 정보가 없어서 손익을 계산할 수 없습니다.");
        }
        this.profit = revenue - expense;
    }

    public void updateSpot(Spot spot) {
        this.spot = spot;
    }

    public void updateStartAt(LocalDateTime startAt) {
        this.startAt = startAt;
    }
    public void updateEndAt(LocalDateTime endAt) {
        this.endAt = endAt;
    }
    public void updateRevenue(Long revenue) {
        this.revenue = revenue;
    }

    public void updateExpense(Long expense) {
        this.expense = expense;
    }

    public void updateMemo(String memo) {
        this.memo = memo;
    }

    public void updateImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

}
