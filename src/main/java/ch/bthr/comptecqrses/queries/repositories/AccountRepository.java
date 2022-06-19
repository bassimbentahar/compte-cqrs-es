package ch.bthr.comptecqrses.queries.repositories;

import ch.bthr.comptecqrses.queries.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account,String> {
}
