package com.climingo.climingoApi.gym.api;

import com.climingo.climingoApi.gym.api.response.GymSearchResponse;
import com.climingo.climingoApi.gym.api.response.LevelResponse;
import com.climingo.climingoApi.gym.application.GymService;
import com.climingo.climingoApi.record.api.response.PageDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class GymController {

    private final GymService gymService;

    @GetMapping("/search/gyms")
    public ResponseEntity<List<GymSearchResponse>> search(@RequestParam("keyword") String keyword) {
        List<GymSearchResponse> response = gymService.search(keyword);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/gyms/paged")
    public ResponseEntity<PageDto<GymSearchResponse>> searchByCusorBasedPagination(@RequestParam(name = "keyword", required = false) String keyword,
                                                                                   @RequestParam(name = "cusorId", required = false) Long cursorId,
                                                                                   @RequestParam("size") int size) {
        PageDto<GymSearchResponse> response = gymService.searchByCusorBasedPagination(keyword, cursorId, size);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/gyms/{gymId}/levels")
    public ResponseEntity<List<LevelResponse>> findLevelList(@PathVariable Long gymId) {
        List<LevelResponse> response = gymService.findLevelList(gymId);
        return ResponseEntity.ok().body(response);
    }

}
