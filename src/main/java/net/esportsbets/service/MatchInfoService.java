package net.esportsbets.service;

import net.esportsbets.dao.BettableMatchesdao;
import net.esportsbets.dao.Matches;
import net.esportsbets.model.MatchResults;
import net.esportsbets.repository.BettableRepository;
import net.esportsbets.repository.MatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class MatchInfoService {

    @Autowired
    private MatchRepository matchRepository;

    public List<MatchResults> getPastMatches( int pageNumber ) {

        Pageable page = PageRequest.of(pageNumber, 10);
        List<Matches> matches = matchRepository.findByTimeIsBetweenOrderByTimeAsc(
                                                        new Timestamp( new java.util.Date().getTime() - 12*60*60*1000 ),
                                                        new Timestamp( new java.util.Date().getTime() ),
                                                        page);

        List<MatchResults> matchResults = matches.stream()
                                                    .map(MatchResults::mapMatchResults)
                                                    .collect(Collectors.toList());


        return matchResults;
    }

    public int getPastMatchesPageCount() {

        int matches = matchRepository.countByTimeIsBetweenOrderByTimeAsc(
                                                        new Timestamp( new java.util.Date().getTime() - 12*60*60*1000 ),
                                                        new Timestamp( new java.util.Date().getTime() ));
        return matches/10;
    }

    @Autowired
    private BettableRepository bettableRepository;

    public List<BettableMatchesdao> getBettableMatches() {

//        return StreamSupport.stream(bettableRepository.findAll().spliterator(), false)
//                .collect(Collectors.toList());

        return (List<BettableMatchesdao>) bettableRepository.findAll();

    };


}
