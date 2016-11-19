package robert.web.svc.rest.ctrl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import robert.db.dao.ApplianceBuildingRoomManagementDao;
import robert.db.dao.UserDao;
import robert.exceptions.ApplianceException;
import robert.utils.api.AppLogger;
import robert.web.session.user.api.UserInfoProvider;
import robert.web.svc.rest.ctrl.api.UserServiceCtrl;
import robert.web.svc.rest.responses.asm.ApplianceAssembler;
import robert.web.svc.rest.responses.asm.ReservationAssembler;
import robert.web.svc.rest.responses.asm.RoomAssembler;
import robert.web.svc.rest.responses.data.ApplianceDR;
import robert.web.svc.rest.responses.data.ReservationDR;
import robert.web.svc.rest.responses.data.RoomDR;

import java.util.Date;
import java.util.List;

@RestController
public class UserServiceController implements UserServiceCtrl {

	private final AppLogger log;

	private final ApplianceBuildingRoomManagementDao abrmDao;

	private final UserDao userDao;

	private final UserInfoProvider userInfoProvider;

	@Autowired
	public UserServiceController(AppLogger log, ApplianceBuildingRoomManagementDao abrmDao, UserDao userDao, UserInfoProvider userInfoProvider) {
		this.log = log;
		this.abrmDao = abrmDao;
		this.userDao = userDao;
		this.userInfoProvider = userInfoProvider;
	}

	@Override
	@RequestMapping(value = MAKE_RESERVATION_URL, method = RequestMethod.POST)
	public HttpStatus makeReservation(@PathVariable(APPLIANCE_ID) Long applianceId,
									  @RequestBody ReservationDR reservationDR) {

		HttpStatus status = HttpStatus.OK;
		try {
			userDao.makeReservationForAppliance(
					userInfoProvider.getEmail(),
					applianceId,
					new Date(reservationDR.getFrom()),
					(int) reservationDR.getMinutes()
			);
		} catch (ApplianceException e) {
			log.debug(e);
			status = HttpStatus.SERVICE_UNAVAILABLE;
		}
		return status;
	}

	@Override
	@RequestMapping(value = GET_MY_RESERVATIONS_URL)
	public List<ReservationDR> getMyReservations() {
		log.debug("Get user's reservations for user:", userInfoProvider.getEmail());
		return ReservationAssembler.convertToReservationDR(
				userDao.getUsersReservations(userInfoProvider.getEmail())
		);
	}

	@Override
	@RequestMapping(value = GET_ALL_RESERVATIONS_FOR_APPLIANCE_URL)
	public List<ReservationDR> getAllReservations(@PathVariable(APPLIANCE_ID) Long applianceId) {
		log.debug("Get all reservations - ", userInfoProvider.getEmail());
		return ReservationAssembler.convertToReservationDR(
				userDao.getAllReservationsForAppliance(applianceId)
		);
	}

	@Override
	@RequestMapping(value = GET_ALL_ROOMS_IN_BUILDING_URL)
	public List<RoomDR> getAllRoomsInBuilding(@PathVariable(BUILDING_NUMBER) String buildingNumber) {
		log.debug(userInfoProvider.getEmail(), "get all rooms in building:", buildingNumber);
		return RoomAssembler.convertToRoomDR(abrmDao.findAllRoomsInBuilding(buildingNumber));
	}

	@Override
	@RequestMapping(value = GET_ALL_APPLIANCES_IN_ROOM_URL)
	public List<ApplianceDR> getAllAppliancesInRoom(@PathVariable(ROOM_ID) Long roomId) {
		log.debug(userInfoProvider.getEmail(), "get all appliances in room:", roomId);
		return ApplianceAssembler.convertToApplianceDR(
				abrmDao.getAllAppliancesInRoom(roomId)
		);
	}

	@Override
	@RequestMapping(value = GET_ALL_BUILDING_URL)
	public List<String> getAllAvailableBuildings() {
		return abrmDao.findAllBuildingNumbers();
	}

	@Override
	@RequestMapping(value = REGISTER_NEW_BUILDING_URL, method = RequestMethod.PUT)
	public HttpStatus registerNewBuilding(@PathVariable(BUILDING_NUMBER) String buildingNumber) {
		try {
			abrmDao.saveBuilding(buildingNumber);
			log.info("Registered new building:", buildingNumber);
		} catch (Exception e) {
			log.debug(e);
			return HttpStatus.CONFLICT;
		}
		return HttpStatus.OK;
	}

