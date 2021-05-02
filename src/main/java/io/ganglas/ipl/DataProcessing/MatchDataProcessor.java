package io.ganglas.ipl.DataProcessing;

import java.time.LocalDate;


import org.springframework.batch.item.ItemProcessor;

import io.ganglas.ipl.Model.Match;

public class MatchDataProcessor implements ItemProcessor<MatchInput, Match> {

    // private static final Logger log = LoggerFactory.getLogger(MatchDataProcessor.class);

    @Override
    public Match process(final MatchInput matchInput) throws Exception {

        final Match match=new Match();
        match.setId((Long.parseLong(matchInput.getId())));
        match.setCity(matchInput.getCity());
        match.setDate(LocalDate.parse(matchInput.getDate()));
        match.setPlayerOfMatch(matchInput.getPlayer_of_match());
        match.setVenue(matchInput.getVenue());
        if("bat".equals(matchInput.getToss_decision())){
            match.setTeam1(matchInput.getToss_winner());
            match.setTeam2(matchInput.getToss_winner().equals(matchInput.getTeam1())?matchInput.getTeam2():matchInput.getTeam1());
        }else
        {
            match.setTeam2(matchInput.getToss_winner());
            match.setTeam1(matchInput.getToss_winner().equals(matchInput.getTeam1())?matchInput.getTeam2():matchInput.getTeam1());
        }
        match.setTossWinner(matchInput.getToss_winner());
        match.setTossDecision(matchInput.getToss_decision());
        match.setMatchWinner(matchInput.getWinner());
        match.setResult(matchInput.getResult());
        match.setResultMargin(matchInput.getResult_margin());
        match.setUmpire1(matchInput.getUmpire1());
        match.setUmpire2(matchInput.getUmpire2());
        return match;
    }

}