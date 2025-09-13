package com.dontgojunbao.bossoverhere.domain.post.controller;

import com.dontgojunbao.bossoverhere.domain.post.controller.docs.PostControllerDocs;
import com.dontgojunbao.bossoverhere.domain.post.dto.PostDto;
import com.dontgojunbao.bossoverhere.domain.post.dto.PostSimpleDto;
import com.dontgojunbao.bossoverhere.domain.post.dto.PostSaveDto;
import com.dontgojunbao.bossoverhere.domain.post.dto.PostUpdateDto;
import com.dontgojunbao.bossoverhere.domain.post.service.PostService;
import com.dontgojunbao.bossoverhere.global.common.dto.CommonResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Post", description = "게시글 API")
@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController implements PostControllerDocs {

    private final PostService postService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CommonResponse<Long>> createPost(
            @AuthenticationPrincipal Long userId,
            @Valid @ModelAttribute PostSaveDto dto
    ) {
        Long id = postService.savePost(userId, dto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(CommonResponse.createSuccess(id));
    }


    @GetMapping("/{postId}")
    public ResponseEntity<CommonResponse<PostDto>> getPost(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long postId
    ) {
        PostDto info = postService.getPost(userId, postId);
        return ResponseEntity.ok(CommonResponse.createSuccess(info));
    }


    @GetMapping
    public ResponseEntity<CommonResponse<Page<PostSimpleDto>>> getPosts(
            @AuthenticationPrincipal Long userId,
            Pageable pageable
    ) {
        Page<PostSimpleDto> page = postService.getPosts(userId, pageable);
        return ResponseEntity.ok(CommonResponse.createSuccess(page));
    }


    @PutMapping(value = "/{postId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CommonResponse<Void>> updatePost(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long postId,
            @Valid @ModelAttribute PostUpdateDto dto
    ) {
        postService.updatePost(userId, postId, dto);
        return ResponseEntity.ok(CommonResponse.createSuccess(null));
    }


    @DeleteMapping("/{postId}")
    public ResponseEntity<CommonResponse<Void>> deletePost(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long postId
    ) {
        postService.deletePost(userId, postId);
        return ResponseEntity.ok(CommonResponse.createSuccess(null));
    }

}