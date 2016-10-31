package robert.web.svc.rest.ctrl.api;

import org.springframework.http.HttpStatus;
import robert.web.svc.rest.responses.data.ReservationDR;
import robert.web.svc.rest.responses.data.RoomDR;

import java.util.List;

public interface UserServiceCtrl extends BasicParams {

	String USER_SERVICE_PREFIX = "/user-service/";

	String GET_ALL_ROOMS_IN_BUILDING_URL = USER_SERVICE_PREFIX + "{" + BUILDING_NUMBER + "}/";

	String GET_ALL_BUILDING_URL = USER_SERVICE_PREFIX + "get-all-buildings/";

	String MAKE_RESERVATION_URL = USER_SERVICE_PREFIX + "reservation" + "/{" + APPLIANCE_ID + "}/";

	String REGISTER_NEW_BUILDING_URL = USER_SERVICE_PREFIX + "register-building/{" + BUILDING_NUMBER + "}/";

	String REGISTER_NEW_ROOM_IN_BUILDING_URL = USER_SERVICE_PREFIX + "register-room/{" +
			BUILDING_NUMBER + "}/{" + ROOM_NUMBER + "}/";

	String REGISTER_NEW_APPLIANCE_URL = USER_SERVICE_PREFIX + "register-appliance/{" +
			ROOM_ID + "}/{" + APPLIANCE_NAME + "}/";

	List<RoomDR> getAllRoomsInBuilding(String buildingNumber);

	List<String> getAllAvailableBuildings();

	HttpStatus makeReservation(Long applianceId, ReservationDR reservationDR);

	HttpStatus registerNewBuilding(String buildingNumber);

	HttpStatus registerNewRoomInBuilding(String building, String roomNum);

	String registerNewAppliance(Long roomId, String applianceName);
}
