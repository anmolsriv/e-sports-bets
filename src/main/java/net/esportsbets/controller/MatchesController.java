package net.esportsbets.controller;

import net.esportsbets.dao.Matches;
import net.esportsbets.model.MatchResults;
import net.esportsbets.repository.MatchRepository;
import net.esportsbets.service.MatchInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/matches")
public class MatchesController {

    @Autowired
    private MatchInfoService matchInfoService;

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
}