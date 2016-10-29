package robert.web.svc.rest.ctrl;

import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import robert.db.dao.ApplianceBuildingRoomManagementDao;
import robert.db.dao.UserDao;
import robert.db.entity.Appliance;
import robert.exceptions.ApplianceException;
import robert.utils.api.AppLogger;
import robert.web.session.user.api.UserInfoProvider;
import robert.web.svc.rest.ctrl.api.UserServiceCtrl;
import robert.web.svc.rest.responses.data.ReservationData;

import java.util.Date;

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
	public HttpStatus makeReservation(@PathVariable(APPLIANCE_ID) Long applianceId, @RequestBody ReservationData reservationData) {

		HttpStatus status = HttpStatus.OK;
		Appliance app = abrmDao.findApplianceById(applianceId);
		try {
			cheIfApplianceIsAvailable(app);
			userDao.makeReservationForAppliance(userInfoProvider.getEmail(), applianceId,
					new Date(reservationData.getFrom()),
					reservationData.getHours());
		} catch (NotFoundException e) {
			log.debug(e);
			status = HttpStatus.NOT_FOUND;
		} catch (ApplianceException e) {
			log.debug(e);
			status = HttpStatus.SERVICE_UNAVAILABLE;
		}
		return status;
	}

	private void cheIfApplianceIsAvailable(Appliance app) throws NotFoundException {
		if (app == null) {
			throw new NotFoundException("Appliance not found.");
		}
	}
}
