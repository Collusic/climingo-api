package com.climingo.climingoApi.gym.domain;

import java.util.List;
import org.springframework.data.domain.Page;

public interface GymRepositoryCustom {

    List<Gym> findGymPage(String keyword, Long cursorId, int size);

    long countGyms(String keyword);

}
