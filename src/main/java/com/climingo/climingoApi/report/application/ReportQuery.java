package com.climingo.climingoApi.report.application;

import com.climingo.climingoApi.report.api.response.ReportResponse;

import java.util.List;

public interface ReportQuery {
    List<ReportResponse> readReports();
}
