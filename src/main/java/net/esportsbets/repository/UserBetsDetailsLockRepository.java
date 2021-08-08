package net.esportsbets.repository;

import net.esportsbets.dao.UserBetsDetailsLock;
import net.esportsbets.dao.UserBetsDetailsLockId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Collection;
import java.util.List;

public interface UserBetsDetailsLockRepository extends JpaRepository<UserBetsDetailsLock, UserBetsDetailsLockId> {

    List<UserBetsDetailsLock> findByUserIdEqualsAndMatchIdIn(Long userId, Collection<String> matchIds);

}