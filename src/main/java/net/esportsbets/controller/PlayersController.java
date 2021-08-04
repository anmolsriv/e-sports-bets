package net.esportsbets.controller;

import net.esportsbets.model.CustomPlayerStats;
import net.esportsbets.service.PlayerInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class PlayersController {
    @Autowired
    private PlayerInfoService playerService;

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

    @RequestMapping(value = "/top_players", method = RequestMethod.GET)
    @ResponseBody
    public List<CustomPlayerStats> getTopPlayers(
            @RequestParam("attribute") String attribute,
            @RequestParam("limit") Integer limit) {
        return playerService.loadTopPlayers(attribute, limit);
    }

}
