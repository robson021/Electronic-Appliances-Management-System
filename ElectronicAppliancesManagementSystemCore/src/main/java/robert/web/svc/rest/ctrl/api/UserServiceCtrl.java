package robert.web.svc.rest.ctrl.api;

import org.springframework.http.HttpStatus;
import robert.web.svc.rest.responses.data.ReservationData;

public interface UserServiceCtrl {

    String USER_SERVICE_PREFIX = "/user-service/";

    String APPLIANCE_ID = "applianceId";

    String RESERVATION = "reservation";

    String MAKE_RESERVATION_URL = USER_SERVICE_PREFIX + RESERVATION + "/{" + APPLIANCE_ID + "}/";

    HttpStatus makeReservation(Long applianceId, ReservationData reservationData);


}
