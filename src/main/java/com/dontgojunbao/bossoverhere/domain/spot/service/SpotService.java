package com.dontgojunbao.bossoverhere.domain.spot.service;

import com.dontgojunbao.bossoverhere.domain.spot.dao.SpotRepository;
import com.dontgojunbao.bossoverhere.domain.spot.domain.Spot;
import com.dontgojunbao.bossoverhere.domain.spot.dto.SpotDto;
import com.dontgojunbao.bossoverhere.domain.user.service.UserService;
import com.dontgojunbao.bossoverhere.global.error.ApplicationException;
import com.dontgojunbao.bossoverhere.global.error.SpotErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SpotService {
    private final SpotRepository spotRepository;
    private final UserService userService; // 인증 검증이 필요하다면

    /**
     * 전체 스팟 조회
     */
    public List<SpotDto> findAll(Long userId) {
        userService.getUserById(userId); // 인증만 확인하고 리턴 값은 사용 안 해도 됩니다
        return spotRepository.findAll()
                .stream()
                .map(SpotDto::from)
                .toList();
    }

    /**
     * 단건 스팟 조회
     */
    public SpotDto findById(Long userId, Long spotId) {
        userService.getUserById(userId);
        Spot spot = spotRepository.findById(spotId)
                .orElseThrow(() -> new ApplicationException(SpotErrorCode.NOTFOUND_SPOT));
        return SpotDto.from(spot);
    }
}