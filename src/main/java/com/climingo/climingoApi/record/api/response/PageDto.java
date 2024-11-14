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

    private Integer totalPage;

    private Integer page;

    private Boolean isEnd;

    private Long nextCursor;

    private List<T> contents;

}
