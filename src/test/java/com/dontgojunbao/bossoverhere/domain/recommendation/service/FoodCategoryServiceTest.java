package com.dontgojunbao.bossoverhere.domain.recommendation.service;

import com.dontgojunbao.bossoverhere.domain.recommendation.dao.FoodCategoryClusterRepository;
import com.dontgojunbao.bossoverhere.domain.recommendation.dao.FoodCategoryRepository;
import com.dontgojunbao.bossoverhere.domain.recommendation.domain.Cluster;
import com.dontgojunbao.bossoverhere.domain.recommendation.domain.FoodCategory;
import com.dontgojunbao.bossoverhere.domain.recommendation.domain.FoodCategoryCluster;
import com.dontgojunbao.bossoverhere.domain.recommendation.dto.response.ClusterDetailResponse;
import com.dontgojunbao.bossoverhere.domain.recommendation.dto.response.FoodCategoryDetailResponse;

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

    @Mock private UserService userService;
    @Mock private FoodCategoryRepository categoryRepo;
    @Mock private FoodCategoryClusterRepository mappingRepo;
    @InjectMocks private FoodCategoryService foodCategoryService;

    @Test
    @DisplayName("findAll: 모든 카테고리에 대해 DetailDTO 리스트를 반환한다")
    void findAll_success() {
        // given
        Long userId = 1L;
        User dummyUser = User.builder().id(userId).build();
        given(userService.getUserById(userId)).willReturn(dummyUser);

        FoodCategory fc1 = FoodCategory.builder()
                .id(100L)
                .name("분식")
                .description("간단한 분식")
                .build();
        FoodCategory fc2 = FoodCategory.builder()
                .id(200L)
                .name("한식")
                .description("덮밥류")
                .build();
        given(categoryRepo.findAll()).willReturn(List.of(fc1, fc2));

        // when
        List<FoodCategoryDetailResponse> result = foodCategoryService.findAll(userId);

        // then
        assertThat(result).hasSize(2);

        FoodCategoryDetailResponse r1 = result.get(0);
        assertThat(r1.getId()).isEqualTo(100L);
        assertThat(r1.getName()).isEqualTo("분식");
        assertThat(r1.getDescription()).isEqualTo("간단한 분식");

        FoodCategoryDetailResponse r2 = result.get(1);
        assertThat(r2.getId()).isEqualTo(200L);
        assertThat(r2.getName()).isEqualTo("한식");
        assertThat(r2.getDescription()).isEqualTo("덮밥류");

        then(userService).should().getUserById(userId);
        then(categoryRepo).should().findAll();
        then(mappingRepo).shouldHaveNoInteractions();
    }

    @Test
    @DisplayName("findClustersByCategory: 존재하는 카테고리 ID로 ClusterDetailResponse 리스트를 반환한다")
    void findClustersByCategory_success() {
        // given
        Long userId = 2L;
        Long categoryId = 300L;
        User dummyUser = User.builder().id(userId).build();
        given(userService.getUserById(userId)).willReturn(dummyUser);

        FoodCategory fc = FoodCategory.builder()
                .id(categoryId)
                .name("카페/디저트")
                .description("커피, 디저트")
                .build();
        given(categoryRepo.findById(categoryId)).willReturn(Optional.of(fc));

        Cluster cluster1 = Cluster.builder()
                .id(10L)
                .title("고요한 휴식")
                .nickname("실버 쉼터")
                .description("조용한 공간")
                .situations("산책, 독서")
                .build();
        Cluster cluster2 = Cluster.builder()
                .id(20L)
                .title("활기찬 모임")
                .nickname("젊음의 장터")
                .description("젊은층 밀집")
                .situations("친구와의 약속")
                .build();

        FoodCategoryCluster fcc1 = mock(FoodCategoryCluster.class);
        given(fcc1.getCluster()).willReturn(cluster1);
        FoodCategoryCluster fcc2 = mock(FoodCategoryCluster.class);
        given(fcc2.getCluster()).willReturn(cluster2);
        given(mappingRepo.findAllByFoodCategory_Id(categoryId))
                .willReturn(List.of(fcc1, fcc2));

        // when
        List<ClusterDetailResponse> result =
                foodCategoryService.findClustersByCategory(userId, categoryId);

        // then
        assertThat(result).hasSize(2);

        ClusterDetailResponse c1 = result.get(0);
        assertThat(c1.getId()).isEqualTo(10L);
        assertThat(c1.getTitle()).isEqualTo("고요한 휴식");
        assertThat(c1.getNickname()).isEqualTo("실버 쉼터");
        assertThat(c1.getDescription()).isEqualTo("조용한 공간");
        assertThat(c1.getSituations()).isEqualTo("산책, 독서");

        ClusterDetailResponse c2 = result.get(1);
        assertThat(c2.getId()).isEqualTo(20L);
        assertThat(c2.getTitle()).isEqualTo("활기찬 모임");
        assertThat(c2.getNickname()).isEqualTo("젊음의 장터");
        assertThat(c2.getDescription()).isEqualTo("젊은층 밀집");
        assertThat(c2.getSituations()).isEqualTo("친구와의 약속");

        then(userService).should().getUserById(userId);
        then(categoryRepo).should().findById(categoryId);
        then(mappingRepo).should().findAllByFoodCategory_Id(categoryId);
    }

    @Test
    @DisplayName("findClustersByCategory: 없는 카테고리 ID로 조회 시 ApplicationException 발생")
    void findClustersByCategory_notFound() {
        // given
        Long userId = 3L;
        Long categoryId = 400L;
        User dummyUser = User.builder().id(userId).build();
        given(userService.getUserById(userId)).willReturn(dummyUser);
        given(categoryRepo.findById(categoryId)).willReturn(Optional.empty());

        // when / then
        ApplicationException ex = catchThrowableOfType(
                () -> foodCategoryService.findClustersByCategory(userId, categoryId),
                ApplicationException.class
        );
        assertThat(ex.getErrorCode())
                .isEqualTo(FoodCategoryErrorCode.NOTFOUND_FOOD_CATEGORY);

        then(userService).should().getUserById(userId);
        then(categoryRepo).should().findById(categoryId);
        then(mappingRepo).should(never()).findAllByFoodCategory_Id(anyLong());
    }
}