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
import com.dontgojunbao.bossoverhere.global.error.PostErrorCode;
import com.dontgojunbao.bossoverhere.global.text.ProfanityValidator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.dontgojunbao.bossoverhere.global.error.PostErrorCode.FORBIDDEN_POST;
import static com.dontgojunbao.bossoverhere.global.error.PostErrorCode.NOT_FOUND_POST;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowableOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    @Mock PostRepository postRepository;
    @Mock UserService userService;
    @Mock SpotService spotService;
    @Mock S3Service s3Service;
    @Mock
    ProfanityValidator profanityValidator;
    @InjectMocks PostServiceImpl postService;

    @Test @DisplayName("savePost: 파일 없이 정상 저장")
    void savePost_withoutFile_success() {
        Long userId = 1L;
        String memo = "clean memo";
        PostSaveDto dto = new PostSaveDto(
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(2),
                10L, 100L, 50L, memo,
                Optional.empty()
        );
        User writer = User.builder().id(userId).oauthId("oauth-1").build();
        Spot spot = Spot.builder().id(10L).build();
        Post saved = Post.builder().id(99L).build();

        given(userService.getUserById(userId)).willReturn(writer);
        given(spotService.getSpotById(10L)).willReturn(spot);
        // profanityValidator.assertNoBadWordOrThrow does nothing by default
        given(postRepository.save(any(Post.class))).willReturn(saved);

        Long result = postService.savePost(userId, dto);

        assertThat(result).isEqualTo(99L);
        then(profanityValidator).should().assertNoBadWordOrThrow(memo);
        then(s3Service).should(never()).uploadFile(any());
        then(postRepository).should().save(any(Post.class));
    }

    @Test @DisplayName("savePost: 메모에 비속어 포함 시 BAD_WORD_DETECTED 예외")
    void savePost_badWordInMemo_throws() {
        Long userId = 2L;
        String badMemo = "someBadWord";
        PostSaveDto dto = new PostSaveDto(
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(1),
                20L, 200L, 80L, badMemo,
                Optional.empty()
        );
        User writer = User.builder().id(userId).oauthId("oauth-2").build();
        Spot spot = Spot.builder().id(20L).build();

        given(userService.getUserById(userId)).willReturn(writer);
        given(spotService.getSpotById(20L)).willReturn(spot);
        doThrow(new ApplicationException(PostErrorCode.BAD_WORD_DETECTED))
                .when(profanityValidator).assertNoBadWordOrThrow(badMemo);

        ApplicationException ex = catchThrowableOfType(
                () -> postService.savePost(userId, dto),
                ApplicationException.class
        );
        assertThat(ex.getErrorCode()).isEqualTo(PostErrorCode.BAD_WORD_DETECTED);

        then(profanityValidator).should().assertNoBadWordOrThrow(badMemo);
        then(postRepository).should(never()).save(any());
        then(s3Service).shouldHaveNoInteractions();
    }

    @Test @DisplayName("savePost: 파일 업로드 후 URL 세팅")
    void savePost_withFile_success() throws Exception {
        Long userId = 3L;
        var file = mock(org.springframework.web.multipart.MultipartFile.class);
        given(file.isEmpty()).willReturn(false);
        String memo = "clean";

        PostSaveDto dto = new PostSaveDto(
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(1),
                30L, 300L, 150L, memo,
                Optional.of(file)
        );
        User writer = User.builder().id(userId).oauthId("oauth-3").build();
        Spot spot = Spot.builder().id(30L).build();
        String url = "http://s3/bucket/img.png";
        Post saved = Post.builder().id(100L).build();

        given(userService.getUserById(userId)).willReturn(writer);
        given(spotService.getSpotById(30L)).willReturn(spot);
        doNothing().when(profanityValidator).assertNoBadWordOrThrow(memo);
        given(s3Service.uploadFile(file)).willReturn(url);

        ArgumentCaptor<Post> captor = ArgumentCaptor.forClass(Post.class);
        given(postRepository.save(captor.capture())).willReturn(saved);

        Long id = postService.savePost(userId, dto);

        assertThat(id).isEqualTo(100L);
        Post toSave = captor.getValue();
        assertThat(toSave.getImageUrl()).isEqualTo(url);
        then(profanityValidator).should().assertNoBadWordOrThrow(memo);
    }

    @Test @DisplayName("getPost: 없으면 NOT_FOUND_POST")
    void getPost_notFound() {
        Long userId = 4L, postId = 99L;
        given(userService.getUserById(userId))
                .willReturn(User.builder().id(userId).oauthId("oauth-4").build());
        given(postRepository.findById(postId)).willReturn(Optional.empty());

        ApplicationException ex = catchThrowableOfType(
                () -> postService.getPost(userId, postId),
                ApplicationException.class
        );

        assertThat(ex.getErrorCode()).isEqualTo(NOT_FOUND_POST);
    }

    @Test @DisplayName("getPosts: 페이징 조회 성공")
    void getPosts_success() {
        Long userId = 5L;
        given(userService.getUserById(userId))
                .willReturn(User.builder().id(userId).oauthId("oauth-5").build());

        Post p1 = Post.builder().id(1L)
                .writer(User.builder().id(userId).build())
                .spot(Spot.builder().id(2L).build())
                .build();
        Post p2 = Post.builder().id(2L)
                .writer(User.builder().id(userId).build())
                .spot(Spot.builder().id(3L).build())
                .build();

        Page<Post> page = new PageImpl<>(List.of(p1, p2));
        given(postRepository.findAllPosts(PageRequest.of(0,10))).willReturn(page);

        Page<PostInfoDto> result = postService.getPosts(userId, PageRequest.of(0,10));

        assertThat(result.getTotalElements()).isEqualTo(2);
        assertThat(result.getContent())
                .extracting(PostInfoDto::getPostId)
                .containsExactlyInAnyOrder(1L, 2L);
    }

    @Test @DisplayName("updatePost: 정상 수정")
    void updatePost_success() {
        Long userId = 6L, postId = 60L;
        User user = User.builder().id(userId).oauthId("oauth-6").build();
        Post post = Post.builder()
                .id(postId).writer(user)
                .spot(Spot.builder().id(1L).build())
                .startAt(LocalDateTime.of(2025,1,1,9,0))
                .endAt(LocalDateTime.of(2025,1,1,17,0))
                .revenue(100L).expense(40L).memo("old")
                .build();
        post.calculateProfit();

        PostUpdateDto dto = new PostUpdateDto(
                Optional.of(2L),
                Optional.of(LocalDateTime.of(2025,1,1,10,0)),
                Optional.empty(),
                Optional.of(120L),
                Optional.empty(),
                Optional.of("new memo"),
                Optional.empty()
        );
        Spot newSpot = Spot.builder().id(2L).build();

        given(userService.getUserById(userId)).willReturn(user);
        given(postRepository.findById(postId)).willReturn(Optional.of(post));
        given(spotService.getSpotById(2L)).willReturn(newSpot);

        postService.updatePost(userId, postId, dto);

        assertThat(post.getSpot().getId()).isEqualTo(2L);
        assertThat(post.getStartAt().getHour()).isEqualTo(10);
        assertThat(post.getRevenue()).isEqualTo(120L);
        assertThat(post.getMemo()).isEqualTo("new memo");
    }

    @Test @DisplayName("updatePost: 작성자 불일치 FORBIDDEN_POST")
    void updatePost_forbidden() {
        Long userId = 7L, postId = 70L;
        Post post = Post.builder()
                .id(postId)
                .writer(User.builder().id(77L).build())
                .build();

        given(userService.getUserById(userId))
                .willReturn(User.builder().id(userId).oauthId("oauth-7").build());
        given(postRepository.findById(postId)).willReturn(Optional.of(post));
        PostUpdateDto dto = new PostUpdateDto(
                Optional.of(2L),
                Optional.of(LocalDateTime.of(2025,1,1,10,0)),
                Optional.empty(),
                Optional.of(120L),
                Optional.empty(),
                Optional.of("new memo"),
                Optional.empty()
        );
        ApplicationException ex = catchThrowableOfType(
                () -> postService.updatePost(userId, postId, dto),
                ApplicationException.class
        );
        assertThat(ex.getErrorCode()).isEqualTo(FORBIDDEN_POST);
    }

    @Test @DisplayName("updatePost: 메모에 비속어 포함 시 BAD_WORD_DETECTED 예외")
    void updatePost_badWordInMemo_throws() {
        Long userId = 4L, postId = 40L;
        User user = User.builder().id(userId).oauthId("oauth-4").build();
        Post post = Post.builder()
                .id(postId).writer(user)
                .spot(Spot.builder().id(1L).build())
                .startAt(LocalDateTime.now())
                .endAt(LocalDateTime.now().plusHours(1))
                .revenue(100L).expense(20L)
                .memo("old").build();
        post.calculateProfit();

        String badMemo = "dirtyWord";
        PostUpdateDto dto = new PostUpdateDto(
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.of(badMemo),
                Optional.empty()
        );

        given(userService.getUserById(userId)).willReturn(user);
        given(postRepository.findById(postId)).willReturn(Optional.of(post));
        doThrow(new ApplicationException(PostErrorCode.BAD_WORD_DETECTED))
                .when(profanityValidator).assertNoBadWordOrThrow(badMemo);

        ApplicationException ex = catchThrowableOfType(
                () -> postService.updatePost(userId, postId, dto),
                ApplicationException.class
        );
        assertThat(ex.getErrorCode()).isEqualTo(PostErrorCode.BAD_WORD_DETECTED);

        then(profanityValidator).should().assertNoBadWordOrThrow(badMemo);
        then(postRepository).should(never()).save(any());
    }

    @Test @DisplayName("deletePost: 정상 삭제")
    void deletePost_success() {
        Long userId = 8L, postId = 80L;
        User user = User.builder().id(userId).oauthId("oauth-8").build();
        Post post = Post.builder().id(postId).writer(user).build();

        given(userService.getUserById(userId)).willReturn(user);
        given(postRepository.findById(postId)).willReturn(Optional.of(post));

        postService.deletePost(userId, postId);

        then(postRepository).should().delete(post);
    }

    @Test @DisplayName("deletePost: 불일치 FORBIDDEN_POST")
    void deletePost_forbidden() {
        Long userId = 9L, postId = 90L;
        Post post = Post.builder().id(postId).writer(User.builder().id(99L).build()).build();

        given(userService.getUserById(userId)).willReturn(User.builder().id(userId).oauthId("oauth-9").build());
        given(postRepository.findById(postId)).willReturn(Optional.of(post));

        ApplicationException ex = catchThrowableOfType(
                () -> postService.deletePost(userId, postId),
                ApplicationException.class
        );
        assertThat(ex.getErrorCode()).isEqualTo(FORBIDDEN_POST);
    }

    @Test @DisplayName("deletePost: 없으면 NOT_FOUND_POST")
    void deletePost_notFound() {
        Long userId = 10L, postId = 100L;
        given(userService.getUserById(userId)).willReturn(User.builder().id(userId).oauthId("oauth-10").build());
        given(postRepository.findById(postId)).willReturn(Optional.empty());

        ApplicationException ex = catchThrowableOfType(
                () -> postService.deletePost(userId, postId),
                ApplicationException.class
        );
        assertThat(ex.getErrorCode()).isEqualTo(NOT_FOUND_POST);
    }


}
