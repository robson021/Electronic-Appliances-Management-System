package robert.svc.api;

public interface ReportService {

    void sendUserReportAboutReservationsInThePast(long applianceId, int daysAgo, String receiverEmail);
}
