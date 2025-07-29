package com.dontgojunbao.bossoverhere.domain.recommendation.service;


import com.dontgojunbao.bossoverhere.domain.recommendation.dao.ClusterRepository;
import com.dontgojunbao.bossoverhere.domain.recommendation.domain.Cluster;
import com.dontgojunbao.bossoverhere.domain.recommendation.dto.response.ClusterDetailResponse;
import com.dontgojunbao.bossoverhere.domain.recommendation.dto.response.ClusterSimpleResponse;
import com.dontgojunbao.bossoverhere.domain.user.domain.User;
import com.dontgojunbao.bossoverhere.domain.user.service.UserService;
import com.dontgojunbao.bossoverhere.global.error.ApplicationException;
import com.dontgojunbao.bossoverhere.global.error.ClusterErrorCode;
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
class ClusterServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private ClusterRepository clusterRepo;

    @InjectMocks
    private ClusterService clusterService;

    @Test
    @DisplayName("findAll: 정상적으로 모든 클러스터를 SimpleDTO 로 반환한다")
    void findAll_success() {
        // given
        Long userId = 42L;
        User dummyUser = User.builder().id(userId).build();
        given(userService.getUserById(userId)).willReturn(dummyUser);

        Cluster c1 = Cluster.builder()
                .id(1L)
                .title("타이틀1")
                .nickname("별명1")
                .build();
        Cluster c2 = Cluster.builder()
                .id(2L)
                .title("타이틀2")
                .nickname("별명2")
                .build();
        given(clusterRepo.findAll()).willReturn(List.of(c1, c2));

        // when
        List<ClusterSimpleResponse> result = clusterService.findAll(userId);

        // then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getId()).isEqualTo(1L);
        assertThat(result.get(0).getTitle()).isEqualTo("타이틀1");
        assertThat(result.get(0).getNickname()).isEqualTo("별명1");
        then(userService).should().getUserById(userId);
        then(clusterRepo).should().findAll();
    }

    @Test
    @DisplayName("findById: 존재하는 클러스터 ID 로 상세 DTO 를 반환한다")
    void findById_success() {
        // given
        Long userId = 7L;
        Long clusterId = 99L;
        User dummyUser = User.builder().id(userId).build();
        given(userService.getUserById(userId)).willReturn(dummyUser);

        Cluster c = Cluster.builder()
                .id(clusterId)
                .title("타이틀99")
                .nickname("별명99")
                .description("상세설명99")
                .situations("상황99")
                .build();
        given(clusterRepo.findById(clusterId)).willReturn(Optional.of(c));

        // when
        ClusterDetailResponse dto = clusterService.findById(userId, clusterId);

        // then
        assertThat(dto.getId()).isEqualTo(clusterId);
        assertThat(dto.getTitle()).isEqualTo("타이틀99");
        assertThat(dto.getNickname()).isEqualTo("별명99");
        assertThat(dto.getDescription()).isEqualTo("상세설명99");
        assertThat(dto.getSituations()).isEqualTo("상황99");
        then(userService).should().getUserById(userId);
        then(clusterRepo).should().findById(clusterId);
    }

    @Test
    @DisplayName("findById: 없는 클러스터 ID 로 조회 시 ApplicationException 발생")
    void findById_notFound() {
        // given
        Long userId = 5L;
        Long clusterId = 1234L;
        User dummyUser = User.builder().id(userId).build();
        given(userService.getUserById(userId)).willReturn(dummyUser);
        given(clusterRepo.findById(clusterId)).willReturn(Optional.empty());

        // when / then
        ApplicationException ex = catchThrowableOfType(
                () -> clusterService.findById(userId, clusterId),
                ApplicationException.class
        );
        assertThat(ex.getErrorCode()).isEqualTo(ClusterErrorCode.NOTFOUND_CLUSTER);
        then(userService).should().getUserById(userId);
        then(clusterRepo).should().findById(clusterId);
    }
}