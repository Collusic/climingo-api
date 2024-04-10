package com.climingo.climingoApi.gym.domain;

import com.climingo.climingoApi.gym.api.response.GradeResponse;
import com.climingo.climingoApi.gym.api.response.GymSearchResponse;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface GymRepository extends JpaRepository<Gym, Long> {

    @Query("select new com.climingo.climingoApi.gym.api.response.GymSearchResponse(g.id, g.address.address, g.address.zipCode, g.name) from Gym g where g.name like %:keyword%")
    List<GymSearchResponse> search(@Param("keyword") String keyword);

    @Query("select new com.climingo.climingoApi.gym.api.response.GradeResponse(gd.id, gd.colorName) from Grade gd where gd.gym.id = :gymId")
    List<GradeResponse> findGradesByGymId(@Param("gymId") Long gymId);

}
