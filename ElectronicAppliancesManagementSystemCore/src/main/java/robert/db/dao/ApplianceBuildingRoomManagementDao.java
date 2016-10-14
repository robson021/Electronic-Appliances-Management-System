package robert.db.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import robert.db.entity.Appliance;
import robert.db.entity.Building;
import robert.db.entity.Room;
import robert.db.repository.ApplianceRepository;
import robert.db.repository.BuildingRepository;
import robert.db.repository.RoomRepository;
import robert.utils.api.AppLogger;

@Component
@Transactional
public class ApplianceBuildingRoomManagementDao {

	private final AppLogger log;

	private final RoomRepository roomRepository;

	private final ApplianceRepository applianceRepository;

	private final BuildingRepository buildingRepository;

	@Autowired
	public ApplianceBuildingRoomManagementDao(AppLogger log, RoomRepository roomRepository,
											  ApplianceRepository applianceRepository,
											  BuildingRepository buildingRepository) {
		this.log = log;
		this.roomRepository = roomRepository;
		this.applianceRepository = applianceRepository;
		this.buildingRepository = buildingRepository;
	}

	public Room saveRoom(Room room) {
		room.setNumber(room.getNumber().trim().toLowerCase());
		return roomRepository.save(room);
	}

	public Room findRoomById(long roomId) {
		return roomRepository.findOne(roomId);
	}

	public void addApplianceToTheRoom(Appliance appl, String building, String roomNumber) {
		// TODO: select with hql query
		Building b = buildingRepository.findOneByName(building);
		if (b == null) {
			log.debug("Could not find building " + building);
			return;
		}
		b.getRooms()
				.stream()
				.filter(r -> r.getNumber().equalsIgnoreCase(roomNumber))
				.findFirst()
				.ifPresent(room -> {
					room.addNewAppliance(appl);
					appl.setRoom(room);
					buildingRepository.save(b);
				});
	}

	public Iterable<Appliance> findAllAppliances() {
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

	public Building saveBuilding(Building building) {
		building.setName(building.getName().trim().toLowerCase());
		return buildingRepository.save(building);
	}

}
