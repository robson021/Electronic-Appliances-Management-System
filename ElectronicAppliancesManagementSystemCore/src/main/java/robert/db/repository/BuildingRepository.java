package robert.db.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import robert.db.entity.Building;

@Repository
public interface BuildingRepository extends CrudRepository<Building, Long> {
	Building findOneByName(String name);
}
