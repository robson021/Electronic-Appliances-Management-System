package robert.web.svc.rest.ctrl.api;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import robert.web.svc.rest.responses.data.ApplianceDR;
import robert.web.svc.rest.responses.data.ReservationDR;
import robert.web.svc.rest.responses.data.RoomDR;
import robert.web.svc.rest.responses.data.SimpleDR;

public interface UserServiceCtrl extends BasicParams {

	String USER_SERVICE_PREFIX = "/user-service/";

	String GET_ALL_ROOMS_IN_BUILDING_URL = USER_SERVICE_PREFIX + "{" + BUILDING_NUMBER + "}/";

	String GET_ALL_BUILDING_URL = USER_SERVICE_PREFIX + "get-all-buildings/";

	String GET_ALL_APPLIANCES_IN_ROOM_URL = USER_SERVICE_PREFIX + "get-all-appliances/{" + ROOM_ID + "}/";

	String MAKE_RESERVATION_URL = USER_SERVICE_PREFIX + "reservation" + "/{" + APPLIANCE_ID + "}/";

	String GET_MY_RESERVATIONS_URL = USER_SERVICE_PREFIX + "my-reservations/";

	String CANCEL_MY_RESERVATIONS_URL = USER_SERVICE_PREFIX + "cancel-reservation/{" + RESERVATION_ID + "}/";

	String GET_ALL_RESERVATIONS_FOR_APPLIANCE_URL = USER_SERVICE_PREFIX + "all-reservations/{" + APPLIANCE_ID + "}/";

	String GET_TOKEN_FOR_RESERVATION = USER_SERVICE_PREFIX + "reservation/token/{" + RESERVATION_ID + "}/";

	String REGISTER_NEW_BUILDING_URL = USER_SERVICE_PREFIX + "register-building/{" + BUILDING_NUMBER + "}/";

	String REGISTER_NEW_ROOM_IN_BUILDING_URL = USER_SERVICE_PREFIX + "register-room/{" +
			BUILDING_NUMBER + "}/{" + ROOM_NUMBER + "}/";

	String REGISTER_NEW_APPLIANCE_URL = USER_SERVICE_PREFIX + "register-appliance/{" +
			ROOM_ID + "}/{" + APPLIANCE_NAME + "}/";

	String DELETE_BUILDING_URL = USER_SERVICE_PREFIX + "delete/building/{" + BUILDING_NUMBER + "}/";

	String RENAME_BUILDING_URL = USER_SERVICE_PREFIX + "rename/building/{" + BUILDING_NUMBER + "}/{" + NEW_VALUE + "}/";

	String DELETE_ROOM_URL = USER_SERVICE_PREFIX + "delete/room/{" + ROOM_ID + "}/";

	String RENAME_ROOM_URL = USER_SERVICE_PREFIX + "rename/room/{" + ROOM_ID + "}/{" + NEW_VALUE + "}/";

	String DELETE_APPLIANCE_URL = USER_SERVICE_PREFIX + "delete/appliance/{" + APPLIANCE_ID + "}/";

	String RENAME_APPLIANCE_URL = USER_SERVICE_PREFIX + "rename/appliance/{" + APPLIANCE_ID + "}/{" + NEW_VALUE + "}/";

	String SET_NEW_ADDRESS_0F_APPLIANCE_URL = USER_SERVICE_PREFIX + "new-address/appliance/{" + APPLIANCE_ID + "}/{" + NEW_VALUE + "}/";

    String CONNECT_USER_TO_APPLIANCE_URL = USER_SERVICE_PREFIX + "connect-to-appliance/{" + RESERVATION_ID + "}/";

	List<RoomDR> getAllRoomsInBuilding(String buildingNumber);

	List<ApplianceDR> getAllAppliancesInRoom(Long roomId);

	List<String> getAllAvailableBuildings();

	HttpStatus makeReservation(Long applianceId, ReservationDR reservationDR);

	List<ReservationDR> getMyReservations();

	List<ReservationDR> getAllReservations(Long applianceId);

	HttpStatus cancelMyReservation(Long reservationId);

	HttpStatus setApplianceAddress(Long applianceId, String newAddress);

	SimpleDR getReservationToken(Long reservationId);

	HttpStatus registerNewBuilding(String buildingNumber);

	HttpStatus registerNewRoomInBuilding(String building, String roomNum);

	SimpleDR registerNewAppliance(Long roomId, String applianceName);

	HttpStatus deleteBuilding(String buildingNumber);

	HttpStatus renameBuilding(String buildingNumber, String newValue);

	HttpStatus deleteRoom(Long roomId);

	HttpStatus renameRoom(Long roomId, String newValue);

	HttpStatus deleteAppliance(Long applianceId);

	HttpStatus renameAppliance(Long applianceId, String newName);

    ResponseEntity<String> connectToTheAppliance(Long reservationId);

}
