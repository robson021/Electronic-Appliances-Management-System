package robert.db.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import robert.db.entity.Reservation;

@Repository
public interface ReservationRepository extends CrudRepository<Reservation, Long> {
}
