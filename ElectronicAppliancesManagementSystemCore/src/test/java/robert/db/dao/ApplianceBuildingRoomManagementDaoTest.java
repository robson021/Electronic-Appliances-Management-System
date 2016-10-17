package robert.db.dao;

import org.apache.commons.lang3.RandomStringUtils;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import robert.db.entity.Appliance;
import robert.db.entity.Building;
import robert.db.entity.Room;
import utils.SpringTest;

public class ApplianceBuildingRoomManagementDaoTest extends SpringTest {

	@Autowired
	private ApplianceBuildingRoomManagementDao dao;

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
	public void getAllAppliances() throws Exception {
		Appliance a1 = new Appliance();
		a1.setName("a1");
		Appliance a2 = new Appliance();
		a2.setName("a2");
		Appliance a3 = new Appliance();
		a3.setName("a3");

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

	}

	private Room createRoom(int roomNumberLength) {
		String roomNO = RandomStringUtils.random(roomNumberLength).toLowerCase();
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