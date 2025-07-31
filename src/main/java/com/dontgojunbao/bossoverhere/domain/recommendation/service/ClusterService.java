package com.dontgojunbao.bossoverhere.domain.recommendation.service;

import com.dontgojunbao.bossoverhere.domain.recommendation.dao.ClusterRepository;
import com.dontgojunbao.bossoverhere.domain.recommendation.domain.Cluster;
import com.dontgojunbao.bossoverhere.domain.recommendation.dto.response.ClusterDetailResponse;
import com.dontgojunbao.bossoverhere.domain.recommendation.dto.response.ClusterDto;
import com.dontgojunbao.bossoverhere.domain.user.service.UserService;
import com.dontgojunbao.bossoverhere.global.error.ApplicationException;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.dontgojunbao.bossoverhere.global.error.ClusterErrorCode.NOTFOUND_CLUSTER;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ClusterService {
    private final UserService userService;
    private final ClusterRepository clusterRepo;

    public List<ClusterDto> findAll(Long userId) {
        userService.getUserById(userId);
        return clusterRepo.findAll().stream()
                .map(c -> new ClusterDto(c.getId(), c.getTitle(), c.getNickname()))
                .toList();
    }
    public ClusterDetailResponse findById(Long userId, Long clusterId) {
        userService.getUserById(userId);
        Cluster c = clusterRepo.findById(clusterId)
                .orElseThrow(() -> new ApplicationException(NOTFOUND_CLUSTER));
        return new ClusterDetailResponse(
                c.getId(), c.getTitle(), c.getNickname(), c.getDescription(), c.getSituations()
        );
    }
}