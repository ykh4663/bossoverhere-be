package com.dontgojunbao.bossoverhere.domain.post.controller;

import com.dontgojunbao.bossoverhere.domain.post.dto.PostInfoDto;
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
public class PostController {

    private final PostService postService;

    @Operation(summary = "게시글 생성", description = "새로운 게시글을 작성합니다.")
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

    @Operation(summary = "단일 게시글 조회", description = "postId로 게시글 한 건을 조회합니다.")
    @GetMapping("/{postId}")
    public ResponseEntity<CommonResponse<PostInfoDto>> getPost(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long postId
    ) {
        PostInfoDto info = postService.getPost(userId, postId);
        return ResponseEntity.ok(CommonResponse.createSuccess(info));
    }

    @Operation(summary = "전체 게시글 조회", description = "페이징된 게시글 목록을 조회합니다.")
    @GetMapping
    public ResponseEntity<CommonResponse<Page<PostInfoDto>>> getPosts(
            @AuthenticationPrincipal Long userId,
            Pageable pageable
    ) {
        Page<PostInfoDto> page = postService.getPosts(userId, pageable);
        return ResponseEntity.ok(CommonResponse.createSuccess(page));
    }

    @Operation(summary = "게시글 수정", description = "postId에 해당하는 게시글을 수정합니다.")
    @PutMapping(value = "/{postId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CommonResponse<Void>> updatePost(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long postId,
            @Valid @ModelAttribute PostUpdateDto dto
    ) {
        postService.updatePost(userId, postId, dto);
        return ResponseEntity.ok(CommonResponse.createSuccess(null));
    }

    @Operation(summary = "게시글 삭제", description = "postId에 해당하는 게시글을 삭제합니다.")
    @DeleteMapping("/{postId}")
    public ResponseEntity<CommonResponse<Void>> deletePost(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long postId
    ) {
        postService.deletePost(userId, postId);
        return ResponseEntity.ok(CommonResponse.createSuccess(null));
    }

}