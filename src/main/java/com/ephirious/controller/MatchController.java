package com.ephirious.controller;

import com.ephirious.dto.request.MatchCreateDto;
import com.ephirious.dto.request.PlayerNamePointDto;
import com.ephirious.dto.response.CreatedMatchDto;
import com.ephirious.dto.response.MatchStatusDto;
import com.ephirious.model.value.PlayerName;
import com.ephirious.service.MatchService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/matches")
@RequiredArgsConstructor
public class MatchController {
    private final MatchService service;


    @PostMapping(path = "")
    public CreatedMatchDto createMatch(@Valid @RequestBody MatchCreateDto newMatch) {
        PlayerName first = PlayerName.of(newMatch.firstPlayerName());
        PlayerName second = PlayerName.of(newMatch.secondPlayerName());

        return service.createMatch(first, second);
    }

    @PostMapping(path = "/{uuid}/point")
    public MatchStatusDto registerPoint(@PathVariable UUID uuid, @Valid @RequestBody PlayerNamePointDto player) {
        PlayerName name = PlayerName.of(player.name());
        return service.awardPoint(uuid, name);
    }

    @GetMapping(path = "/{uuid}")
    public MatchStatusDto getMatch(@PathVariable UUID uuid) {
        return service.getMatch(uuid);
    }
}
