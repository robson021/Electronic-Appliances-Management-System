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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static robert.web.svc.rest.ctrl.api.BasicParams.BUILDING_NUMBER;
import static robert.web.svc.rest.ctrl.api.UserServiceCtrl.GET_ALL_ROOMS_IN_BUILDING;
import static robert.web.svc.rest.ctrl.api.UserServiceCtrl.REGISTER_NEW_BUILDING;

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

		final String url = GET_ALL_ROOMS_IN_BUILDING.replace("{" + BUILDING_NUMBER + "}",
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
		final String url = REGISTER_NEW_BUILDING.replace("{" + BUILDING_NUMBER + "}", name);

		int numBuildings = abrmDao.findAllBuildings().size();

		mockMvc.perform(put(url))
				.andExpect(status().isOk());

		Building building = abrmDao.findBuildingByName(name);

		Assertions.assertThat(building)
				.isNotNull();

		// this should fail due to unique name conflict
		mockMvc.perform(put(url));

		final int sizeAfterTwoRegistrations = abrmDao.findAllBuildings().size();
		Assertions.assertThat(sizeAfterTwoRegistrations)
				.isEqualTo(++numBuildings);
	}

}