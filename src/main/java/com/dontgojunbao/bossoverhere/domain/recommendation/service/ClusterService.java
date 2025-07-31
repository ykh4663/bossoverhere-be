package com.dontgojunbao.bossoverhere.domain.recommendation.service;

import com.dontgojunbao.bossoverhere.domain.recommendation.dao.ClusterRepository;
import com.dontgojunbao.bossoverhere.domain.recommendation.domain.Cluster;

import com.dontgojunbao.bossoverhere.global.error.ApplicationException;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



import static com.dontgojunbao.bossoverhere.global.error.ClusterErrorCode.NOTFOUND_CLUSTER;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ClusterService {

    private final ClusterRepository clusterRepo;

    public Cluster getClusterById(Long clusterId) {
        return clusterRepo.findById(clusterId)
                .orElseThrow(() -> new ApplicationException(NOTFOUND_CLUSTER));
    }
}