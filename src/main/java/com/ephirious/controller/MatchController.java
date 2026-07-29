package com.ephirious.controller;

import com.ephirious.dto.request.MatchCreateDto;
import com.ephirious.dto.request.PlayerNamePointDto;
import com.ephirious.dto.response.CompletedPaginationMatchDto;
import com.ephirious.dto.response.CreatedMatchDto;
import com.ephirious.dto.response.MatchStatusDto;
import com.ephirious.model.value.PlayerName;
import com.ephirious.service.MatchOrchestrator;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping("/matches")
@RequiredArgsConstructor
public class MatchController {
    private static final String DEFAULT_PAGE = "1";
    private static final String DEFAULT_PLAYER_NAME = "";

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

    @GetMapping
    public CompletedPaginationMatchDto getCompletedMatches(
            @RequestParam(defaultValue = DEFAULT_PAGE)  int page,
            @RequestParam(defaultValue = DEFAULT_PLAYER_NAME) String playerName
    ) {
        ensurePositivePageValue(page);
        if (Objects.equals(playerName, DEFAULT_PLAYER_NAME)) {
            return matchOrchestrator.getCompletedMatches(page);
        }
        return matchOrchestrator.getCompletedMatchesByName(page, PlayerName.of(playerName));
    }


    private void ensurePositivePageValue(int page) {
        if (page < 0) {
            throw new IllegalStateException("The page value must not be negative or equal zero");
        }
    }
}
