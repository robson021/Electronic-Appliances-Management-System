package robert.db.dao;

import org.apache.commons.lang3.RandomStringUtils;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import robert.SpringTest;
import robert.db.entity.Room;

public class ApplianceRoomManagementDaoTest extends SpringTest {

	@Autowired
	private ApplianceRoomManagementDao dao;

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void saveRoom() throws Exception {
		String roomNO = RandomStringUtils.random(5).toLowerCase();
		Room room = new Room();
		room.setNumber(roomNO);
		long id = dao.saveRoom(room).getId();

		Room roomById = dao.findRoomById(id);
		Assertions.assertThat(roomById)
				.isNotNull()
				.hasFieldOrPropertyWithValue("number", roomNO);
	}

	@Test
	public void addApplianceToTheRoom() throws Exception {

	}

	@Test
	public void getAllAppliances() throws Exception {

	}

	@Test
	public void addApplianceToTheRoom1() throws Exception {

	}

	@Test
	public void saveBuilding() throws Exception {

	}

}