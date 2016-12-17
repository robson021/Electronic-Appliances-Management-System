package robert.web.svc.rest.ctrl.api;

import robert.web.svc.rest.responses.json.SimpleDTO;

public interface GuestPanelCtrl {

    String GUEST_PREFIX = "/guest/";

    String CONNECT_TO_APPLIANCE_AS_GUEST_URL = GUEST_PREFIX + "connect-to-appliance/";

	SimpleDTO connectMeToTheApplianceAsGuest(SimpleDTO requestBody);

}
