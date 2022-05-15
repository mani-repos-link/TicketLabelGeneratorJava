package com.visifan.axel.termalLabelGen.ByPrinterService;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.awt.print.PrinterException;

public class PrintableLabelGenerator implements Printable {
    public static double dpi = 300;
    double labelLength = 3.6;
    double labelHeight = 2.1;

    private String productName = "";
    private String productDesc = "";
    private String productCode = "";
    private BufferedImage bufferedImage;

    public void setDPI(double dp) {
        dpi = dp;
    }

    public void setQRImage(BufferedImage bi) {
        this.bufferedImage = bi;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setProductDesc(String productDesc) {
        this.productDesc = productDesc;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }


    @Override
    public int print(Graphics g, PageFormat pf, int page) throws PrinterException {
        Paper paper = pf.getPaper();
        int paperWidth = (int) cmToPixel(labelLength);
        int paperHeight = (int) cmToPixel(labelHeight);
        System.out.println("Paper Size(px): " + paperWidth + "X" + paperHeight);
        paper.setSize(paperWidth, paperHeight);
        ImageIcon icon = new ImageIcon(this.bufferedImage);
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
                g2d.drawString(productName, QRCodeSideStartIndex, yStartIndex);
                yStartIndex += yShift;
                g2d.setFont(normalFont);
                g2d.drawString(productDesc, QRCodeSideStartIndex, yStartIndex);
                yStartIndex += yShift + (QRCodeSize - yStartIndex);
                g2d.drawString(productCode, xStartIndex + 5, yStartIndex);
            } catch (Exception e) {
                e.printStackTrace();
            }
            result = PAGE_EXISTS;
        }
        return result;
    }


    public static double cmToPixel(double cm) {
        return (cm * dpi) / 2.54;
    }
}