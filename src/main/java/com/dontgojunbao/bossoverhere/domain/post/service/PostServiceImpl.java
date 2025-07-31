package com.dontgojunbao.bossoverhere.domain.post.service;

import com.dontgojunbao.bossoverhere.domain.post.dao.PostRepository;
import com.dontgojunbao.bossoverhere.domain.post.domain.Post;
import com.dontgojunbao.bossoverhere.domain.post.dto.PostInfoDto;
import com.dontgojunbao.bossoverhere.domain.post.dto.PostSaveDto;
import com.dontgojunbao.bossoverhere.domain.post.dto.PostUpdateDto;
import com.dontgojunbao.bossoverhere.domain.spot.domain.Spot;
import com.dontgojunbao.bossoverhere.domain.spot.service.SpotService;
import com.dontgojunbao.bossoverhere.domain.user.domain.User;
import com.dontgojunbao.bossoverhere.domain.user.service.UserService;
import com.dontgojunbao.bossoverhere.global.adapter.aws.s3.S3Service;
import com.dontgojunbao.bossoverhere.global.error.ApplicationException;
import com.dontgojunbao.bossoverhere.global.text.ProfanityValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

import static com.dontgojunbao.bossoverhere.global.error.PostErrorCode.FORBIDDEN_POST;
import static com.dontgojunbao.bossoverhere.global.error.PostErrorCode.NOT_FOUND_POST;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostServiceImpl implements PostService{
    private final PostRepository postRepository;
    private final UserService userService;
    private final SpotService spotService;
    private final S3Service s3Service;
    private final ProfanityValidator profanityValidator;
    @Override
    @Transactional
    public Long savePost(Long userId, PostSaveDto dto) {
        User writer = userService.getUserById(userId);

        Spot spot = spotService.getSpotById(dto.spotId());

        // 메모 비속어 차단
        profanityValidator.assertNoBadWordOrThrow(dto.memo());

        Post post = dto.toEntity();
        post.confirmWriter(writer);
        post.updateSpot(spot);


        post.calculateProfit();

        // 4) 파일 업로드가 있으면 S3에 올리고 URL set
        Optional<MultipartFile> maybeFile = dto.uploadFile();
        if (maybeFile.isPresent() && !maybeFile.get().isEmpty()) {
            String url = s3Service.uploadFile(maybeFile.get());
            post.updateImageUrl(url);
        }

        Post p = postRepository.save(post);
        return p.getId();


    }

    @Override
    public PostInfoDto getPost(Long userId, Long postId) {
        return new PostInfoDto(loadPostWithUserCheck(userId, postId));
    }

    @Override
    public Page<PostInfoDto> getPosts(Long userId, Pageable pageable) {
        userService.getUserById(userId);  // 인증 확인
        return postRepository.findAllPosts(pageable)
                .map(PostInfoDto::new);
    }

    @Override
    @Transactional
    public void updatePost(Long userId, Long postId, PostUpdateDto dto) {
        Post post = loadPostWithUserCheck(userId, postId);
        validateOwnership(post, userId);

        dto.spotId().ifPresent(id -> {
            Spot newSpot = spotService.getSpotById(id);
            post.updateSpot(newSpot);
        });

        dto.startAt().ifPresent(post::updateStartAt);
        dto.endAt().ifPresent(post::updateEndAt);
        dto.revenue().ifPresent(post::updateRevenue);
        dto.expense().ifPresent(post::updateExpense);

        dto.memo().ifPresent(m -> {
            profanityValidator.assertNoBadWordOrThrow(m);
            post.updateMemo(m);
        });


        Optional<MultipartFile> maybeFile = dto.uploadFile();
        if (maybeFile.isPresent() && !maybeFile.get().isEmpty()) {
            String url = s3Service.uploadFile(maybeFile.get());
            post.updateImageUrl(url);
        }
        post.calculateProfit();


    }

    @Override
    public void deletePost(Long userId, Long postId) {
        Post post = loadPostWithUserCheck(userId, postId);
        validateOwnership(post, userId);
        postRepository.delete(post);

    }

    private Post loadPostWithUserCheck(Long userId, Long postId) {
        // 1) 로그인(사용자) 검증
        userService.getUserById(userId);
        // 2) Post 존재 검증
        return postRepository.findById(postId)
                .orElseThrow(() -> new ApplicationException(NOT_FOUND_POST));
    }


    private void validateOwnership(Post post, Long userId) {
        if (!post.getWriter().getId().equals(userId)) {
            throw new ApplicationException(FORBIDDEN_POST);
        }
    }
}
