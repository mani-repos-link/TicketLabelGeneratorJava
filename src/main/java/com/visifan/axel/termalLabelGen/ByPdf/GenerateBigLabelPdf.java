package com.visifan.axel.termalLabelGen.ByPdf;

import com.google.zxing.WriterException;
import com.visifan.axel.termalLabelGen.QRCode.QRGenerator;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import java.io.IOException;

// Then you need to create a rectangle that measures 3 by 5 inches.
// As the measurement unit in PDF is the user unit, and as 1 inch corresponds with 72
// user units, the rectangle will be 3 x 72 user units wide and 5 by 72 user units high;

public class GenerateBigLabelPdf {
    static int pageWidth;
    static int pageHeight;
    static int qrCodeSize = 90;

    public static void main(String[] args) throws IOException, WriterException {
        PDDocument document = new PDDocument();
        pageWidth = cmToPdfUnit(10);
        pageHeight = cmToPdfUnit(4.8);
        PDRectangle rec = new PDRectangle(pageWidth, pageHeight);
        System.out.println(rec.getWidth() + "X" + rec.getHeight());

        // label 1
        PDPage page = new PDPage(rec);
        PDPageContentStream contentStream = new PDPageContentStream(document, page);
        generateStream(contentStream, document,
                "HYC-150", "(DCP-14512145)", "A-0001-000161_05-06_2211800001");
        document.addPage(page);

        // label 2
        page = new PDPage(rec);
        contentStream = new PDPageContentStream(document, page);
        generateStream(contentStream, document,
                "HYC-150", "(DCP-14512145)", "A-0001-000161_05-06_2211800001");
        document.addPage(page);

        // label 3
        page = new PDPage(rec);
        contentStream = new PDPageContentStream(document, page);
        generateStream(contentStream, document,
                "HYC-150", "(DCP-14512145)", "A-0001-000161_05-06_2211800001");
        document.addPage(page);


        document.save("./data/pdfBoxImage2.pdf");
        document.close();
    }

    private static void generateStream(
            PDPageContentStream contentStream, PDDocument document,
            String productName, String prodDesc, String prodCode
    ) throws IOException, WriterException {
        int borderMargin = 2;
        PDImageXObject pdImageXObject = LosslessFactory.createFromImage(
                document,
                QRGenerator.generateQRCodeToBitMatrix(prodCode, qrCodeSize, qrCodeSize)
        );
        contentStream.addRect(borderMargin, borderMargin, pageWidth - borderMargin*2, pageHeight-borderMargin*2);
        contentStream.drawImage(pdImageXObject, 5, pageHeight - qrCodeSize - 5);
        contentStream.stroke();

        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 16);
        contentStream.beginText();
        contentStream.newLineAtOffset(qrCodeSize + 30, pageHeight - 30);
        contentStream.showText(productName);
        contentStream.endText();

        contentStream.beginText();
        contentStream.setFont(PDType1Font.HELVETICA, 10);
        contentStream.newLineAtOffset(qrCodeSize + 30, pageHeight - 50);
        contentStream.showText(prodDesc);
        contentStream.endText();


        contentStream.beginText();
        contentStream.setFont(PDType1Font.HELVETICA, 10);
        contentStream.newLineAtOffset(10, 15);
        contentStream.showText(prodCode);
        contentStream.endText();
        contentStream.close();
    }


    private static double cmToInch(double cm) {
        return cm*0.393701;
    }

    private static int cmToPdfUnit(double cm) {
        System.out.println("cmToInch(cm)(" +cm+ ") " + cmToInch(cm) + " = " + (cmToInch(cm)*72));
        return (int) (cmToInch(cm)*72);
    }

    private static int cmToDPI(double cm) {
        return (int) ((cm * 300) / 2.54);
    }
}
