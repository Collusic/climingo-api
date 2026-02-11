package com.climingo.climingoApi.report.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlockRepository extends JpaRepository<Block, Long> {
}
