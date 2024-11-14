package com.climingo.climingoApi.gym.application;

import com.climingo.climingoApi.gym.api.response.GymSearchResponse;
import com.climingo.climingoApi.gym.api.response.LevelResponse;
import com.climingo.climingoApi.gym.domain.Gym;
import com.climingo.climingoApi.gym.domain.GymRepository;
import com.climingo.climingoApi.record.api.response.PageDto;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class GymService {

    private final GymRepository gymRepository;

    public List<GymSearchResponse> search(String keyword) {
        return gymRepository.search(keyword);
    }

    public List<LevelResponse> findLevelList(Long gymId) {
        return gymRepository.findLevelsByGymId(gymId);
    }

    public PageDto<GymSearchResponse> searchByCusorBasedPagination(String keyword, Long cursorId, int size) {
        List<Gym> gyms = gymRepository.findGymPage(keyword, cursorId, size);

        boolean isEnd = gyms.size() < size;
        Long nextCursor = isEnd ? null : gyms.get(gyms.size() - 1).getId();

        return PageDto.<GymSearchResponse>builder()
                      .resultCount(gyms.size())
                      .isEnd(isEnd)
                      .nextCursor(nextCursor)
                      .contents(toGymResponses(gyms))
                      .build();
    }

    private List<GymSearchResponse> toGymResponses(List<Gym> gyms) {
        return gyms.stream()
                   .map(gym -> new GymSearchResponse(gym.getId(), gym.getAddress().getAddress(), gym.getAddress().getZipCode(), gym.getName()))
                   .collect(Collectors.toList());
    }

}
