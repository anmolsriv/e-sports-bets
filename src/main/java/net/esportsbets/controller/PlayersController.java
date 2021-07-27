package net.esportsbets.controller;

import net.esportsbets.model.CustomPlayerStats;
import net.esportsbets.model.MatchOddsRequest;
import net.esportsbets.model.MatchOddsResponse;
import net.esportsbets.service.OddsModelingService;
import net.esportsbets.service.PlayerInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
public class PlayersController {
    @Autowired
    private PlayerInfoService playerService;

    @Autowired
    private OddsModelingService oddService;

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
            @RequestParam("gamertags") String[] gamertags) {
        return playerService.loadPlayersByGamertags(gamertags);
    }

    @RequestMapping(value = "/odds", method = RequestMethod.POST)
    @ResponseBody
    public List<MatchOddsResponse> getOddsForMatches(
            @RequestBody List<MatchOddsRequest> matches
            ) {
        return null;
        //return oddService.loadOddsForMatches(matches);
    }

}
