package com.visifan.axel.termalLabelGen;

import javax.swing.*;
import java.awt.*;
import java.awt.print.*;

public class RotatePrintableLabelGenerator implements Printable {
    public static int dpi = 300;
    double labelLength = 3.6;
    double labelHeight = 2.1;

    @Override
    public int print(Graphics g, PageFormat pf, int page) throws PrinterException {
        Paper paper = pf.getPaper();
        int paperWidth = (int) cmToPixel(labelLength);
        int paperHeight = (int) cmToPixel(labelHeight);
        System.out.println("Paper Size(px): " + paperWidth + "X" + paperHeight);
        paper.setSize(paperWidth, paperHeight);
        ImageIcon icon = new ImageIcon("./data/qr.png");
        int result = NO_SUCH_PAGE;
        if (page == 0) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.translate((int) pf.getImageableX(), (int) pf.getImageableY());
            g2d.drawRect(0, 0, paperWidth, paperHeight);
            Font normalFont = new Font("Monospaced", Font.PLAIN, 14);
            Font boldFont = new Font("Monospaced", Font.BOLD, 16);
            int yStartIndex = 16; // start index
            int xStartIndex = 16; // start index
            int QRCodeSize = 100;
            int QRCodeImageRightMargin = 20;
            int QRCodeSideStartIndex = QRCodeSize + QRCodeImageRightMargin + xStartIndex;
            int yShift = 25; // shift y-axis margin from top
            try {
                g2d.setFont(normalFont);
                g2d.drawImage(icon.getImage(), xStartIndex, yStartIndex, QRCodeSize, QRCodeSize, null); // draw QR
                yStartIndex += yShift;
                g2d.setFont(boldFont);
                g2d.drawString("HYC-150", QRCodeSideStartIndex, yStartIndex);
                yStartIndex += yShift;
                g2d.setFont(normalFont);
                g2d.drawString("(102642) ohne AC", QRCodeSideStartIndex, yStartIndex);
                yStartIndex += yShift + (QRCodeSize - yStartIndex);
                g2d.drawString("A-0001-000161_04-06_2211300001", xStartIndex + 5, yStartIndex);
                drawRotate(g2d, paperWidth + 50 - QRCodeSideStartIndex, 70, 90, "Test");


            } catch (Exception e) {
                e.printStackTrace();
            }
            result = PAGE_EXISTS;
        }
        return result;
    }

    public static void drawRotate(Graphics2D g2d, double x, double y, int angle, String text) {
        g2d.translate(x, y);
        g2d.rotate(-Math.toRadians(angle));
        g2d.drawString(text, 0, 0);
        g2d.rotate(Math.toRadians(angle));
        g2d.translate(-x, -y);
    }

    public static double cmToPixel(double cm) {
        return (cm * dpi) / 2.54;
    }
}