package net.esportsbets.service;

import net.esportsbets.model.CustomPlayerStats;
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

    public List<CustomPlayerStats> loadPlayersBySearchString(
            String searchString) {
        Pageable topTen = PageRequest.of(0, 10);
        List<CustomPlayerStats> players = (
                playersRepo.findByGamertagStartingWithIgnoreCase(
                        searchString, topTen));
        if (players == null) {
            return new ArrayList<>();
        }
        return players;
    }

    public List<CustomPlayerStats> loadPlayersByGamertags(String[] gamertags) {
        List<CustomPlayerStats> players = playersRepo.findByGamertagIn(
                gamertags);
        if (players == null) {
            return new ArrayList<>();
        }
        return players;
    }

    public List<CustomPlayerStats> loadTopPlayers(String attribute, Integer limit) {
        List<CustomPlayerStats> players = new ArrayList<>();
        Pageable playerLimit = PageRequest.of(0, limit);
        switch (attribute) {
            case "kills":
                return playersRepo.findTopPlayersByKills(playerLimit);
            case "deaths":
                return playersRepo.findTopPlayersByDeaths(playerLimit);
            case "assists":
                return playersRepo.findTopPlayersByAssists(playerLimit);
            case "weaponDamage":
                return playersRepo.findTopPlayersByWeaponDamage(playerLimit);
            case "kda":
                return  playersRepo.findTopPlayersByKDA(playerLimit);
            case "winPercentage":
                return playersRepo.findTopPlayersByWinPercentage(playerLimit);
            case "accuracy":
                return playersRepo.findTopPlayersByAccuracy(playerLimit);
            case "perfectKill":
                return playersRepo.findTopPlayersByPerfectKill(playerLimit);
            case "powerWeaponKills":
                return playersRepo.findTopPlayersByPowerWeaponKills(playerLimit);
            default:
                return new ArrayList<>();
        }
    }
}
