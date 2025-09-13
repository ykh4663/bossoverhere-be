package com.dontgojunbao.bossoverhere.domain.post.service;

import com.dontgojunbao.bossoverhere.domain.post.dao.PostRepository;
import com.dontgojunbao.bossoverhere.domain.post.domain.Post;
import com.dontgojunbao.bossoverhere.domain.post.dto.PostDto;
import com.dontgojunbao.bossoverhere.domain.spot.dao.SpotRepository;

import com.dontgojunbao.bossoverhere.domain.spot.domain.Spot;
import com.dontgojunbao.bossoverhere.domain.user.dao.UserRepository;
import com.dontgojunbao.bossoverhere.domain.user.domain.User;
import jakarta.persistence.EntityManagerFactory;
import net.datafaker.Faker;
import org.hibernate.SessionFactory;
import org.hibernate.stat.Statistics;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(properties = {
        // 쿼리 카운트/시간 측정을 위해 통계 활성화
        "spring.jpa.properties.hibernate.generate_statistics=true"
})
@ActiveProfiles("dev")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PostEntityGraphPerfTest {

    @Autowired PostRepository postRepository;
    @Autowired UserRepository userRepository;
    @Autowired SpotRepository spotRepository;
    @Autowired EntityManagerFactory emf;

    @BeforeAll
    @Transactional
    void seed500() {



        // 2) User 200명 채우기
        long userCount = userRepository.count();
        List<User> users = new ArrayList<>();
        if (userCount < 200) {
            for (int i = 0; i < 200 - userCount; i++) {
                users.add(userRepository.save(
                        User.builder()
                                .oauthId("oauth-" + (userCount + i))
                                .build()
                ));
            }
        }
        // 시드를 위해 전체 유저 다시 로드
        List<User> allUsers = userRepository.findAll();
        List<Spot> allSpots = spotRepository.findAll();

        // 3) Post 500개 채우기
        if (postRepository.count() < 500) {
            Faker faker = new Faker();
            List<Post> bulk = new ArrayList<>(500);
            for (int i = 0; i < 500; i++) {
                User writer = allUsers.get(ThreadLocalRandom.current().nextInt(allUsers.size()));
                Spot spot   = allSpots.get(ThreadLocalRandom.current().nextInt(allSpots.size()));

                // 시간/금액 랜덤
                LocalDateTime start = LocalDateTime.now()
                        .minusDays(ThreadLocalRandom.current().nextInt(0, 30))
                        .withHour(ThreadLocalRandom.current().nextInt(8, 12))
                        .withMinute(0).withSecond(0).withNano(0);
                LocalDateTime end = start.plusHours(ThreadLocalRandom.current().nextInt(4, 10));

                long revenue = ThreadLocalRandom.current().nextLong(50_000, 500_000);
                long expense = ThreadLocalRandom.current().nextLong(10_000, Math.max(10_000, (long) (revenue * 0.8)));

                Post p = Post.builder()
                        .writer(writer)
                        .spot(spot)
                        .startAt(start)
                        .endAt(end)
                        .revenue(revenue)
                        .expense(expense)
                        .memo(faker.lorem().sentence())
                        .imageUrl(null)
                        .build();

                p.calculateProfit(); // profit 세팅
                bulk.add(p);
            }
            postRepository.saveAll(bulk);
        }
    }



    @Test
    @Transactional(readOnly = true)
    void compareNaiveVsEntityGraph_on500() {
        SessionFactory sf = emf.unwrap(SessionFactory.class);
        Statistics stats = sf.getStatistics();

        // === NAIVE (N+1 유발) ===
        stats.clear();
        long t1Start = System.nanoTime();
        var naive = postRepository.findAllPosts(PageRequest.of(0, 500))
                .map(PostDto::new);
        long t1End = System.nanoTime();
        long naiveQueries = stats.getPrepareStatementCount();

        // === ENTITY GRAPH (연관 로딩 최적화) ===
        stats.clear();
        long t2Start = System.nanoTime();
        var graph = postRepository.findAllPostsWithWriterAndSpot(PageRequest.of(0, 500))
                .map(PostDto::new);
        long t2End = System.nanoTime();
        long graphQueries = stats.getPrepareStatementCount();

        long naiveMs = (t1End - t1Start) / 1_000_000;
        long graphMs = (t2End - t2Start) / 1_000_000;

        System.out.println("==== RESULT (500 rows) ====");
        System.out.println("NAIVE        : " + naiveQueries + " queries, " + naiveMs + " ms");
        System.out.println("ENTITY-GRAPH : " + graphQueries + " queries, " + graphMs + " ms");

        assertThat(naive.getContent()).hasSize(500);
        assertThat(graph.getContent()).hasSize(500);
        assertThat(naiveQueries).isGreaterThan(graphQueries);
    }
}