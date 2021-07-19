package net.esportsbets.service;

import net.esportsbets.dao.Players;
import net.esportsbets.repository.PlayersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PlayerInfoService {
    @Autowired
    private PlayersRepository playersRepo;

    public List<Players> loadPlayersBySearchString(String searchString) {
        Pageable topTen = PageRequest.of(0, 10);
        List<Players> players = playersRepo.findByGamertagStartingWithIgnoreCase(searchString, topTen);
        if (players == null) {
            return new ArrayList<Players>();
        }
        return players;
    }

    public List<Players> loadPlayersByGamertags(String[] gamertags) {
        List<Players> players = playersRepo.findByGamertagIn(gamertags);
        if (players == null) {
            return new ArrayList<Players>();
        }
        return players;
    }
}