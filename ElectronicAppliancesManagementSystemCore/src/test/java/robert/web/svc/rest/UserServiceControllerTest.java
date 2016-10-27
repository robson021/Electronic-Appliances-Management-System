package robert.web.svc.rest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Date;

import org.assertj.core.api.Assertions;
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

public class UserServiceControllerTest extends SpringWebMvcTest {

    private static final String RESERVATION_URL = "/user/reservation/%s/";

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

        int initialSize = abrmDao.findAllReservations()
                .size();
        userDao.saveUser(TestUtils.createAdminUser());

        Appliance appliance = TestUtils.geenrateRandomAppliance();
        long appId = abrmDao.saveAppliance(appliance)
                .getId();

        String url = String.format(RESERVATION_URL, appId);

        ReservationData reservation = new ReservationData();
        reservation.setFrom(new Date().getTime());
        reservation.setHours(3);
        String json = TestUtils.asJsonString(reservation);

        mockMvc.perform(post(url).content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Iterable<Reservation> allReservations = abrmDao.findAllReservations();
        Assertions.assertThat(allReservations)
                .hasSize(initialSize + 1);

    }

}