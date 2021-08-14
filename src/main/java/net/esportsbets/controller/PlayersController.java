package net.esportsbets.controller;

import net.esportsbets.dao.MatchGamertagLink;
import net.esportsbets.model.CustomPlayerStats;
import net.esportsbets.service.MatchGamertagLinkService;
import net.esportsbets.service.PlayerInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/players")
public class PlayersController {
    @Autowired
    private PlayerInfoService playerService;

    @Autowired
    private MatchGamertagLinkService gamertagLinkService;

    @RequestMapping(value = "/player_search/{search_string}",
            method = RequestMethod.GET)
    @ResponseBody
    public List<CustomPlayerStats> getPlayersMatchingSearchString(
            @PathVariable("search_string") String searchString) {
        return playerService.loadPlayersBySearchString(searchString);
    }

    @RequestMapping(value = "/player_stats", method = RequestMethod.GET)
    @ResponseBody
    public List<CustomPlayerStats> getPlayersInGamertagList(
            @RequestParam("gamertags") List<String> gamertags) {
        return playerService.loadPlayersByGamertags(gamertags);
    }

    @RequestMapping(value = "/top_players", method = RequestMethod.GET)
    @ResponseBody
    public List<CustomPlayerStats> getTopPlayers(
            @RequestParam("attribute") String attribute,
            @RequestParam("limit") Integer limit) {
        return playerService.loadTopPlayers(attribute, limit);
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
