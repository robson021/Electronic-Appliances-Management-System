package robert.db.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import robert.db.entity.Room;

@Repository
public interface RoomRepository extends CrudRepository<Room, Long> {

	Room findRoomByNumberAndBuilding(String number, String building);
}
