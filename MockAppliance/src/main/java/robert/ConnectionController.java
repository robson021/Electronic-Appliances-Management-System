package robert;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConnectionController {

	private static final Logger log = Logger.getLogger("MockAppliance");

	private static final String TIME = "time";

	private static final String UNIQUE_CODE = "code";

	private static final String ACCESS_URL = "/access/{" + TIME + "}/{" + UNIQUE_CODE + "}/";

	private DateTime timeOfUseExpiration = null;

	@RequestMapping(value = ACCESS_URL)
	public HttpStatus grantAccess(@PathVariable(TIME) Integer hoursFromNow,
								  @PathVariable(UNIQUE_CODE) String code) {

		if (hoursFromNow <= 0) {
			return HttpStatus.BAD_REQUEST;
		}

		if (!Validation.MY_UNIQUE_CODE.equals(code)) {
			return HttpStatus.UNAUTHORIZED;
		}

		final DateTime currentTime = new DateTime();

		if (timeOfUseExpiration != null && !(currentTime.isAfter(timeOfUseExpiration))) {
			log.error("Cannot grant access. The appliance is already in use.");
			return HttpStatus.CONFLICT;
		}

		timeOfUseExpiration = currentTime
				.plusHours(hoursFromNow);

		log.info("Access grant to the appliance. Valid form now for " + hoursFromNow + " hours");

		return HttpStatus.OK;
	}

}
