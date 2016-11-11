package robert.web.svc.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import robert.enums.Validation;

import static robert.enums.BeanNames.DEFAULT_REST_TEMPLATE;

@Service
public class ApplianceConnector {

	// TODO: move to svc package and add i-face

	private final RestTemplate restTemplate;

	@Autowired
	public ApplianceConnector(@Qualifier(DEFAULT_REST_TEMPLATE) RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	public String connectToTheAppliance(String applianceAddress, int time, String accessCode) {
		if (accessCode == null) {
			accessCode = Validation.MOCK_APPLIANCE_UNIQUE_CODE;
		}
		final String url = applianceAddress + "/" + time + "/" + accessCode + "/";
		return restTemplate.getForObject(url, String.class);
	}

}
