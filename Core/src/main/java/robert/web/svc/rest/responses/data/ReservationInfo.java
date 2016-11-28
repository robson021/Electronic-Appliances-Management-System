package robert.web.svc.rest.responses.data;

public class ReservationInfo {

    private String applianceAddress;

    private String accessCode;

    private int time;

    private long reservationId;

    public String getApplianceAddress() {
        return applianceAddress;
    }

    public void setApplianceAddress(String applianceAddress) {
        this.applianceAddress = applianceAddress;
    }

    public String getAccessCode() {
        return accessCode;
    }

    public void setAccessCode(String accessCode) {
        this.accessCode = accessCode;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public long getReservationId() {
        return reservationId;
    }

    public void setReservationId(long reservationId) {
        this.reservationId = reservationId;
    }
}
