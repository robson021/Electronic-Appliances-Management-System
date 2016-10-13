package robert.db.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import robert.db.entity.Appliance;
import robert.db.entity.Room;
import robert.db.repository.ApplianceRepository;
import robert.db.repository.RoomRepository;
import robert.exception.NoSuchRoomException;

@Component
@Transactional
public class ApplianceRoomManagementDao {

	private final RoomRepository roomRepository;

	private final ApplianceRepository applianceRepository;

	@Autowired
	public ApplianceRoomManagementDao(RoomRepository roomRepository, ApplianceRepository applianceRepository) {
		this.roomRepository = roomRepository;
		this.applianceRepository = applianceRepository;
	}

	public Room addNewRoom(Room room) {
		return roomRepository.save(room);
	}

	public void addApplianceToTheRoom(Appliance appl, String building, String roomNumber) throws NoSuchRoomException {
		Room room = roomRepository.findRoomByNumberAndBuilding(roomNumber, building);
		if (room == null) {
			throw new NoSuchRoomException(roomNumber, building);
		}

		appl.setRoom(room);
		room.addNewAppliance(appl);

		roomRepository.save(room);
	}

	public Iterable<Appliance> getAllAppliances() {
		return applianceRepository.findAll();
	}

	public void addApplianceToTheRoom(Appliance appl, Room room) throws Exception {
		if (room == null || appl == null) {
			throw new Exception("Room or appliance is null.");
		}

		appl.setRoom(room);
		room.addNewAppliance(appl);
		roomRepository.save(room);
	}

}
