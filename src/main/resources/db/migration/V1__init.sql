-- Cluster 테이블
CREATE TABLE IF NOT EXISTS cluster (
                                       cluster_id   BIGINT         AUTO_INCREMENT PRIMARY KEY,
                                       title        VARCHAR(100)   NOT NULL,
    nickname     VARCHAR(50)    NOT NULL,
    description  TEXT           NOT NULL,
    situations   TEXT           NOT NULL,
    created_at   TIMESTAMP      DEFAULT CURRENT_TIMESTAMP,
    updated_at   TIMESTAMP      DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
    );

-- FoodCategory 테이블
CREATE TABLE IF NOT EXISTS food_category (
                                             food_category_id BIGINT         AUTO_INCREMENT PRIMARY KEY,
                                             name              VARCHAR(50)   NOT NULL,
    description       TEXT           NULL,
    created_at        TIMESTAMP      DEFAULT CURRENT_TIMESTAMP,
    updated_at        TIMESTAMP      DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
    );

-- FoodCategoryCluster 매핑 테이블
CREATE TABLE IF NOT EXISTS food_category_cluster (
                                                     recommend_value_pick_id BIGINT       AUTO_INCREMENT PRIMARY KEY,
                                                     cluster_id              BIGINT       NOT NULL,
                                                     food_category_id        BIGINT       NOT NULL,
                                                     created_at              TIMESTAMP    DEFAULT CURRENT_TIMESTAMP,
                                                     updated_at              TIMESTAMP    DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                                     CONSTRAINT fk_fcc_cluster
                                                     FOREIGN KEY (cluster_id)
    REFERENCES cluster(cluster_id)
    ON DELETE CASCADE,
    CONSTRAINT fk_fcc_food_category
    FOREIGN KEY (food_category_id)
    REFERENCES food_category(food_category_id)
    ON DELETE CASCADE
    );

-- Spot 테이블
CREATE TABLE IF NOT EXISTS spot (
                                    spot_id     BIGINT      AUTO_INCREMENT PRIMARY KEY,
                                    name        VARCHAR(100) NOT NULL,
    address     VARCHAR(200) NOT NULL,
    latitude    DOUBLE       NOT NULL,
    longitude   DOUBLE       NOT NULL,
    created_at  TIMESTAMP    DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP    DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
    );
-- USERS 테이블

CREATE TABLE IF NOT EXISTS USERS (
                                     user_id     BIGINT      AUTO_INCREMENT PRIMARY KEY,
                                     OAUTH_ID    VARCHAR(255) NOT NULL,
    created_at  TIMESTAMP    DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP    DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
    );
-- RefreshToken 테이블
CREATE TABLE IF NOT EXISTS refresh_token (
                                             user_id BIGINT PRIMARY KEY,
                                             token   VARCHAR(512) NOT NULL,
    FOREIGN KEY (user_id)
    REFERENCES USERS(user_id)
    ON DELETE CASCADE
    );

-- RecommendationRequest 테이블
CREATE TABLE IF NOT EXISTS recommendation_request (
                                                      request_id       BIGINT         AUTO_INCREMENT PRIMARY KEY,
                                                      user_id          BIGINT         NOT NULL,
                                                      request_date     DATE           NOT NULL,
                                                      start_time       TIME           NOT NULL,
                                                      end_time         TIME           NOT NULL,
                                                      food_category_id BIGINT         NOT NULL,
                                                      spot_id          BIGINT         NOT NULL,
                                                      created_at       TIMESTAMP      DEFAULT CURRENT_TIMESTAMP,
                                                      updated_at       TIMESTAMP      DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

                                                      CONSTRAINT fk_rr_user
                                                      FOREIGN KEY (user_id)
    REFERENCES USERS(user_id)
    ON DELETE CASCADE,

    CONSTRAINT fk_rr_food_category
    FOREIGN KEY (food_category_id)
    REFERENCES food_category(food_category_id)
    ON DELETE CASCADE,

    CONSTRAINT fk_rr_spot
    FOREIGN KEY (spot_id)
    REFERENCES spot(spot_id)
    ON DELETE CASCADE
    );

-- RecommendationRequestCluster 테이블
CREATE TABLE IF NOT EXISTS recommendation_request_cluster (
                                                              id             BIGINT      AUTO_INCREMENT PRIMARY KEY,
                                                              request_id     BIGINT      NOT NULL,
                                                              cluster_id     BIGINT      NOT NULL,
                                                              created_at     TIMESTAMP   DEFAULT CURRENT_TIMESTAMP,
                                                              updated_at     TIMESTAMP   DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

                                                              CONSTRAINT fk_rrc_request
                                                              FOREIGN KEY (request_id)
    REFERENCES recommendation_request(request_id)
    ON DELETE CASCADE,

    CONSTRAINT fk_rrc_cluster
    FOREIGN KEY (cluster_id)
    REFERENCES cluster(cluster_id)
    ON DELETE CASCADE
    );
-- RecommendationSegment 테이블
CREATE TABLE IF NOT EXISTS recommendation_segment (
                                                      segment_id    BIGINT       AUTO_INCREMENT PRIMARY KEY,
                                                      time          VARCHAR(5)   NOT NULL,  -- "HH:mm" 포맷
    from_spot_id  BIGINT       NOT NULL,
    to_spot_id    BIGINT       NOT NULL,
    request_id    BIGINT       NOT NULL,
    created_at    TIMESTAMP    DEFAULT CURRENT_TIMESTAMP,
    updated_at    TIMESTAMP    DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT fk_rs_from_spot
    FOREIGN KEY (from_spot_id)
    REFERENCES spot(spot_id)
                                                         ON DELETE CASCADE,

    CONSTRAINT fk_rs_to_spot
    FOREIGN KEY (to_spot_id)
    REFERENCES spot(spot_id)
                                                         ON DELETE CASCADE,

    CONSTRAINT fk_rs_request
    FOREIGN KEY (request_id)
    REFERENCES recommendation_request(request_id)
                                                         ON DELETE CASCADE
    );


CREATE TABLE IF NOT EXISTS post (
                                    post_id     BIGINT         AUTO_INCREMENT PRIMARY KEY,
                                    user_id     BIGINT         NOT NULL,
                                    spot_id     BIGINT         NOT NULL,
                                    start_at    DATETIME       NOT NULL,
                                    end_at      DATETIME       NULL,
                                    revenue     BIGINT         NOT NULL,
                                    expense     BIGINT         NOT NULL,
                                    profit      BIGINT         NULL,
                                    memo        TEXT           NULL,      -- ← CLOB 에서 TEXT 로 변경
                                    image_url   VARCHAR(255)   NULL,
    created_at  TIMESTAMP      DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP      DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT fk_post_user FOREIGN KEY (user_id) REFERENCES USERS(user_id) ON DELETE CASCADE,
    CONSTRAINT fk_post_spot FOREIGN KEY (spot_id) REFERENCES spot(spot_id) ON DELETE CASCADE,

    INDEX idx_post_user (user_id),
    INDEX idx_post_spot (spot_id)
    )
    ENGINE=InnoDB
    DEFAULT CHARSET=utf8mb4;