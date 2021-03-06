package robert.db.dao;

import java.util.Collection;
import java.util.UUID;

import org.apache.commons.lang3.RandomStringUtils;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;

import robert.db.entity.Appliance;
import robert.db.entity.Building;
import robert.db.entity.Room;
import robert.db.repository.UserRepository;
import utils.SpringTest;
import utils.TestUtils;

public class ApplianceBuildingRoomManagementDaoTest extends SpringTest {

	@Autowired
	private ApplianceBuildingRoomManagementDao dao;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserDao userDao;

	@Override
	public void setup() throws Exception {
	}

	@Test
	public void saveRoom() throws Exception {
		Room room = createRoom(5);
		String roomNo = room.getNumber();
		long id = dao.saveRoom(room).getId();

		Room roomById = dao.findRoomById(id);
		Assertions.assertThat(roomById)
				.isNotNull()
				.hasFieldOrPropertyWithValue("number", roomNo);
	}

	@Test
	public void addApplianceToTheRoom() throws Exception {
		Room room = createRoom(6);
		Building building = createBuilding(6);
		Appliance appliance = new Appliance();
		appliance.setUniqueCode(UUID.randomUUID().toString());

		building.addRoom(room);
		room.setBuilding(building);
		dao.saveBuilding(building);

		String applName = "Test Appliance";
		appliance.setName(applName);

		dao.addApplianceToTheRoom(appliance, building.getName(), room.getNumber());
		final boolean[] found = {false};
		dao.findAllAppliances().forEach(appl -> {
			if (appl.getName().equals(applName))
				found[0] = true;
		});

		if (!found[0])
			throw new Exception(applName + " not found.");
	}

	@Test
	@DirtiesContext
	public void getAllAppliances() throws Exception {
		Appliance a1 = new Appliance();
		a1.setName("a1");
		Appliance a2 = new Appliance();
		a2.setName("a2");
		Appliance a3 = new Appliance();
		a3.setName("a3");

		a1.setUniqueCode(UUID.randomUUID().toString());
		a2.setUniqueCode(UUID.randomUUID().toString() + "a");
		a3.setUniqueCode(UUID.randomUUID().toString() + "bb");

		Room room = dao.saveRoom(createRoom(7));
		dao.addApplianceToTheRoom(a1, room);
		dao.addApplianceToTheRoom(a2, room);
		dao.addApplianceToTheRoom(a3, room);

		Assertions.assertThat(dao.findAllAppliances())
				.isNotNull()
				.contains(a1, a2, a3);
	}

	@Test
	public void saveBuilding() throws Exception {
		Building building = TestUtils.generateRandomBuilding();
		final String name = dao.saveBuilding(building).getName();
		Assertions.assertThat(dao.findBuildingByName(name))
				.isNotNull();
	}

	@Test
	public void getAllAppliancesInRoom() throws Exception {
		final int numOfAppl = 5;
		Room room = TestUtils.generateRandomRoom();
		for (int i = 0; i < numOfAppl; i++) {
			Appliance appl = TestUtils.generateRandomAppliance();
			room.addNewAppliance(appl);
			appl.setRoom(room);
		}

		final long roomId = dao.saveRoom(room)
				.getId();

		Collection<Appliance> appls = dao.getAllAppliancesInRoom(roomId);
		Assertions.assertThat(appls)
				.isNotNull()
				.hasSize(numOfAppl);
	}

    @Test
    public void runQueryTest() {
        dao.getReservationsFromThePast(23);
    }

	private Room createRoom(int roomNumberLength) {
		String roomNO = RandomStringUtils.random(roomNumberLength, false, true).toLowerCase().trim();
		Room room = new Room();
		room.setNumber(roomNO);
		return room;
	}

	private Building createBuilding(int buildingNameLength) {
		Building building = new Building();
		building.setName(RandomStringUtils.random(buildingNameLength).toLowerCase());
		return building;
	}

}