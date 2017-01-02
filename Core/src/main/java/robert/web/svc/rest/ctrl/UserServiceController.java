package robert.web.svc.rest.ctrl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import robert.db.dao.ApplianceBuildingRoomManagementDao;
import robert.db.dao.UserDao;
import robert.svc.api.ApplianceConnector;
import robert.svc.appliance.ReservationInfo;
import robert.utils.api.AppLogger;
import robert.web.session.user.api.UserInfoProvider;
import robert.web.svc.rest.ctrl.api.UserServiceCtrl;
import robert.web.svc.rest.responses.asm.ApplianceAssembler;
import robert.web.svc.rest.responses.asm.ReservationAssembler;
import robert.web.svc.rest.responses.asm.RoomAssembler;
import robert.web.svc.rest.responses.dto.ApplianceDTO;
import robert.web.svc.rest.responses.dto.BasicDTO;
import robert.web.svc.rest.responses.dto.ReservationDTO;
import robert.web.svc.rest.responses.dto.RoomDTO;

import java.util.Date;
import java.util.List;

@RestController
public class UserServiceController implements UserServiceCtrl {

    private final AppLogger log;

    private final ApplianceBuildingRoomManagementDao abrmDao;

    private final UserDao userDao;

    private final UserInfoProvider userInfoProvider;

    private final ApplianceConnector applianceConnector;

    @Autowired
    public UserServiceController(AppLogger log, ApplianceBuildingRoomManagementDao abrmDao, UserDao userDao, UserInfoProvider userInfoProvider,
            ApplianceConnector applianceConnector) {
        this.log = log;
        this.abrmDao = abrmDao;
        this.userDao = userDao;
        this.userInfoProvider = userInfoProvider;
        this.applianceConnector = applianceConnector;
    }

    @Override
    @RequestMapping(value = MAKE_RESERVATION_URL, method = RequestMethod.POST)
    public HttpStatus makeReservation(@PathVariable(APPLIANCE_ID) Long applianceId, @RequestBody ReservationDTO reservationDTO) {
        HttpStatus status = HttpStatus.OK;
        try {
            userDao.makeReservationForAppliance(userInfoProvider.getEmail(), applianceId, new Date(reservationDTO.getFrom()), (int) reservationDTO.getMinutes());
            log.info("New reservation done by", userInfoProvider.getEmail());
        } catch (Exception e) {
            log.debug(e);
            status = HttpStatus.SERVICE_UNAVAILABLE;
        }
        return status;
    }

    @Override
    @RequestMapping(value = GET_MY_RESERVATIONS_URL)
    public List<ReservationDTO> getMyReservations() {
        log.debug("Get user's reservations for user:", userInfoProvider.getEmail());
        return ReservationAssembler.convertToReservationDTO(userDao.getUsersReservations(userInfoProvider.getEmail()));
    }

    @Override
    @RequestMapping(value = GET_ALL_RESERVATIONS_FOR_APPLIANCE_URL)
    public List<ReservationDTO> getAllReservations(@PathVariable(APPLIANCE_ID) Long applianceId) {
        log.debug("Get all reservations - ", userInfoProvider.getEmail());
        return ReservationAssembler.convertToReservationDTO(userDao.getAllReservationsForAppliance(applianceId));
    }

    @Override
    @RequestMapping(value = CANCEL_MY_RESERVATIONS_URL, method = RequestMethod.DELETE)
    public HttpStatus cancelMyReservation(@PathVariable(RESERVATION_ID) Long reservationId) {
        log.debug("User", userInfoProvider.getEmail(), "wants to delete reservation", reservationId);
        try {
            userDao.cancelReservation(userInfoProvider.getEmail(), reservationId);
            return HttpStatus.OK;
        } catch (Exception e) {
            log.debug(e);
            return HttpStatus.CONFLICT;
        }
    }

