package com.climingo.climingoApi.gym.application;

import com.climingo.climingoApi.gym.api.response.GradeResponse;
import com.climingo.climingoApi.gym.api.response.GymSearchResponse;
import com.climingo.climingoApi.gym.domain.GymRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class GymService {

    private final GymRepository gymRepository;

    public List<GymSearchResponse> search(String keyword) {
        return gymRepository.search(keyword);
    }

    public List<GradeResponse> findGradeList(Long gymId) {
        return gymRepository.findGradesByGymId(gymId);
    }

}