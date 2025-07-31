-- ① Cluster 테이블
CREATE TABLE IF NOT EXISTS cluster (
                                       cluster_id   BIGINT AUTO_INCREMENT PRIMARY KEY,
                                       title        VARCHAR(100) NOT NULL,
    nickname     VARCHAR(50)  NOT NULL,
    description  TEXT         NOT NULL,
    situations   TEXT         NOT NULL,
    created_at   TIMESTAMP    DEFAULT CURRENT_TIMESTAMP,
    updated_at   TIMESTAMP    DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
    );

-- ② FoodCategory 테이블
CREATE TABLE IF NOT EXISTS food_category (
                                             food_category_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                             name              VARCHAR(50) NOT NULL,
    description       TEXT,
    created_at        TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at        TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
    );

-- ③ FoodCategoryCluster (매핑) 테이블
CREATE TABLE IF NOT EXISTS food_category_cluster (
                                                     id                BIGINT AUTO_INCREMENT PRIMARY KEY,
                                                     cluster_id        BIGINT NOT NULL,
                                                     food_category_id  BIGINT NOT NULL,
                                                     created_at        TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                                     updated_at        TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                                     FOREIGN KEY (cluster_id)        REFERENCES cluster(cluster_id),
    FOREIGN KEY (food_category_id)  REFERENCES food_category(food_category_id)
    );

-- ④ Spot 테이블
CREATE TABLE IF NOT EXISTS spot (
                                    spot_id     BIGINT AUTO_INCREMENT PRIMARY KEY,
                                    name        VARCHAR(100) NOT NULL,
    address     VARCHAR(200) NOT NULL,
    latitude    DOUBLE        NOT NULL,
    longitude   DOUBLE        NOT NULL,
    created_at  TIMESTAMP     DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
    );

-- ⑤ Users 테이블
CREATE TABLE IF NOT EXISTS users (
                                     user_id    BIGINT AUTO_INCREMENT PRIMARY KEY,
                                     oauth_id   VARCHAR(255) NOT NULL,
    created_at TIMESTAMP     DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
    );

-- ⑥ RefreshToken 테이블
CREATE TABLE IF NOT EXISTS refresh_token (
                                             user_id BIGINT PRIMARY KEY,
                                             token   VARCHAR(512) NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(user_id)
    );

-- ⑦ RecommendationRequest 테이블
CREATE TABLE IF NOT EXISTS recommendation_request (
                                                      request_id       BIGINT AUTO_INCREMENT PRIMARY KEY,
                                                      user_id          BIGINT NOT NULL,
                                                      request_date     DATE     NOT NULL,
                                                      start_time       TIME     NOT NULL,
                                                      end_time         TIME     NOT NULL,
                                                      food_category_id BIGINT   NOT NULL,
                                                      spot_id          BIGINT   NOT NULL,
                                                      created_at       TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                                      updated_at       TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                                      FOREIGN KEY (user_id)           REFERENCES users(user_id),
    FOREIGN KEY (food_category_id)  REFERENCES food_category(food_category_id),
    FOREIGN KEY (spot_id)           REFERENCES spot(spot_id)
    );

-- ⑧ RecommendationRequestCluster 테이블
CREATE TABLE IF NOT EXISTS recommendation_request_cluster (
                                                              id         BIGINT AUTO_INCREMENT PRIMARY KEY,
                                                              request_id BIGINT NOT NULL,
                                                              cluster_id BIGINT NOT NULL,
                                                              FOREIGN KEY (request_id) REFERENCES recommendation_request(request_id),
    FOREIGN KEY (cluster_id) REFERENCES cluster(cluster_id)
    );

-- ⑨ RecommendationRequestFood 테이블 (누락된 매핑)
CREATE TABLE IF NOT EXISTS recommendation_request_food (
                                                           id               BIGINT AUTO_INCREMENT PRIMARY KEY,
                                                           request_id       BIGINT NOT NULL,
                                                           food_category_id BIGINT NOT NULL,
                                                           FOREIGN KEY (request_id)       REFERENCES recommendation_request(request_id),
    FOREIGN KEY (food_category_id) REFERENCES food_category(food_category_id)
    );

-- ⑩ RecommendationSegment 테이블
CREATE TABLE IF NOT EXISTS recommendation_segment (
                                                      segment_id  BIGINT AUTO_INCREMENT PRIMARY KEY,
                                                      time        VARCHAR(5) NOT NULL,  -- "HH:mm"
    from_spot_id BIGINT NOT NULL,
    to_spot_id   BIGINT NOT NULL,
    request_id   BIGINT NOT NULL,
    created_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (from_spot_id) REFERENCES spot(spot_id),
    FOREIGN KEY (to_spot_id)   REFERENCES spot(spot_id),
    FOREIGN KEY (request_id)   REFERENCES recommendation_request(request_id)
    );