package com.dontgojunbao.bossoverhere.domain.post.dao;

import com.dontgojunbao.bossoverhere.domain.post.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PostRepository extends JpaRepository<Post,Long> {
    @EntityGraph(attributePaths = {"writer", "spot"})
    @Query("select p from Post p")
    Page<Post> findAllPosts(Pageable pageable);
}
