package robert.web.svc.rest.ctrl.api;

import robert.web.svc.rest.responses.data.SimpleDR;

public interface GuestPanelCtrl {

    String GUEST_PREFIX = "/guest/";

    String CONNECT_TO_APPLIANCE_AS_GUEST_URL = GUEST_PREFIX + "connect-to-appliance/";

    SimpleDR connectMeToTheApplianceAsGuest(SimpleDR requestBody);

}
