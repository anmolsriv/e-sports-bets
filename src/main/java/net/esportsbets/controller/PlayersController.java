package net.esportsbets.controller;

import net.esportsbets.dao.Players;
import net.esportsbets.service.PlayerInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class PlayersController {
    @Autowired
    private PlayerInfoService playerService;

    @RequestMapping(value = "/player_search/{search_string}", method = RequestMethod.GET)
    @ResponseBody
    public List<Players> getPlayersMatchingSearchString(
            @PathVariable("search_string") String searchString) {
        return playerService.loadPlayersBySearchString(searchString);
    }

    @RequestMapping(value = "/player_stats", method = RequestMethod.GET)
    @ResponseBody
    public List<Players> getPlayersInGamertagList(
            @RequestParam("gamertags") String[] gamertags) {
        return playerService.loadPlayersByGamertags(gamertags);
    }
}
