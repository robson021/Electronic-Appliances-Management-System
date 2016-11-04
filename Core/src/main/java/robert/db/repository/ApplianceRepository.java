package robert.db.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import robert.db.entity.Appliance;

@Repository
public interface ApplianceRepository extends CrudRepository<Appliance, Long> {
	Appliance findOneByUniqueCode(String uniqueCode);
}
