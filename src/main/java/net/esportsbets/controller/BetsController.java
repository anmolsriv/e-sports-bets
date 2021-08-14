package net.esportsbets.controller;

import net.esportsbets.model.UserBetRequestModel;
import net.esportsbets.model.UserBetsResponse;
import net.esportsbets.service.BetsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/bets")
public class BetsController {

    @Autowired
    private BetsService betsService;

    @RequestMapping(value = "/user_credits", method = RequestMethod.GET)
    @ResponseBody
    public Double getUserCredits(HttpServletRequest httpRequest) {

        return betsService.getUserCredits( httpRequest.getUserPrincipal().getName() );
    }

    @RequestMapping(value = "/place_bet", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> getResultMatches(@RequestBody UserBetRequestModel bet, HttpServletRequest httpRequest) {

        return betsService.placeBets( bet, httpRequest.getUserPrincipal().getName() );
    }

    @RequestMapping(value = "/user_bets", method = RequestMethod.GET)
    @ResponseBody
    public List<UserBetsResponse> getUserMatches(HttpServletRequest httpRequest) {

        return betsService.getBetsForUser( httpRequest.getUserPrincipal().getName() );
    }
}
