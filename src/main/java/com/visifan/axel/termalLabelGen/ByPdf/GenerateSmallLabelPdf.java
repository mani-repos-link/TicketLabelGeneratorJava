package com.visifan.axel.termalLabelGen.ByPdf;


import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.visifan.axel.termalLabelGen.QRCode.QRGenerator;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

// Then you need to create a rectangle that measures 3 by 5 inches.
// As the measurement unit in PDF is the user unit, and as 1 inch corresponds with 72
// user units, the rectangle will be 3 x 72 user units wide and 5 by 72 user units high;

public class GenerateSmallLabelPdf {
    static final int pageWidth = cmToDPI(2.1);
    static final int pageHeight = cmToDPI(3.6);

    public static void main(String[] args) throws Exception {
        String dest = "./data/itext.pdf";
        int qrCodeSize = 43;
        String[] codes = new String[]{
                "A-0001-000161_05-06_2211800001",
                "A-0001-000161_05-06_2211800002",
                "A-0001-000161_05-06_2211800003"
        };
        FileOutputStream fos = new FileOutputStream(dest);
        File file = new File(dest);
        Document document = new Document();
        PdfWriter pdfWriter = PdfWriter.getInstance(document, fos);
        Rectangle pageSize = new Rectangle(pageWidth, pageHeight);
        document.setPageSize(pageSize);
        document.setMargins(1, 1, 1, 1);
        document.open();
        for (String code : codes) {
            generateLabel(document, code, qrCodeSize);
            document.newPage();
        }
        document.close();
        manipulatePdf(dest, "./data/rotated-labels.pdf");
    }

    private static void generateLabel(Document document, String code, int qrCodeSize) throws Exception {
        Image image = getQRImage(code, qrCodeSize);
        image.setAlignment(Element.ALIGN_CENTER);
        image.setRotationDegrees(90);
        document.add(image);
        Font f = new Font(Font.FontFamily.TIMES_ROMAN,6.0f, Font.NORMAL);
        Paragraph p = new Paragraph("A-0001-000161_05-06_2211800002", f);
        p.setAlignment(Element.ALIGN_CENTER);
        document.add(p);
    }

    public static void manipulatePdf(String src, String dest)
            throws IOException, DocumentException {
        PdfReader reader = new PdfReader(src);
        int n = reader.getNumberOfPages();
        int rot;
        PdfDictionary pageDict;
        for (int i = 1; i <= n; i++) {
            rot = reader.getPageRotation(i);
            pageDict = reader.getPageN(i);
            pageDict.put(PdfName.ROTATE, new PdfNumber(-90));
        }
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(dest));
        stamper.close();
        reader.close();
    }

    private static Image getQRImage(String data, int qrCodeSize) throws Exception {
        BufferedImage bi = QRGenerator.generateQRCodeToBitMatrix("Hello wr", qrCodeSize, qrCodeSize);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(bi, "png", baos);
        return Image.getInstance(baos.toByteArray());
    }
    private static double cmToInch(double cm) {
        return cm*0.393701;
    }

    private static int cmToPdfUnit(double cm) {
        System.out.println("cmToInch(cm)(" +cm+ ") " + cmToInch(cm) + " = " + (cmToInch(cm)*72));
        return (int) (cmToInch(cm)*72);
    }
    private static int cmToDPI(double cm) {
        // return (int) ((cm * 300) / 2.54);
        return cmToPdfUnit(cm);
    }
}
