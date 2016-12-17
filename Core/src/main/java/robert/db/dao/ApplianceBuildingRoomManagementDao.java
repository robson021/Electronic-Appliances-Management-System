package robert.db.dao;

import javassist.NotFoundException;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import robert.db.entity.Appliance;
import robert.db.entity.Building;
import robert.db.entity.Reservation;
import robert.db.entity.Room;
import robert.db.repository.ApplianceRepository;
import robert.db.repository.BuildingRepository;
import robert.db.repository.ReservationRepository;
import robert.db.repository.RoomRepository;
import robert.enums.Validation;
import robert.exceptions.NoSuchBuildingException;
import robert.utils.api.AppLogger;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.*;

@SuppressWarnings({"WeakerAccess", "SpringJavaAutowiringInspection"})
@Component
@Transactional
public class ApplianceBuildingRoomManagementDao {

	private final AppLogger log;

	private final RoomRepository roomRepository;

	private final ApplianceRepository applianceRepository;

	private final BuildingRepository buildingRepository;

	private final ReservationRepository reservationRepository;

	private final EntityManager em;

	@Autowired
	public ApplianceBuildingRoomManagementDao(AppLogger log, RoomRepository roomRepository, ApplianceRepository applianceRepository,
											  BuildingRepository buildingRepository, ReservationRepository reservationRepository, EntityManager em) {
		this.log = log;
		this.roomRepository = roomRepository;
		this.applianceRepository = applianceRepository;
		this.buildingRepository = buildingRepository;
		this.reservationRepository = reservationRepository;
		this.em = em;
	}

	public Room saveRoom(Room room) {
		room.setNumber(room.getNumber()
				.trim()
				.toLowerCase());
		return roomRepository.save(room);
	}

	public Room findRoomById(long roomId) {
		return roomRepository.findOne(roomId);
	}

	public Set<Room> findAllRoomsInBuilding(String buildingName) {
		Building building = buildingRepository.findOneByName(buildingName.trim()
				.toLowerCase());
		if (building == null) {
			return Collections.emptySet();
		}
		return building.getRooms();
	}

	public void addApplianceToTheRoom(Appliance appl, String building, String roomNumber) throws NoSuchBuildingException {
		// TODO: select with hql query
		building = building.trim()
				.toLowerCase();
		Building b = buildingRepository.findOneByName(building);
		if (b == null) {
			log.debug("Could not find building:", building);
			throw new NoSuchBuildingException("Could not find building: " + building);
		}
		b.getRooms()
				.stream()
				.filter(r -> r.getNumber()
						.equalsIgnoreCase(roomNumber))
				.findFirst()
				.ifPresent(room -> {
					room.addNewAppliance(appl);
					appl.setRoom(room);
					buildingRepository.save(b);
				});
	}

	@SuppressWarnings({"unchecked", "JpaQlInspection"})
	public List<String> findAllBuildingNumbers() {
		List resultList = em.createQuery("select b.name from Building b")
				.getResultList();
		if (CollectionUtils.isEmpty(resultList)) {
			return Collections.emptyList();
		}
		return resultList;
	}

	public Iterable<Appliance> findAllAppliances() {
		return applianceRepository.findAll();
	}

	public Appliance findApplianceById(long id) {
		return applianceRepository.findOne(id);
	}

	public Appliance findApplianceByUniqueCode(String code) {
		return applianceRepository.findOneByUniqueCode(code);
	}

	public Appliance saveAppliance(Appliance appliance) {
		return applianceRepository.save(appliance);
	}

	public String addApplianceToTheRoom(long roomId, String applianceName) throws Exception {
		Appliance appliance = new Appliance();
		appliance.setName(applianceName);
		final String uniqueCode = UUID.randomUUID()
				.toString();
		appliance.setUniqueCode(uniqueCode);
		Room room = roomRepository.findOne(roomId);
		this.addApplianceToTheRoom(appliance, room);
		return uniqueCode;
	}

	public void addApplianceToTheRoom(Appliance appl, Room room) throws Exception {
		if (room == null || appl == null) {
			throw new Exception("Room or appliance is null.");
		}

		appl.setRoom(room);
		room.addNewAppliance(appl);
		roomRepository.save(room);
	}

	public Collection<Appliance> getAllAppliancesInRoom(long roomId) {
		return roomRepository.findOne(roomId)
				.getAppliances();
	}

	public void addNewRoomToTheBuilding(String building, String roomNum) throws NotFoundException {
		Building b = this.findBuildingByName(building);
		validateRoomOrBuilding(b, building);
		Room room = new Room();
		room.setBuilding(b);
		room.setNumber(roomNum);
		b.addRoom(room);
		buildingRepository.save(b);
	}

	public Building saveBuilding(Building building) {
		return buildingRepository.save(building);
	}

	public Building findBuildingByName(String buildingName) {
		return buildingRepository.findOneByName(buildingName.trim()
				.toLowerCase());
	}

	public List<Building> findAllBuildings() {
		return (List<Building>) buildingRepository.findAll();
	}

	public Building saveBuilding(String name) {
		Building building = new Building();
		building.setName(name);
		return buildingRepository.save(building);
	}

	public List<Reservation> findAllReservations() {
		return (List<Reservation>) reservationRepository.findAll();
	}

	public Reservation findReservation(long id) {
		return reservationRepository.findOne(id);
	}

	public Reservation getReservationByToken(String token) {
		return reservationRepository.findOneByAccessToken(token);
	}

	public int cleanOldReservations() {
		long expirationTime = new DateTime()
				.minusDays(Validation.TIME_OF_KEEPING_OLD_RESERVATION_IN_DAYS)
				.toDate()
				.getTime();

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Reservation> query = cb.createQuery(Reservation.class);

		Root<Reservation> reservationEntity = query.from(Reservation.class);
		CriteriaQuery<Reservation> criteriaQuery = query
				.select(reservationEntity)
				.where(cb.lessThan(reservationEntity.get("validTill"), expirationTime));

		List<Reservation> resultList = em.createQuery(criteriaQuery)
				.getResultList();
		if (CollectionUtils.isEmpty(resultList)) {
			return 0;
		}
		int reservationsToDelete = resultList.size();
		reservationRepository.delete(resultList);
		return reservationsToDelete;
	}

	private void validateRoomOrBuilding(Object o, String name) throws NotFoundException {
		if (o == null) {
			throw new NotFoundException("Building or room cannot be found: " + name);
		}
	}

}
