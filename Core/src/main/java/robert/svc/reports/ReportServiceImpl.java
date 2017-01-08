package robert.svc.reports;

import java.io.File;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import robert.db.dao.ApplianceBuildingRoomManagementDao;
import robert.db.entity.Reservation;
import robert.svc.api.MailService;
import robert.svc.api.ReportPdfGenerator;
import robert.svc.api.ReportService;
import robert.utils.api.AppLogger;
import robert.web.svc.rest.responses.asm.ReservationAssembler;
import robert.web.svc.rest.responses.dto.ReservationDTO;

@SuppressWarnings("ALL")
@Service
public class ReportServiceImpl implements ReportService {

    private static final String MESSAGE_TOPIC = "Appliance reservation raport";

    private final AppLogger log;

    private final MailService mailService;

    private final ReportPdfGenerator reportPdfGenerator;

    private final ApplianceBuildingRoomManagementDao abrmDao;

    @Autowired
    public ReportServiceImpl(AppLogger log, MailService mailService, ReportPdfGenerator reportPdfGenerator, ApplianceBuildingRoomManagementDao abrmDao) {
        this.log = log;
        this.mailService = mailService;
        this.reportPdfGenerator = reportPdfGenerator;
        this.abrmDao = abrmDao;
    }

    @Override
    public void sendUserReportAboutReservationsInThePast(long applianceId, int daysAgo, String receiverEmail) {
        List<Reservation> results = abrmDao.getReservationsFromThePast(applianceId, daysAgo);
        if ( results.isEmpty() ) {
            log.debug("Found no reservations for appliance with id:", applianceId);
            sendEmptyMessage(receiverEmail);
        } else {
            log.debug("Found ", results.size(), "reservations");
            sendRaport(ReservationAssembler.convertToReservationDTO(results), receiverEmail);
        }
    }

    private void sendRaport(List<ReservationDTO> reservations, String receiverEmail) {
        try {
            String fileName = reportPdfGenerator.generatePdfWithReport(reservations);
            String applianceName = reservations.get(0)
                    .getAppliance();
            File file = new File(fileName);
            mailService.sendEmail(receiverEmail, MESSAGE_TOPIC, "Reports for " + applianceName, file);
        } catch (Exception e) {
        }
    }

    private void sendEmptyMessage(String receiverEmail) {
        mailService.sendEmail(receiverEmail, MESSAGE_TOPIC, //
                "No reservations has been found.", null);
    }
}
