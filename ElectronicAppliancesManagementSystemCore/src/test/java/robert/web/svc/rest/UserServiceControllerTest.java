package robert.web.svc.rest;

import org.assertj.core.api.Assertions;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import robert.db.dao.ApplianceBuildingRoomManagementDao;
import robert.db.dao.UserDao;
import robert.db.entity.Appliance;
import robert.db.entity.Reservation;
import robert.web.svc.rest.responses.data.ReservationData;
import utils.SpringWebMvcTest;
import utils.TestUtils;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
		Appliance appliance = TestUtils.geenrateRandomAppliance();
		long appId = abrmDao.saveAppliance(appliance)
				.getId();

		String url = String.format(RESERVATION_URL, appId);

		DateTime currentTime = new DateTime();
		ReservationData reservation = new ReservationData();
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
		reservation = new ReservationData();
		reservation.setFrom(currentTime.minusHours(3).toDate().getTime());
		reservation.setHours(5);
		json = TestUtils.asJsonString(reservation);

		mockMvc.perform(post(url).content(json)
				.contentType(MediaType.APPLICATION_JSON));

		appl = abrmDao.findApplianceById(appId);
		Assertions.assertThat(appl.getReservations())
				.hasSize(numOfReservations);

		// 3 - ok reservation
		reservation = new ReservationData();
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

}