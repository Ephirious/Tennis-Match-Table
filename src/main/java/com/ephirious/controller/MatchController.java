package com.ephirious.controller;

import com.ephirious.dto.request.MatchCreateDto;
import com.ephirious.dto.request.MatchesFilterDto;
import com.ephirious.dto.request.PlayerNamePointDto;
import com.ephirious.dto.response.CompletedPaginationMatchDto;
import com.ephirious.dto.response.CreatedMatchDto;
import com.ephirious.dto.response.MatchStatusDto;
import com.ephirious.model.value.PlayerName;
import com.ephirious.service.MatchOrchestrator;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping("/matches")
@RequiredArgsConstructor
public class MatchController {
    private final MatchOrchestrator matchOrchestrator;


    @PostMapping(path = "")
    public ResponseEntity<CreatedMatchDto> createMatch(@Valid @RequestBody MatchCreateDto newMatch) {
        PlayerName first = PlayerName.of(newMatch.firstPlayerName());
        PlayerName second = PlayerName.of(newMatch.secondPlayerName());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(matchOrchestrator.createMatch(first, second));
    }

    @PostMapping(path = "/{uuid}/point")
    public ResponseEntity<MatchStatusDto> registerPoint(@PathVariable UUID uuid, @Valid @RequestBody PlayerNamePointDto player) {
        PlayerName name = PlayerName.of(player.name());
        return ResponseEntity.status(HttpStatus.OK)
                .body(matchOrchestrator.awardPoint(uuid, name));
    }

    @GetMapping(path = "/{uuid}")
    public ResponseEntity<MatchStatusDto> getMatch(@PathVariable UUID uuid) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(matchOrchestrator.getPlayingMatch(uuid));
    }

    @GetMapping
    public ResponseEntity<CompletedPaginationMatchDto> getCompletedMatches(@Valid MatchesFilterDto params) {
        CompletedPaginationMatchDto dto = params.hasPlayerName()
                ? matchOrchestrator.getCompletedMatches(params.page())
                : matchOrchestrator.getCompletedMatchesByName(params.page(), PlayerName.of(params.playerName()));

        return ResponseEntity.status(HttpStatus.OK).body(dto);
    }
}
