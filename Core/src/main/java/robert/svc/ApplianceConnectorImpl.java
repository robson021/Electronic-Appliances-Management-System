package robert.svc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import robert.db.dao.ApplianceBuildingRoomManagementDao;
import robert.db.entity.Reservation;
import robert.enums.Validation;
import robert.svc.api.ApplianceConnector;

import java.util.Date;

import static robert.enums.BeanNames.DEFAULT_REST_TEMPLATE;

@Service
public class ApplianceConnectorImpl implements ApplianceConnector {

	private final RestTemplate restTemplate;

	private final ApplianceBuildingRoomManagementDao abrmDao;

	@Autowired
	public ApplianceConnectorImpl(@Qualifier(DEFAULT_REST_TEMPLATE) RestTemplate restTemplate, ApplianceBuildingRoomManagementDao abrmDao) {
		this.restTemplate = restTemplate;
		this.abrmDao = abrmDao;
	}

	@Override
	public String connectToTheAppliance(String applianceAddress, int time, String accessCode, long reservationId, String userEmail) throws Exception {
		if (accessCode == null) {
			accessCode = Validation.MOCK_APPLIANCE_UNIQUE_CODE;
		} else {
			this.validateIfUserCanGrantAccessToTheAppliance(reservationId, userEmail);
		}
		final String url = applianceAddress + "/access/" + time + "/" + accessCode + "/";
		return restTemplate.getForObject(url, String.class);
	}

	private void validateIfUserCanGrantAccessToTheAppliance(long reservationId, String email) throws Exception {
		Reservation reservation = abrmDao.findReservation(reservationId);
		if (!reservation.getUser().getEmail().equals(email)) {
			throw new Exception("The reservation is not from user " + email);
		}

		long currentTime = new Date().getTime();

		if (reservation.getValidTill() < currentTime) {
			throw new Exception("Reservation time has expired");
		}

		if (reservation.getValidFrom() < currentTime) {
			throw new Exception("Too early. Reservation can be granted in "
					+ (reservation.getValidFrom() - currentTime) + " hours");
		}

	}

}
