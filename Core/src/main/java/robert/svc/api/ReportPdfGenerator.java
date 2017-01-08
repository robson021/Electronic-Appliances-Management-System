package robert.svc.api;

import java.util.List;

import robert.web.svc.rest.responses.dto.ReservationDTO;

public interface ReportPdfGenerator {
    String generatePdfWithReport(List<ReservationDTO> reservations) throws Exception;
}
