package robert.svc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import robert.enums.Validation;
import robert.svc.api.ApplianceConnector;

import static robert.enums.BeanNames.DEFAULT_REST_TEMPLATE;

@Service
public class ApplianceConnectorImpl implements ApplianceConnector {

	private final RestTemplate restTemplate;

	@Autowired
	public ApplianceConnectorImpl(@Qualifier(DEFAULT_REST_TEMPLATE) RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	@Override
	public String connectToTheAppliance(String applianceAddress, int time, String accessCode) {
		if (accessCode == null) {
			accessCode = Validation.MOCK_APPLIANCE_UNIQUE_CODE;
		}
		final String url = applianceAddress + "/access/" + time + "/" + accessCode + "/";
		return restTemplate.getForObject(url, String.class);
	}

}
