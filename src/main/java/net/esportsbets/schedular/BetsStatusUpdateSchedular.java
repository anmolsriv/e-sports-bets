package net.esportsbets.schedular;

import net.esportsbets.dao.UserBets;
import net.esportsbets.repository.hibernate.BetsHibernateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class BetsStatusUpdateSchedular {

    @Autowired
    private BetsHibernateRepository betsHibernateRepository;

    @Scheduled(cron = "0 1/30 * 1/1 * ? *")
    public void computeBetResults() {

        List<UserBets> pendingBets = betsHibernateRepository.customMatchSearchQuery( new Timestamp( new java.util.Date().getTime() ) );
    }
}
