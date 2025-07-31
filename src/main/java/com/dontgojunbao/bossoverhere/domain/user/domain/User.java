package com.dontgojunbao.bossoverhere.domain.user.domain;

import com.dontgojunbao.bossoverhere.domain.post.domain.Post;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.CascadeType.ALL;

@Table(name = "USERS")
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "OAUTH_ID", nullable = false)
    private String oauthId;

    @Builder.Default
    @OneToMany(mappedBy = "writer", cascade = ALL, orphanRemoval = true)
    private List<Post> postList = new ArrayList<>();

    public void addPost(Post post){
        postList.add(post);
    }
}