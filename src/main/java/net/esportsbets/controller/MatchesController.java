package net.esportsbets.controller;

import net.esportsbets.dao.MatchGamertagLink;
import net.esportsbets.model.BettableMatches;
import net.esportsbets.model.CustomPlayerStats;
import net.esportsbets.model.MatchResults;
import net.esportsbets.service.MatchGamertagLinkService;
import net.esportsbets.service.MatchInfoService;
import net.esportsbets.service.PlayerInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/matches")
public class MatchesController {

    @Autowired
    private MatchInfoService matchInfoService;

    @Autowired
    private MatchGamertagLinkService gamertagLinkService;

    @Autowired
    private PlayerInfoService playerService;

    @RequestMapping(value = "/results", method = RequestMethod.GET)
    @ResponseBody
    public List<MatchResults> getResultMatches(@RequestParam("page") int page) {
        return matchInfoService.getPastMatches( page );
    }

    @RequestMapping(value = "/results/count", method = RequestMethod.GET)
    @ResponseBody
    public Integer getResultMatchesCount() {
        return matchInfoService.getPastMatchesPageCount();
    }

    @RequestMapping(value = "/bettable", method = RequestMethod.GET)
    @ResponseBody
    public List<BettableMatches> getBettableMatches() {
        return matchInfoService.getBettableMatches();
    }

    @RequestMapping(value = "/team_stats", method = RequestMethod.GET)
    @ResponseBody
    public List<String> getTeamStatistics(
            @RequestParam("matchId") String matchId,
            @RequestParam("teamId") Integer teamId) {
        List<MatchGamertagLink> gamertagLinks = gamertagLinkService.loadGamertagsByMatchIdAndTeam(matchId, teamId);
        return gamertagLinks.stream().map(g -> g.getGamertag())
                .collect(Collectors.toList());
    }
}
