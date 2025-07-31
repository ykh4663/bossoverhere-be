package com.dontgojunbao.bossoverhere.domain.post.service;

import com.dontgojunbao.bossoverhere.domain.post.dto.PostInfoDto;
import com.dontgojunbao.bossoverhere.domain.post.dto.PostSaveDto;
import com.dontgojunbao.bossoverhere.domain.post.dto.PostUpdateDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostService {
    /**
     * 게시글 저장
     */
    Long savePost(Long userId, PostSaveDto dto);

    /**
     * 단일 게시글 조회
     */
    PostInfoDto getPost(Long userId, Long postId);

    /**
     * 전체 게시글 조회
     */
    Page<PostInfoDto> getPosts(Long userId, Pageable pageable);

    /**
     * 게시글 수정 (필드 중 optional 값만 변경)
     */
    void updatePost(Long userId, Long postId, PostUpdateDto dto);

    /**
     * 게시글 삭제
     */
    void deletePost(Long userId, Long postId);
}