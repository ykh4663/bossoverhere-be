package com.dontgojunbao.bossoverhere.domain.spot.service;

import com.dontgojunbao.bossoverhere.domain.spot.dao.SpotRepository;
import com.dontgojunbao.bossoverhere.domain.spot.domain.Spot;
import com.dontgojunbao.bossoverhere.domain.spot.dto.SpotDto;
import com.dontgojunbao.bossoverhere.domain.user.domain.User;
import com.dontgojunbao.bossoverhere.domain.user.service.UserService;
import com.dontgojunbao.bossoverhere.global.error.ApplicationException;
import com.dontgojunbao.bossoverhere.global.error.SpotErrorCode;
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
class SpotServiceTest {

    @Mock
    private SpotRepository spotRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private SpotService spotService;

    @Test
    @DisplayName("findAll: 정상적으로 모든 스팟을 반환한다")
    void findAll_success() {
        // given
        Long userId = 1L;
        // 인증 유저 리턴 스텁
        given(userService.getUserById(userId))
                .willReturn(User.builder().id(userId).build());
        // repository 스텁
        Spot s1 = Spot.builder()
                .id(10L).name("A지점").address("주소A").latitude(1.1).longitude(2.2)
                .build();
        Spot s2 = Spot.builder()
                .id(20L).name("B지점").address("주소B").latitude(3.3).longitude(4.4)
                .build();
        given(spotRepository.findAll()).willReturn(List.of(s1, s2));

        // when
        List<SpotDto> result = spotService.findAll(userId);

        // then
        assertThat(result).hasSize(2);
        assertThat(result).extracting(SpotDto::getSpotId).containsExactly(10L, 20L);
        assertThat(result).extracting(SpotDto::getSpotName).containsExactly("A지점", "B지점");
        then(userService).should().getUserById(userId);
        then(spotRepository).should().findAll();
    }

    @Test
    @DisplayName("findAll: 존재하지 않는 사용자로 인증 실패할 경우 예외 발생")
    void findAll_userNotFound() {
        // given
        Long userId = 99L;
        willThrow(new ApplicationException(SpotErrorCode.NOTFOUND_SPOT))
                .given(userService).getUserById(userId);

        // when / then
        assertThatThrownBy(() -> spotService.findAll(userId))
                .isInstanceOf(ApplicationException.class)
                .extracting("errorCode")
                .isEqualTo(SpotErrorCode.NOTFOUND_SPOT);

        then(userService).should().getUserById(userId);
        verifyNoInteractions(spotRepository);
    }

    @Test
    @DisplayName("findById: 정상적으로 단건 스팟을 반환한다")
    void findById_success() {
        // given
        Long userId = 2L;
        Long spotId = 42L;
        given(userService.getUserById(userId))
                .willReturn(User.builder().id(userId).build());
        Spot spot = Spot.builder()
                .id(spotId).name("C지점").address("주소C").latitude(5.5).longitude(6.6)
                .build();
        given(spotRepository.findById(spotId)).willReturn(Optional.of(spot));

        // when
        SpotDto dto = spotService.findById(userId, spotId);

        // then
        assertThat(dto.getSpotId()).isEqualTo(spotId);
        assertThat(dto.getSpotName()).isEqualTo("C지점");
        assertThat(dto.getSpotAddress()).isEqualTo("주소C");
        then(userService).should().getUserById(userId);
        then(spotRepository).should().findById(spotId);
    }

    @Test
    @DisplayName("findById: 스팟이 존재하지 않을 때 예외 발생")
    void findById_notFound() {
        // given
        Long userId = 3L;
        Long spotId = 99L;
        given(userService.getUserById(userId))
                .willReturn(User.builder().id(userId).build());
        given(spotRepository.findById(spotId))
                .willReturn(Optional.empty());

        // when / then
        assertThatThrownBy(() -> spotService.findById(userId, spotId))
                .isInstanceOf(ApplicationException.class)
                .satisfies(ex -> {
                    ApplicationException ae = (ApplicationException) ex;
                    assertThat(ae.getErrorCode())
                            .isEqualTo(SpotErrorCode.NOTFOUND_SPOT);
                });

        then(userService).should().getUserById(userId);
        then(spotRepository).should().findById(spotId);
    }
}