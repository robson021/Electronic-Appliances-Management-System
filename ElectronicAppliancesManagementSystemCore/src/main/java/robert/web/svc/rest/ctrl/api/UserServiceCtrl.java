package robert.web.svc.rest.ctrl.api;

import org.springframework.http.HttpStatus;
import robert.web.svc.rest.responses.data.ReservationDR;
import robert.web.svc.rest.responses.data.RoomDR;

import java.util.List;

public interface UserServiceCtrl extends BasicParams {

    String APPLIANCE_ID = "applianceId";

    String USER_SERVICE_PREFIX = "/user-service/";

    String MAKE_RESERVATION_URL = USER_SERVICE_PREFIX + "reservation" + "/{" + APPLIANCE_ID + "}/";

    String GET_ALL_ROOMS_IN_BUILDING = USER_SERVICE_PREFIX + "{" + BUILDING_NUMBER + "}/";

    String REGISTER_NEW_BUILDING = USER_SERVICE_PREFIX + "register-building/{" + BUILDING_NUMBER + "}/";

    String REGISTER_NEW_ROOM_IN_BUILDING = USER_SERVICE_PREFIX + "register-room/{" +
            BUILDING_NUMBER + "}/{" + ROOM_NUMBER + "}/";

    HttpStatus makeReservation(Long applianceId, ReservationDR reservationDR);

    List<RoomDR> getAllRoomsInBuilding(String buildingNumber);

    HttpStatus registerNewBuilding(String buildingNumber);

    HttpStatus registerNewRoomInBuilding(String building, String roomNum);

}
