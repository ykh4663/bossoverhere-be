CREATE TABLE IF NOT EXISTS cluster (
                                       cluster_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                       title VARCHAR(255) NOT NULL,
    nickname VARCHAR(255),
    description TEXT,
    situations TEXT
    );

CREATE TABLE IF NOT EXISTS food_category (
                                             food_category_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                             name VARCHAR(100) NOT NULL,
    description TEXT
    );

CREATE TABLE IF NOT EXISTS food_category_cluster (
                                                     recommend_value_pick_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                                     cluster_id BIGINT NOT NULL,
                                                     food_category_id BIGINT NOT NULL,
                                                     FOREIGN KEY (cluster_id) REFERENCES cluster(cluster_id),
    FOREIGN KEY (food_category_id) REFERENCES food_category(food_category_id)
    );

CREATE TABLE IF NOT EXISTS spot (
                                    spot_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                    name VARCHAR(255) NOT NULL,
    address VARCHAR(500) NOT NULL,
    latitude DOUBLE NOT NULL,
    longitude DOUBLE NOT NULL
    );

CREATE TABLE IF NOT EXISTS recommendation_request (
                                                      request_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                                      request_date DATE NOT NULL,
                                                      start_time TIME NOT NULL,
                                                      end_time TIME NOT NULL,
                                                      food_category_id BIGINT NOT NULL,
                                                      spot_id BIGINT NOT NULL,
                                                      FOREIGN KEY (food_category_id) REFERENCES food_category(food_category_id),
    FOREIGN KEY (spot_id) REFERENCES spot(spot_id)
    );

CREATE TABLE IF NOT EXISTS recommendation_request_cluster (
                                                              id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                                              request_id BIGINT NOT NULL,
                                                              cluster_id BIGINT NOT NULL,
                                                              FOREIGN KEY (request_id) REFERENCES recommendation_request(request_id),
    FOREIGN KEY (cluster_id) REFERENCES cluster(cluster_id)
    );


CREATE TABLE IF NOT EXISTS recommendation_request_food (
                                                           id                        BIGINT AUTO_INCREMENT PRIMARY KEY,
                                                           request_id                BIGINT NOT NULL,
                                                           food_category_id          BIGINT NOT NULL,
                                                           FOREIGN KEY (request_id)
    REFERENCES recommendation_request(request_id),
    FOREIGN KEY (food_category_id)
    REFERENCES food_category(food_category_id)
    );

-- ② 인증(User) 테이블
CREATE TABLE IF NOT EXISTS users (
                                     user_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                     oauth_id VARCHAR(255) NOT NULL
    );

-- ③ Refresh Token 테이블
CREATE TABLE IF NOT EXISTS refresh_token (
                                             user_id BIGINT PRIMARY KEY,
                                             token VARCHAR(512) NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(user_id)
    );
