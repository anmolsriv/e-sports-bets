package net.esportsbets.repository;

import net.esportsbets.dao.UserTransactionHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserTransactionHistoryRepository extends JpaRepository<UserTransactionHistory, Long> {
}
