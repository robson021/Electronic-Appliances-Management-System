package robert.svc.api;

public interface ReportService {

    void sendUserReportAboutReservationsInThePast(int daysAgo, String receiverEmail);
}
