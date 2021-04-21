package io.webapp.ipldashboard.data;

import java.time.LocalDate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import io.webapp.ipldashboard.model.Match;

public class MatchDataProcessor implements ItemProcessor<MatchInput, Match> {
    //private static final Logger log = LoggerFactory.getLogger(MatchDataProcessor.class);

    @Override
    public Match process(final MatchInput matchInput) throws Exception {
        Match match = new Match();
        match.setId(Long.parseLong(matchInput.getId().trim()));
        match.setDate(LocalDate.parse(matchInput.getDate()));
        match.setCity(matchInput.getCity());
        match.setMatchWinner(matchInput.getWinner());
        match.setPlayerOfMatch(matchInput.getPlayer_of_match());
        match.setResult(matchInput.getResult());
        match.setResultMargin(matchInput.getResult_margin());
        
        //set Team1 and Team2 depending on Innings Order
        String firstInningsTeam , secondInningsTeam;

        if("bat".equals(matchInput.getToss_decision())){
            firstInningsTeam = matchInput.getToss_winner();
            secondInningsTeam = matchInput.getTeam1().equals(matchInput.getToss_winner())?matchInput.getTeam2():matchInput.getTeam1();
        }else{
            secondInningsTeam=matchInput.getToss_winner();
            firstInningsTeam=matchInput.getToss_winner().equals(matchInput.getTeam1())?matchInput.getTeam2():matchInput.getTeam1();
        }
        match.setFirstInningsTeam(firstInningsTeam);
        match.setSecondInningsTeam(secondInningsTeam);
        match.setTossDecision(matchInput.getToss_decision());
        match.setTossWinner(matchInput.getToss_winner());
        match.setUmpire1(matchInput.getUmpire1());
        match.setUmpire2(matchInput.getUmpire2());
        match.setVenue(matchInput.getVenue());
        return match;
    }

}
