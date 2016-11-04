package robert.web.svc.rest;

import org.apache.commons.lang3.RandomStringUtils;
import org.assertj.core.api.Assertions;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import robert.db.dao.ApplianceBuildingRoomManagementDao;
import robert.db.dao.UserDao;
import robert.db.entity.Appliance;
import robert.db.entity.Building;
import robert.db.entity.Reservation;
import robert.db.entity.Room;
import robert.web.svc.rest.responses.data.ReservationDR;
import utils.SpringWebMvcTest;
import utils.TestUtils;

import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static robert.web.svc.rest.ctrl.api.BasicParams.BUILDING_NUMBER;
import static robert.web.svc.rest.ctrl.api.BasicParams.ROOM_NUMBER;
import static robert.web.svc.rest.ctrl.api.UserServiceCtrl.*;

public class UserServiceControllerTest extends SpringWebMvcTest {

	private static final String RESERVATION_URL = "/user-service/reservation/%s/";

	@Autowired
	private ApplianceBuildingRoomManagementDao abrmDao;

	@Autowired
	private UserDao userDao;

	@Override
	@Before
	public void setup() throws Exception {
		super.initMockMvc();
	}

	@Test
	public void makeReservation() throws Exception {

		int numOfReservations = abrmDao.findAllReservations()
				.size();
		userDao.saveUser(TestUtils.createAdminUser());

		// 1 - ok reservation
		Appliance appliance = TestUtils.generateRandomAppliance();
		long appId = abrmDao.saveAppliance(appliance)
				.getId();

		String url = String.format(RESERVATION_URL, appId);

		DateTime currentTime = new DateTime();
		ReservationDR reservation = new ReservationDR();
		reservation.setFrom(currentTime.toDate().getTime());
		reservation.setHours(3);
		String json = TestUtils.asJsonString(reservation);

		mockMvc.perform(post(url).content(json)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());

		Iterable<Reservation> allReservations = abrmDao.findAllReservations();
		Assertions.assertThat(allReservations)
				.hasSize(++numOfReservations);

		Appliance appl = abrmDao.findApplianceById(appId);
		Assertions.assertThat(appl.getReservations())
				.hasSize(1);

		// 2 - failed reservation
		reservation = new ReservationDR();
		reservation.setFrom(currentTime.minusHours(3).toDate().getTime());
		reservation.setHours(5);
		json = TestUtils.asJsonString(reservation);

		mockMvc.perform(post(url).content(json)
				.contentType(MediaType.APPLICATION_JSON));

		appl = abrmDao.findApplianceById(appId);
		Assertions.assertThat(appl.getReservations())
				.hasSize(numOfReservations);

		// 3 - ok reservation
		reservation = new ReservationDR();
		reservation.setFrom(currentTime.plusHours(10).toDate().getTime());
		reservation.setHours(7);
		json = TestUtils.asJsonString(reservation);

		mockMvc.perform(post(url).content(json)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());

		appl = abrmDao.findApplianceById(appId);
		Assertions.assertThat(appl.getReservations())
				.hasSize(++numOfReservations);
	}

	@Test
	public void getAllRoomsInBuilding() throws Exception {
		final int numRooms = 5;
		Building building = TestUtils.generateRandomBuildingWithRooms(numRooms);
		String buildingName = abrmDao.saveBuilding(building)
				.getName();

		final String url = GET_ALL_ROOMS_IN_BUILDING_URL.replace("{" + BUILDING_NUMBER + "}",
				String.valueOf(buildingName));

		final String response = mockMvc.perform(get(url))
				.andReturn()
				.getResponse()
				.getContentAsString();
		Assertions.assertThat(response)
				.isNotNull();

		for (Room room : building.getRooms()) {
			String roomNumber = room.getNumber();
			Assertions.assertThat(response)
					.containsIgnoringCase(roomNumber);
		}
	}

	@Test
	public void registerNewBuilding() throws Exception {
		final String name = RandomStringUtils.random(5, "qwertyuiop");
		final String url = REGISTER_NEW_BUILDING_URL.replace("{" + BUILDING_NUMBER + "}", name);

		int numBuildings = abrmDao.findAllBuildings().size();

		// 1st
		mockMvc.perform(put(url))
				.andExpect(status().isOk());

		Building building = abrmDao.findBuildingByName(name);

		Assertions.assertThat(building)
				.isNotNull();

		// 2nd - this should fail due to unique building name conflict
		mockMvc.perform(put(url));

		final int sizeAfterTwoRegistrations = abrmDao.findAllBuildings().size();
		Assertions.assertThat(sizeAfterTwoRegistrations)
				.isEqualTo(++numBuildings);
	}

	@Test
	public void addNewRoomToExistingBuilding() throws Exception {
		final int initialRoomNum = 3;
		final Building b = abrmDao
				.saveBuilding(TestUtils.generateRandomBuildingWithRooms(initialRoomNum));

		String roomNum = "122-test";
		final String url = REGISTER_NEW_ROOM_IN_BUILDING_URL
				.replace("{" + BUILDING_NUMBER + "}", b.getName())
				.replace("{" + ROOM_NUMBER + "}", roomNum);

		mockMvc.perform(put(url))
				.andExpect(status().isOk());

		Building building = abrmDao.findBuildingByName(b.getName());
		Assertions.assertThat(building)
				.isNotNull();
		Assertions.assertThat(building.getRooms().size())
				.isEqualTo(initialRoomNum + 1);
	}

	@Test
	public void registerNewApplianceInExistingRoom() throws Exception {
		final int numOfRooms = 5;
		Building building = abrmDao.saveBuilding(
				TestUtils.generateRandomBuildingWithRooms(numOfRooms)
		);

		building = abrmDao.findBuildingByName(building.getName());
		Assertions.assertThat(building.getRooms())
				.isNotEmpty()
				.hasSize(numOfRooms);

		long roomId = building.getRooms().iterator().next().getId();
		String applName = "Test appliance 12345";

		final String url = REGISTER_NEW_APPLIANCE_URL
				.replace("{room-id}", String.valueOf(roomId))
				.replace("{appl-name}", applName);

		String responseUuid = mockMvc.perform(put(url))
				.andExpect(status().isOk())
				.andReturn()
				.getResponse()
				.getContentAsString();

		final int anyRandomUuidLength = UUID.randomUUID().toString().length();

		Assertions.assertThat(responseUuid)
				.hasSize(anyRandomUuidLength);

		Appliance applianceByUniqueCode = abrmDao.findApplianceByUniqueCode(responseUuid);

		Assertions.assertThat(applianceByUniqueCode)
				.isNotNull()
				.hasNoNullFieldsOrPropertiesExcept("reservations")
				.hasFieldOrPropertyWithValue("name", applName)
				.hasFieldOrPropertyWithValue("uniqueCode", responseUuid);
	}

	@Test
	public void findAllBuildingNumbers() throws Exception {

		final int initBuildingCount = abrmDao.findAllBuildings().size();

		Building b1 = TestUtils.generateRandomBuilding();
		Building b2 = TestUtils.generateRandomBuildingWithRooms(3);
		Building b3 = TestUtils.generateRandomBuildingWithRooms(5);
		abrmDao.saveBuilding(b1);
		abrmDao.saveBuilding(b2);
		abrmDao.saveBuilding(b3);

		List<String> allBuildingNumbers = abrmDao.findAllBuildingNumbers();
		Assertions.assertThat(allBuildingNumbers)
				.isNotNull()
				.hasSize(3 + initBuildingCount)
				.contains(
						b1.getName(),
						b2.getName(),
						b3.getName()
				);
	}

}