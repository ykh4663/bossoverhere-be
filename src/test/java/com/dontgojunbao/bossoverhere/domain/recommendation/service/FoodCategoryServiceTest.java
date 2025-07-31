package com.dontgojunbao.bossoverhere.domain.recommendation.service;

import com.dontgojunbao.bossoverhere.domain.recommendation.dao.FoodCategoryClusterRepository;
import com.dontgojunbao.bossoverhere.domain.recommendation.dao.FoodCategoryRepository;
import com.dontgojunbao.bossoverhere.domain.recommendation.domain.FoodCategory;
import com.dontgojunbao.bossoverhere.domain.recommendation.dto.response.FoodCategoryDetailResponse;
import com.dontgojunbao.bossoverhere.domain.recommendation.dto.response.FoodCategoryDto;
import com.dontgojunbao.bossoverhere.domain.user.domain.User;
import com.dontgojunbao.bossoverhere.domain.user.service.UserService;
import com.dontgojunbao.bossoverhere.global.error.ApplicationException;
import com.dontgojunbao.bossoverhere.global.error.FoodCategoryErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class FoodCategoryServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private FoodCategoryRepository categoryRepo;

    @Mock
    private FoodCategoryClusterRepository mappingRepo;

    @InjectMocks
    private FoodCategoryService foodCategoryService;

    @Test
    @DisplayName("findAll: 모든 카테고리에 대해 SimpleDTO 리스트를 반환한다")
    void findAll_success() {
        // given
        Long userId = 10L;
        User dummyUser = User.builder().id(userId).build();
        given(userService.getUserById(userId)).willReturn(dummyUser);

        FoodCategory fc1 = FoodCategory.builder()
                .id(100L)
                .name("분식")
                .description(null)
                .build();
        FoodCategory fc2 = FoodCategory.builder()
                .id(200L)
                .name("한식")
                .description("덮밥류")
                .build();
        given(categoryRepo.findAll()).willReturn(List.of(fc1, fc2));

        given(mappingRepo.findClusterIdsByCategoryId(100L)).willReturn(List.of(1L, 2L));
        given(mappingRepo.findClusterIdsByCategoryId(200L)).willReturn(List.of(2L, 3L));

        // when
        List<FoodCategoryDto> result = foodCategoryService.findAll(userId);

        // then
        assertThat(result).hasSize(2);

        FoodCategoryDto r1 = result.get(0);
        assertThat(r1.getId()).isEqualTo(100L);
        assertThat(r1.getName()).isEqualTo("분식");
        assertThat(r1.getClusterIds()).containsExactly(1L, 2L);

        FoodCategoryDto r2 = result.get(1);
        assertThat(r2.getId()).isEqualTo(200L);
        assertThat(r2.getName()).isEqualTo("한식");
        assertThat(r2.getClusterIds()).containsExactly(2L, 3L);

        then(userService).should().getUserById(userId);
        then(categoryRepo).should().findAll();
        then(mappingRepo).should().findClusterIdsByCategoryId(100L);
        then(mappingRepo).should().findClusterIdsByCategoryId(200L);
    }

    @Test
    @DisplayName("findById: 존재하는 카테고리 ID 로 상세 DTO 를 반환한다")
    void findById_success() {
        // given
        Long userId = 20L;
        Long categoryId = 300L;
        User dummyUser = User.builder().id(userId).build();
        given(userService.getUserById(userId)).willReturn(dummyUser);

        FoodCategory fc = FoodCategory.builder()
                .id(categoryId)
                .name("카페/디저트")
                .description("커피, 디저트")
                .build();
        given(categoryRepo.findById(categoryId)).willReturn(Optional.of(fc));
        given(mappingRepo.findClusterIdsByCategoryId(categoryId)).willReturn(List.of(5L, 6L));

        // when
        FoodCategoryDetailResponse dto = foodCategoryService.findById(userId, categoryId);

        // then
        assertThat(dto.getId()).isEqualTo(categoryId);
        assertThat(dto.getName()).isEqualTo("카페/디저트");
        assertThat(dto.getDescription()).isEqualTo("커피, 디저트");
        assertThat(dto.getClusterIds()).containsExactly(5L, 6L);

        then(userService).should().getUserById(userId);
        then(categoryRepo).should().findById(categoryId);
        then(mappingRepo).should().findClusterIdsByCategoryId(categoryId);
    }

    @Test
    @DisplayName("findById: 없는 카테고리 ID 로 조회 시 ApplicationException 발생")
    void findById_notFound() {
        // given
        Long userId = 30L;
        Long categoryId = 400L;
        User dummyUser = User.builder().id(userId).build();
        given(userService.getUserById(userId)).willReturn(dummyUser);
        given(categoryRepo.findById(categoryId)).willReturn(Optional.empty());

        // when / then
        ApplicationException ex = catchThrowableOfType(
                () -> foodCategoryService.findById(userId, categoryId),
                ApplicationException.class
        );

        assertThat(ex.getErrorCode())
                .isEqualTo(FoodCategoryErrorCode.NOTFOUND_FOOD_CATEGORY);

        then(userService).should().getUserById(userId);
        then(categoryRepo).should().findById(categoryId);
        then(mappingRepo).should(never()).findClusterIdsByCategoryId(any());
    }
}