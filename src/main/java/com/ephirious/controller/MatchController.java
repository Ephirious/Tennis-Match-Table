package com.ephirious.controller;

import com.ephirious.dto.request.MatchCreateDto;
import com.ephirious.dto.request.PlayerNamePointDto;
import com.ephirious.dto.response.CreatedMatchDto;
import com.ephirious.dto.response.MatchStatusDto;
import com.ephirious.model.value.PlayerName;
import com.ephirious.service.MatchOrchestrator;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/matches")
@RequiredArgsConstructor
public class MatchController {
    private final MatchOrchestrator matchOrchestrator;


    @PostMapping(path = "")
    public CreatedMatchDto createMatch(@Valid @RequestBody MatchCreateDto newMatch) {
        PlayerName first = PlayerName.of(newMatch.firstPlayerName());
        PlayerName second = PlayerName.of(newMatch.secondPlayerName());
        return matchOrchestrator.createMatch(first, second);
    }

    @PostMapping(path = "/{uuid}/point")
    public MatchStatusDto registerPoint(@PathVariable UUID uuid, @Valid @RequestBody PlayerNamePointDto player) {
        PlayerName name = PlayerName.of(player.name());
        return matchOrchestrator.awardPoint(uuid, name);
    }

    @GetMapping(path = "/{uuid}")
    public MatchStatusDto getMatch(@PathVariable UUID uuid) {
        return matchOrchestrator.getPlayingMatch(uuid);
    }
}
