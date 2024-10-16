package com.climingo.climingoApi.record.domain;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RecordRepository extends JpaRepository<Record, Long>, RecordRepositoryCustom {

    @Query("SELECT r FROM Record r JOIN FETCH r.member WHERE r.id = :recordId")
    Optional<Record> findByIdWithMember(@Param("recordId") Long recordId);
}
