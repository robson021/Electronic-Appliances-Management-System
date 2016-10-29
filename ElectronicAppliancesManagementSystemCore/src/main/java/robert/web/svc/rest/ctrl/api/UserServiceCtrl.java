package robert.web.svc.rest.ctrl.api;

import org.springframework.http.HttpStatus;
import robert.web.svc.rest.responses.data.ReservationDR;
import robert.web.svc.rest.responses.data.RoomDR;

import java.util.List;

public interface UserServiceCtrl extends BasicParams {

    String APPLIANCE_ID = "applianceId";

    String RESERVATION = "reservation";

    String USER_SERVICE_PREFIX = "/user-service/";

    String MAKE_RESERVATION_URL = USER_SERVICE_PREFIX + RESERVATION + "/{" + APPLIANCE_ID + "}/";

    String GET_ALL_ROOMS_IN_BUILDING = USER_SERVICE_PREFIX + "{" + BUILDING_NUMBER + "}/";

    HttpStatus makeReservation(Long applianceId, ReservationDR reservationDR);

    List<RoomDR> getAllRoomsInBuilding(String buildingNumber);


}
