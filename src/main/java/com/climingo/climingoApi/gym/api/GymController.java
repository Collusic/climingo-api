package com.climingo.climingoApi.gym.api;

import com.climingo.climingoApi.gym.api.response.LevelResponse;
import com.climingo.climingoApi.gym.api.response.GymSearchResponse;
import com.climingo.climingoApi.gym.application.GymService;
import java.util.List;

import feign.Param;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name="GymController", description = "클라이밍장 장소, 난이도 조회 api")
@RequiredArgsConstructor
@RestController
public class GymController {

    private final GymService gymService;

    @GetMapping("/search/gyms")
    @Operation(summary="클라이밍장 검색", description = "클라이밍장 이름을 검색합니다.")
    @Parameter(name = "keyword" , description = "클라이밍장 이름")
    public ResponseEntity<List<GymSearchResponse>> search(@RequestParam("keyword") String keyword) {
        List<GymSearchResponse> response = gymService.search(keyword);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/gyms/{gymId}/levels")
    @Operation(summary="클라이밍장 난이도 목록 조회", description = "")
    @Parameter(name = "gymId", description = "클라이밍장 아이디")
    public ResponseEntity<List<LevelResponse>> findLevelList(@PathVariable Long gymId) {
        List<LevelResponse> response = gymService.findLevelList(gymId);
        return ResponseEntity.ok().body(response);
    }

}
