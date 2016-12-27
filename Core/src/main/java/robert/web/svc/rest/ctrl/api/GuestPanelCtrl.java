package robert.web.svc.rest.ctrl.api;

import robert.web.svc.rest.responses.dto.BasicDTO;

public interface GuestPanelCtrl {

    String GUEST_PREFIX = "/guest/";

    String CONNECT_TO_APPLIANCE_AS_GUEST_URL = GUEST_PREFIX + "connect-to-appliance/";

    BasicDTO connectMeToTheApplianceAsGuest(BasicDTO requestBody);

}
