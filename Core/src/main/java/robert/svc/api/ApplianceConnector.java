package robert.svc.api;

public interface ApplianceConnector {
	String connectToTheAppliance(String applianceAddress, int time,
								 String accessCode, long reservationId, String userEmail) throws Exception;

    String connectAsGuest(String token) throws Exception;
}