	@Override
	@RequestMapping(value = REGISTER_NEW_ROOM_IN_BUILDING_URL, method = RequestMethod.PUT)
	public HttpStatus registerNewRoomInBuilding(@PathVariable(BUILDING_NUMBER) String building,
												@PathVariable(ROOM_NUMBER) String roomNum) {
		try {
			abrmDao.addNewRoomToTheBuilding(building, roomNum);
			log.info("Added new room:", roomNum, "to the building:", building);
		} catch (Exception e) {
			log.debug(e);
			return HttpStatus.NOT_FOUND;
		}
		return HttpStatus.OK;
	}

	@Override
	@RequestMapping(value = REGISTER_NEW_APPLIANCE_URL, method = RequestMethod.PUT)
	public String registerNewAppliance(@PathVariable(ROOM_ID) Long roomId,
									   @PathVariable(APPLIANCE_NAME) String applianceName) {
		String applianceUniqueCode = null;
		try {
			applianceUniqueCode = abrmDao.addApplianceToTheRoom(roomId, applianceName);
			log.info("Registered new appliance:", applianceName, "with it's code:", applianceUniqueCode);
		} catch (Exception e) {
			log.debug(e);
		}
		return applianceUniqueCode;
	}

	@Override
	@RequestMapping(value = DELETE_BUILDING_URL, method = RequestMethod.DELETE)
	public HttpStatus deleteBuilding(@PathVariable(BUILDING_NUMBER) String buildingNumber) {
		log.debug("Delete building", buildingNumber, "by", userInfoProvider.getEmail());
		try {
			userDao.deleteBuilding(buildingNumber);
			return HttpStatus.OK;
		} catch (Exception e) {
			log.debug(e);
			return HttpStatus.BAD_REQUEST;
		}
	}

	@Override
	@RequestMapping(value = RENAME_BUILDING_URL, method = RequestMethod.POST)
	public HttpStatus renameBuilding(@PathVariable(BUILDING_NUMBER) String buildingNumber,
									 @PathVariable(NEW_VALUE) String newValue) {
		log.debug(userInfoProvider.getEmail(), "wants to rename building", buildingNumber, "to", newValue);
		try {
			userDao.renameBuilding(buildingNumber, newValue);
			return HttpStatus.OK;
		} catch (Exception e) {
			log.debug(e);
			return HttpStatus.BAD_REQUEST;
		}
	}

	@Override
	@RequestMapping(value = DELETE_ROOM_URL, method = RequestMethod.DELETE)
	public HttpStatus deleteRoom(@PathVariable(ROOM_ID) Long roomId) {
		log.debug("Delete room", roomId, "by", userInfoProvider.getEmail());
		try {
			userDao.deleteRoom(roomId);
			return HttpStatus.OK;
		} catch (Exception e) {
			log.debug(e);
			return HttpStatus.BAD_REQUEST;
		}
	}

	@Override
	@RequestMapping(value = RENAME_ROOM_URL, method = RequestMethod.POST)
	public HttpStatus renameRoom(@PathVariable(ROOM_ID) Long roomId,
								 @PathVariable(NEW_VALUE) String newValue) {
		log.debug(userInfoProvider.getEmail(), "wants to rename room", roomId, "to", newValue);
		try {
			userDao.renameRoom(roomId, newValue);
			return HttpStatus.OK;
		} catch (Exception e) {
			log.debug(e);
			return HttpStatus.BAD_REQUEST;
		}
	}

	@Override
	@RequestMapping(value = DELETE_APPLIANCE_URL, method = RequestMethod.DELETE)
	public HttpStatus deleteAppliance(@PathVariable(APPLIANCE_ID) Long applianceId) {
		log.debug("Delete appliance", applianceId, "by", userInfoProvider.getEmail());
		try {
			userDao.deleteAppliance(applianceId);
			return HttpStatus.OK;
		} catch (Exception e) {
			log.debug(e);
			return HttpStatus.BAD_REQUEST;
		}
	}

	@Override
	@RequestMapping(value = RENAME_APPLIANCE_URL, method = RequestMethod.POST)
	public HttpStatus renameAppliance(@PathVariable(APPLIANCE_ID) Long applianceId,
									  @PathVariable(NEW_VALUE) String newName) {
		log.debug(userInfoProvider.getEmail(), "wants to rename appliance", applianceId, "to", newName);
		try {
			userDao.renameAppliance(applianceId, newName);
			return HttpStatus.OK;
		} catch (Exception e) {
			log.debug(e);
			return HttpStatus.BAD_REQUEST;
		}
	}
}
