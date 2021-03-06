package robert.svc.appliance;

import static robert.enums.BeanNames.DEFAULT_REST_TEMPLATE;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import robert.db.dao.ApplianceBuildingRoomManagementDao;
import robert.db.entity.Appliance;
import robert.db.entity.Reservation;
import robert.enums.Validation;
import robert.svc.api.ApplianceConnector;
import robert.utils.api.AppLogger;

@Service
public class ApplianceConnectorImpl implements ApplianceConnector {

    private final AppLogger log;

    private final RestTemplate restTemplate;

    private final ApplianceBuildingRoomManagementDao abrmDao;

    @Autowired
    public ApplianceConnectorImpl(AppLogger log, @Qualifier(DEFAULT_REST_TEMPLATE) RestTemplate restTemplate, ApplianceBuildingRoomManagementDao abrmDao) {
        this.log = log;
        this.restTemplate = restTemplate;
        this.abrmDao = abrmDao;
    }

    @Override
    public String connectToTheAppliance(String applianceAddress, int time, String accessCode, long reservationId, long userId) throws Exception {
        if ( accessCode == null ) {
            accessCode = Validation.MOCK_APPLIANCE_UNIQUE_CODE;
        } else {
            this.validateIfUserCanGetAccessToTheAppliance(reservationId, userId);
        }
        return connect(applianceAddress, time, accessCode);
    }

    @Override
    public String connectAsGuest(String token) throws Exception {
        Reservation reservation = abrmDao.getReservationByToken(token);
        validateTime(reservation);
        Appliance appliance = reservation.getAppliance();
        return connect(appliance.getAddress(), reservation.getDurationOfAccess(), appliance.getUniqueCode());
    }

    private String connect(String applianceAddress, int time, String accessCode) {
        String url = applianceAddress + "/access/" + time + "/" + accessCode + "/";
        String response = restTemplate.getForObject(url, String.class);
        log.debug(url, "-->", response);
        return response;
    }

    private void validateIfUserCanGetAccessToTheAppliance(long reservationId, long userId) throws Exception {
        Reservation reservation = abrmDao.findReservation(reservationId);
        if ( !reservation.getUser()
                .getId()
                .equals(userId) ) {
            throw new Exception("The reservation is not for user with id: " + userId);
        }
        validateTime(reservation);
    }

    private void validateTime(Reservation reservation) throws Exception {
        long currentTime = new Date().getTime();
        if ( reservation.getValidTill() < currentTime ) {
            throw new Exception("Reservation time has expired");
        }
        if ( reservation.getValidFrom() > currentTime ) {
            throw new Exception("Too early. Reservation can be accessed in " + getRemainingTimeToReservationStart(reservation.getValidFrom() - currentTime));
        }
    }

    private String getRemainingTimeToReservationStart(long timeThatLeftInMilliseconds) {
        double timeInMinutes = timeThatLeftInMilliseconds / 1000 / 60;
        if (timeInMinutes >= 60) {
            return timeInMinutes / 60 + " hours.";
        }
        return timeInMinutes + " minutes.";
    }

}
