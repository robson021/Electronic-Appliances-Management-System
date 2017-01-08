package robert.svc.reports;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import robert.svc.api.ReportPdfGenerator;
import robert.web.svc.rest.responses.dto.ReservationDTO;
import utils.SpringTest;

public class ReportPdfGeneratorTest extends SpringTest {

    @Autowired
    private ReportPdfGenerator generator;

    @Override
    public void setup() throws Exception {
    }

    @Test
    public void generateRandomPdf() throws Exception {
        List<ReservationDTO> reservations = generateReservations();
        String filename = generator.generatePdfWithReport(reservations);

        Assertions.assertThat(filename)
                .contains(".pdf");

        Path path = Paths.get(filename);
        Files.deleteIfExists(path);
    }

    private List<ReservationDTO> generateReservations() {
        String owner = "Test User";
        long time = new Date().getTime();

        ReservationDTO reservation1 = new ReservationDTO();
        reservation1.setAppliance("test appl1");
        reservation1.setFrom(time);
        reservation1.setMinutes(45);
        reservation1.setOwner(owner);

        ReservationDTO reservation2 = new ReservationDTO();
        reservation2.setAppliance("test appl2");
        reservation2.setFrom(time / 2);
        reservation2.setMinutes(90);
        reservation2.setOwner(owner);

        return Arrays.asList(reservation1, reservation2);
    }
}