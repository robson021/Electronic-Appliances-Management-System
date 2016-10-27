package robert.web.svc.rest.ctrl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javassist.NotFoundException;
import robert.db.dao.ApplianceBuildingRoomManagementDao;
import robert.db.dao.UserDao;
import robert.db.entity.Appliance;
import robert.exceptions.ApplianceException;
import robert.utils.api.AppLogger;
import robert.web.session.user.api.UserInfoProvider;
import robert.web.svc.rest.ctrl.api.UserServiceCtrl;
import robert.web.svc.rest.responses.data.ReservationData;

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
    public HttpStatus makeReservation(@RequestParam(APPLIANCE_ID) Long applianceId, @RequestBody ReservationData reservationData) {

        Appliance app = abrmDao.findApplianceById(applianceId);
        try {
            cheIfApplianceIsAvailable(app);
        } catch (ApplianceException e) {
            log.debug(e);
            return HttpStatus.NOT_ACCEPTABLE;
        } catch (NotFoundException e) {
            log.debug(e);
            return HttpStatus.NOT_FOUND;
        }
        try {
            userDao.makeReservationForAppliance(userInfoProvider.getEmail(), applianceId, new Date(reservationData.getFrom()), reservationData.getHours());
        } catch (ApplianceException e) {
            log.debug(e);
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return HttpStatus.OK;
    }

    private void cheIfApplianceIsAvailable(Appliance app) throws ApplianceException, NotFoundException {
        if ( app == null ) {
            throw new NotFoundException("Appliance not found");
        }
        if ( app.getReservation() != null ) {
            throw new ApplianceException(app.getName() + " is already taken.");
        }
    }
}
