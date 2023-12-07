// ReportGenerator.java
package backend;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileOutputStream;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ReportGenerator {
    public static void generateReport() {
        try {
            String date = new SimpleDateFormat("yyyy-MM-dd-mm-ss").format(new Date());
            Paragraph dateParagraph = new Paragraph("Report Date: " + date);

            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream("InventoryReport_" + date + ".pdf"));
            document.open();

            // Add the report date
            document.add(dateParagraph);

            // Add a blank line
            document.add(new Paragraph(" "));

            // Add the heading
            Paragraph heading = new Paragraph("Inventory Items", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16));
            document.add(heading);

            // Add a blank line
            document.add(new Paragraph(" "));

            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/SIS", "root", "1234");
            PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM Items");
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String item = rs.getString("Item");
                int quantity = rs.getInt("Quantity");
                Paragraph paragraph = new Paragraph(item + ": " + quantity);
                document.add(paragraph);
            }

            document.close();
            pstmt.close();
            conn.close();
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }
}
