package robert.db.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import robert.db.entity.Appliance;
import robert.db.entity.Building;
import robert.db.entity.Reservation;
import robert.db.entity.Room;
import robert.db.repository.ApplianceRepository;
import robert.db.repository.BuildingRepository;
import robert.db.repository.ReservationRepository;
import robert.db.repository.RoomRepository;
import robert.exceptions.NoSuchBuildingException;
import robert.utils.api.AppLogger;
import robert.web.svc.rest.responses.asm.RoomAssembler;
import robert.web.svc.rest.responses.data.RoomDR;

import java.util.Collections;
import java.util.List;

@Component
@Transactional
public class ApplianceBuildingRoomManagementDao {

	private final AppLogger log;

	private final RoomRepository roomRepository;

	private final ApplianceRepository applianceRepository;

	private final BuildingRepository buildingRepository;

	private final ReservationRepository reservationRepository;

	@Autowired
	public ApplianceBuildingRoomManagementDao(AppLogger log, RoomRepository roomRepository,
											  ApplianceRepository applianceRepository,
											  BuildingRepository buildingRepository, ReservationRepository reservationRepository) {
		this.log = log;
		this.roomRepository = roomRepository;
		this.applianceRepository = applianceRepository;
		this.buildingRepository = buildingRepository;
		this.reservationRepository = reservationRepository;
	}

	public Room saveRoom(Room room) {
		room.setNumber(room.getNumber().trim().toLowerCase());
		return roomRepository.save(room);
	}

	public Room findRoomById(long roomId) {
		return roomRepository.findOne(roomId);
	}

	public List<RoomDR> findAllRoomsInBuilding(String buildingName) {
		Building building = buildingRepository.findOneByName(buildingName.trim().toLowerCase());
		if (building == null) {
			return Collections.emptyList();
		}
		return RoomAssembler.convertToRoomDR(building.getRooms());
	}

	public void addApplianceToTheRoom(Appliance appl, String building, String roomNumber) throws NoSuchBuildingException {
		// TODO: select with hql query
		building = building.trim().toLowerCase();
		Building b = buildingRepository.findOneByName(building);
		if (b == null) {
			log.debug("Could not find building:", building);
			throw new NoSuchBuildingException("Could not find building: " + building);
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

	public Appliance findApplianceById(long id) {
		return applianceRepository.findOne(id);
	}

	public Appliance saveAppliance(Appliance appliance) {
		return applianceRepository.save(appliance);
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
		building.setName(building.getName());
		return buildingRepository.save(building);
	}

	public List<Reservation> findAllReservations() {
		return (List<Reservation>) reservationRepository.findAll();
	}

}
