package com.visifan.axel.termalLabelGen;

import javax.print.*;
import java.awt.print.PrinterJob;
import java.io.File;

public class TicketLabelGenerator {
    public static final float DPI = 300;
//    private static final String dataDir = "." + File.separator + "data" + File.separator;
    private static final String dataDir = "." + File.separator;

    public static void main(String[] args) {
        PrintService service = ThermalPrinter.getPrinterService();
        PrinterJob.lookupPrintServices();
        if (service != null) {
            LabelGeneratorForm lgf = new LabelGeneratorForm(dataDir, "Ticket Label Generator");
            lgf.setPrinterService(service);
            lgf.setVisible(true);
        } else {
            System.out.println("No Printer found!");
        }
    }



    private static void generateLabelAndPrint() {

    }

}
