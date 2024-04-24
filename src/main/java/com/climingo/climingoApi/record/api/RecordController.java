package com.climingo.climingoApi.record.api;

import com.climingo.climingoApi.record.api.request.RecordCreateRequest;
import com.climingo.climingoApi.record.application.RecordService;
import com.climingo.climingoApi.record.domain.Record;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class RecordController {

    private final RecordService recordService;

    @PostMapping("/records")
    public ResponseEntity<Long> create(@ModelAttribute RecordCreateRequest request) throws IOException {
        Record record = recordService.createRecord(request);
        return ResponseEntity.ok().body(record.getId());
    }

}
