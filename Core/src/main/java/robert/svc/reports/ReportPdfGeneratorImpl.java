package robert.svc.reports;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Service;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import robert.svc.api.ReportPdfGenerator;
import robert.web.svc.rest.responses.dto.ReservationDTO;

@Service
public class ReportPdfGeneratorImpl implements ReportPdfGenerator {

    private static final short NUM_OF_COLUMNS = 4;

    private static final AtomicInteger idCounter = new AtomicInteger(0);

    private final static String[][] FONTS = { //
            { BaseFont.HELVETICA, BaseFont.WINANSI },
            { "resources/fonts/cmr10.afm", BaseFont.WINANSI },
            { "resources/fonts/cmr10.pfm", BaseFont.WINANSI },
            { "resources/fonts/Puritan2.otf", BaseFont.WINANSI },
            { "KozMinPro-Regular", "UniJIS-UCS2-H" } };

    @Override
    public String generatePdfWithReport(java.util.List<ReservationDTO> reservations) throws Exception {
        Document document = new Document();
        String fileName = "Report " + idCounter.incrementAndGet() + ".pdf";
        BaseFont bf = BaseFont.createFont(FONTS[0][0], FONTS[0][1], BaseFont.EMBEDDED);
        PdfWriter.getInstance(document, new FileOutputStream(fileName));
        document.open();

        String applianceName = reservations.get(0)
                .getAppliance();
        Chunk underline = new Chunk("Report for " + applianceName, new Font(bf, 18));
        Paragraph element = new Paragraph(underline);
        element.setAlignment(Element.ALIGN_CENTER);
        underline.setUnderline(0.1f, -2f); //0.1 thick, -2 y-location
        document.add(element);

        document.add(new Chunk(" "));
        document.add(new Chunk(" "));

        PdfPTable table = generateTable(reservations);
        document.add(table);

        document.close();
        return fileName;
    }

    private PdfPTable generateTable(List<ReservationDTO> reservations) {
        PdfPTable table = generateTableHeaders();
        generateTableContent(table, reservations);
        return table;
    }

    private PdfPTable generateTableHeaders() {
        PdfPTable table = new PdfPTable(NUM_OF_COLUMNS);
        table.getDefaultCell()
                .setVerticalAlignment(Element.ALIGN_CENTER);
        table.getDefaultCell()
                .setHorizontalAlignment(Element.ALIGN_CENTER);

        List<PdfPCell> headers = new ArrayList<>(NUM_OF_COLUMNS);
        headers.add(getHeaderCell("Appliance"));
        headers.add(getHeaderCell("Who"));
        headers.add(getHeaderCell("When"));
        headers.add(getHeaderCell("Time"));

        headers.forEach(table::addCell);
        return table;
    }

    private PdfPCell getHeaderCell(String text) {
        PdfPCell cell = new PdfPCell(new Paragraph(text));
        cell.setBackgroundColor(BaseColor.GRAY);
        return cell;
    }

    private void generateTableContent(PdfPTable table, List<ReservationDTO> reservations) {
        for (ReservationDTO reservation : reservations) {
            table.addCell(new PdfPCell(new Paragraph(reservation.getAppliance())));
            table.addCell(new PdfPCell(new Paragraph(reservation.getOwner())));
            String when = new Date(reservation.getFrom()).toString();
            table.addCell(new PdfPCell(new Paragraph(when)));
            table.addCell(new PdfPCell(new Paragraph(String.valueOf(reservation.getMinutes()))));
        }
    }

}
