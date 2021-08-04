package net.esportsbets.service;

import net.esportsbets.dao.MatchGamertagLink;
import net.esportsbets.repository.MatchGamertagLinkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MatchGamertagLinkService {

    @Autowired
    private MatchGamertagLinkRepository gamertagLinkRepo;

    public List<MatchGamertagLink> loadGamertagsByMatchIdAndTeam(
            String matchId, Integer teamId) {
        List<MatchGamertagLink> gamertags = (
                gamertagLinkRepo.findGamertagByMatchIdAndTeamId(matchId, teamId));
        if (gamertags == null) {
            return new ArrayList<>();
        }
        return gamertags;
    }
}
