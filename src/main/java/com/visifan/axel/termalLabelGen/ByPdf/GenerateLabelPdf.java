package com.visifan.axel.termalLabelGen.ByPdf;

import com.google.zxing.WriterException;
import com.itextpdf.text.DocumentException;
import com.visifan.axel.termalLabelGen.QRCode.QRGenerator;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.util.Matrix;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

//        Then you need to create a rectangle that measures 3 by 5 inches.
//        As the measurement unit in PDF is the user unit, and as 1 inch corresponds with 72
//        user units, the rectangle will be 3 x 72 user units wide and 5 by 72 user units high;

public class GenerateLabelPdf {

    public static void main(String[] args) throws IOException, WriterException {
        PDDocument document = new PDDocument();
        PDRectangle rec = new PDRectangle(cmToDPI(2.1), cmToDPI(3.6));
        System.out.println(rec.getWidth() + "X" + rec.getHeight());
        PDPage page = new PDPage(rec);
        int qrCodeSize = 50;
        PDImageXObject pdImageXObject = LosslessFactory.createFromImage(document,
                QRGenerator.generateQRCodeToBitMatrix("Hello wr", qrCodeSize, qrCodeSize));
        PDPageContentStream contentStream = new PDPageContentStream(document, page);

        int borderMargin = 2;
        contentStream.addRect(borderMargin, borderMargin, rec.getWidth() - borderMargin*2, rec.getHeight()-borderMargin*2);
         contentStream.drawImage(pdImageXObject, 2, rec.getHeight() - qrCodeSize - borderMargin);
        contentStream.stroke();
        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 10);
//        Paragraph paragraph = new Paragraph();
//        paragraph.addText(
//                "This is some slightly longer text wrapped to a width of 100.",
//                11, PDType1Font.HELVETICA);
//        paragraph.setMaxWidth(100);
//        document.add(paragraph);

//        drawMultiLineText("A-0001-000161_05-06_2211800002", 5, 25, cmToDPI(2.1) - 5, contentStream, 10, 10);
//        contentStream.beginText();
//        // For adjusting location of text on page you need to adjust this two values
//        // contentStream.newLineAtOffset( 60,rec.getHeight() - borderMargin - 10);
//        contentStream.showText("HYP-01 dsfa dfasdf  ");
//        contentStream.endText();
        contentStream.close();

        document.addPage(page);
        document.save("./data/pdfBoxImage2.pdf");
        document.close();
    }
    private static double cmToInch(double cm) {
        return cm*0.393701;
    }
    public void createPdf(String dest) throws IOException, DocumentException {
        PDDocument doc = new PDDocument();
        PDPage page = new PDPage();
        doc.addPage(page);
        PDPageContentStream cos = new PDPageContentStream(doc, page);
        cos.transform(Matrix.getRotateInstance(-Math.PI / 62, 100, 650));
        cos.moveTo(0, 0);
        cos.lineTo(125, 0);
        cos.stroke();
        cos.beginText();
        String text = "0.72";
        cos.newLineAtOffset(50, 5);
        cos.setFont(PDType1Font.HELVETICA_BOLD, 12);
        cos.showText(text);
        cos.endText();
        cos.close();
        doc.save("./data/TextOnLine.pdf");
        doc.close();
    }

    private static void drawMultiLineText(
            String text, int x, int y, int allowedWidth,
            PDPageContentStream contentStream,
            int fontSize, int lineHeight) throws IOException {
        List<String> lines = new ArrayList<String>();
        String myLine = "";
        // get all words from the text
        String[] words = text.split(" ");
        PDFont font = PDType1Font.HELVETICA;
        for(String word : words) {
            if(!myLine.isEmpty()) myLine += " ";
            // test the width of the current line + the current word
            int size = (int) (fontSize * font.getStringWidth(myLine + word) / 1000);
            if(size > allowedWidth) {
                lines.add(myLine); // if the line would be too long with the current word, add the line without the current word
                myLine = word; // and start a new line with the current word
            } else
                myLine += word; // if the current line + the current word would fit, add the current word to the line
        }
        // add the rest to lines
        lines.add(myLine);
        for(String line : lines) {
            contentStream.beginText();
            contentStream.setFont(font, fontSize);
            contentStream.moveTextPositionByAmount(x, y);
            contentStream.drawString(line);
            contentStream.endText();
            y -= lineHeight;
        }
    }

    private static int cmToPdfUnit(double cm) {
        System.out.println("cmToInch(cm)(" +cm+ ") " + cmToInch(cm) + " = " + (cmToInch(cm)*72));
        return (int) (cmToInch(cm)*72);
    }
    private static int cmToDPI(double cm) {
//        return (int) ((cm * 300) / 2.54);
        return cmToPdfUnit(cm);
    }
}
