package com.ephirious.dto.response;

import com.ephirious.model.aggregate.Match;
import com.ephirious.model.entity.Player;
import com.ephirious.model.value.score.game.AbstractGameScore;
import com.ephirious.model.value.score.game.FinalStandardGameScore;
import com.ephirious.model.value.score.game.StandardGameScore;
import com.ephirious.model.value.score.game.TieBreakGameScore;
import com.ephirious.model.value.score.match.AbstractMatchScore;
import com.ephirious.model.value.score.set.AbstractSetScore;

import java.util.Objects;

public record MatchStatusDto(
        PlayerPointsMatchDto firstPlayer,
        PlayerPointsMatchDto secondPlayer,
        String winnerName
) {

    public static MatchStatusDto fromMatch(Match match, Player first, Player second, Player winner) {
        return new MatchStatusDto(
                map(match, first),
                map(match, second),
                winner != null ? winner.name().value() : null
        );
    }

    private static PlayerPointsMatchDto map(Match match, Player player) {
        AbstractMatchScore matchScore = match.score();
        AbstractSetScore setScore = matchScore.currentSet();
        AbstractGameScore<?> gameScore = setScore.currentGame();
        boolean isFirst = Objects.equals(match.firstPlayerId(), player.id());

        int sets = isFirst ? matchScore.firstPlayerScore() : matchScore.secondPlayerScore();
        int games = isFirst ? setScore.firstPlayerScore() : setScore.secondPlayerScore();

        String pointsAsString = getStringPoints(gameScore, isFirst);
        Integer pointsAsInt = getIntegerPoints(gameScore, isFirst);

        return new PlayerPointsMatchDto(
                player.name().value(),
                pointsAsString,
                games,
                sets,
                pointsAsInt
        );
    }

    private static String getStringPoints(AbstractGameScore<?> gameScore, boolean isFirst) {
        return switch (gameScore) {
            case FinalStandardGameScore _, StandardGameScore _-> isFirst
                    ? (String) gameScore.firstPlayerScore()
                    : (String) gameScore.secondPlayerScore();
            case  TieBreakGameScore _ -> null;
            default -> throw new IllegalStateException("Unavailable GameScore implementation");
        };
    }

    private static Integer getIntegerPoints(AbstractGameScore<?> gameScore, boolean isFirst) {
        return switch (gameScore) {
            case TieBreakGameScore tie -> isFirst
                    ? tie.firstPlayerScore()
                    : tie.secondPlayerScore();
            case FinalStandardGameScore _, StandardGameScore _ -> null;
            default -> throw new IllegalStateException("Unavailable GameScore implementation");
        };
    }
}