    @Override
    @RequestMapping(value = SET_NEW_ADDRESS_0F_APPLIANCE_URL, method = RequestMethod.POST)
    public HttpStatus setApplianceAddress(@PathVariable(APPLIANCE_ID) Long applianceId, @PathVariable(NEW_VALUE) String newAddress) {
        log.debug(userInfoProvider.getEmail(), "wants to set new address for the appliance '", applianceId, "' -", newAddress);
        userDao.setNewAddressForTheAppliance(applianceId, newAddress);
        return HttpStatus.OK;
    }

    @Override
    @RequestMapping(value = GET_TOKEN_FOR_RESERVATION)
    public BasicDTO getReservationToken(@PathVariable(RESERVATION_ID) Long reservationId) {
        log.debug("Get token for reservation -", userInfoProvider.getEmail());
        try {
            String token = userDao.getTokenForMyReservation(reservationId, userInfoProvider.getEmail());
            log.debug("Token:", token);
            return new BasicDTO(token);
        } catch (Exception e) {
            log.debug(e);
            return new BasicDTO("NOT FOUND");
        }
    }

    @Override
    @RequestMapping(value = GET_ALL_ROOMS_IN_BUILDING_URL)
    public List<RoomDTO> getAllRoomsInBuilding(@PathVariable(BUILDING_NUMBER) String buildingNumber) {
        log.debug(userInfoProvider.getEmail(), "get all rooms in building:", buildingNumber);
        return RoomAssembler.convertToRoomDTO(abrmDao.findAllRoomsInBuilding(buildingNumber));
    }

    @Override
    @RequestMapping(value = GET_ALL_APPLIANCES_IN_ROOM_URL)
    public List<ApplianceDTO> getAllAppliancesInRoom(@PathVariable(ROOM_ID) Long roomId) {
        log.debug(userInfoProvider.getEmail(), "get all appliances in room:", roomId);
        return ApplianceAssembler.convertToApplianceDTO(abrmDao.getAllAppliancesInRoom(roomId));
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
    public HttpStatus registerNewRoomInBuilding(@PathVariable(BUILDING_NUMBER) String building, @PathVariable(ROOM_NUMBER) String roomNum) {
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
    public BasicDTO registerNewAppliance(@PathVariable(ROOM_ID) Long roomId, @PathVariable(APPLIANCE_NAME) String applianceName) {
        try {
            String applianceUniqueCode = abrmDao.addApplianceToTheRoom(roomId, applianceName);
            log.info("Registered new appliance -", applianceName, "with it's code:", applianceUniqueCode);
            return new BasicDTO(applianceUniqueCode);
        } catch (Exception e) {
            log.debug(e);
            return new BasicDTO("ERROR");
        }
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
    public HttpStatus renameBuilding(@PathVariable(BUILDING_NUMBER) String buildingNumber, @PathVariable(NEW_VALUE) String newValue) {
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
    public HttpStatus renameRoom(@PathVariable(ROOM_ID) Long roomId, @PathVariable(NEW_VALUE) String newValue) {
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
    public HttpStatus renameAppliance(@PathVariable(APPLIANCE_ID) Long applianceId, @PathVariable(NEW_VALUE) String newName) {
        log.debug(userInfoProvider.getEmail(), "wants to rename appliance", applianceId, "to", newName);
        try {
            userDao.renameAppliance(applianceId, newName);
            return HttpStatus.OK;
        } catch (Exception e) {
            log.debug(e);
            return HttpStatus.BAD_REQUEST;
        }
    }

    @Override
    @RequestMapping(value = CONNECT_USER_TO_APPLIANCE_URL, method = RequestMethod.POST)
    public BasicDTO connectToTheAppliance(@PathVariable(RESERVATION_ID) Long reservationId) {
        String email = userInfoProvider.getEmail();
        log.info(email, "is trying to connect to the appliance");
        try {
            ReservationInfo ri = userDao.getReservationInfo(reservationId, email);
            String response = applianceConnector.connectToTheAppliance( //
                    ri.getApplianceAddress(), //
                    ri.getTime(), //
                    ri.getAccessCode(), //
                    ri.getReservationId(), //
                    email);
            log.info("Connected with response:", response);
            return new BasicDTO(response);
        } catch (Exception e) {
            log.debug(e);
            return new BasicDTO(e.getMessage());
        }
    }

}
