package net.esportsbets.model;

import org.springframework.beans.factory.annotation.Value;

public interface CustomPlayerStats {
    String getGamertag();

    Integer getKills();

    Integer getDeaths();

    Integer getAssists();

    Integer getWeaponDamage();

    default double getKDA() {
        if (getDeaths() == 0) {
            return 0.0;
        }
        return (getKills() + getAssists() / 3.0) / getDeaths();
    }

    @Value("#{target.gamesWon * 100/ (target.gamesWon + target.gamesLost +" +
            "0.000001)}")
    double getWinPercentage();

    @Value("#{target.shotsLanded * 100 / (target.shotsFired +" +
            "target.shotsLanded + 0.000001)}")
    double getAccuracy();

    Integer getPerfectKill();

    Integer getPowerWeaponKills();
}
