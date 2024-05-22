package com.climingo.climingoApi.record.api.response;

import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class PageDto<T> {

    private Long totalCount;

    private Integer resultCount;

    private Integer pageNumber;

    private Boolean isEnd;

    private List<T> contents;

}
