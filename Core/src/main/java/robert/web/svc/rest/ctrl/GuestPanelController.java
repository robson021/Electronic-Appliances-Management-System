package robert.web.svc.rest.ctrl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import robert.svc.api.ApplianceConnector;
import robert.utils.api.AppLogger;
import robert.web.svc.rest.ctrl.api.GuestPanelCtrl;
import robert.web.svc.rest.responses.json.SimpleDTO;

@RestController
public class GuestPanelController implements GuestPanelCtrl {

    private final AppLogger log;

    private final ApplianceConnector applianceConnector;

    @Autowired
    public GuestPanelController(AppLogger log, ApplianceConnector applianceConnector) {
        this.log = log;
        this.applianceConnector = applianceConnector;
    }

    @Override
    @RequestMapping(value = CONNECT_TO_APPLIANCE_AS_GUEST_URL, method = RequestMethod.POST)
    public SimpleDTO connectMeToTheApplianceAsGuest(@RequestBody SimpleDTO requestBody) {
        try {
            String response = applianceConnector.connectAsGuest(requestBody.getText());
            return new SimpleDTO(response);
        } catch (Exception e) {
            log.debug(e);
            return new SimpleDTO("Failed to connect. " + e.getMessage());
        }
    }
}
