package ch.bthr.comptecqrses.queries.repositories;

import ch.bthr.comptecqrses.queries.entities.Operation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OperationRepository extends JpaRepository<Operation, Long> {
}
