package com.climingo.climingoApi.record.domain;

import java.util.List;

public interface RecordRepositoryCustom {

    List<Record> findAllWithDetails(Long gymId, Long levelId, Long memberId);

}
