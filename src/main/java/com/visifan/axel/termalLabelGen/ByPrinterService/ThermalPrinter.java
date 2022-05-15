package com.visifan.axel.termalLabelGen.ByPrinterService;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;

public class ThermalPrinter {


    public static PrintService getPrinterService() {
        try {
            PrintService[] printServices = PrintServiceLookup.lookupPrintServices(null, null);
            System.out.println("Printers List:");
            for (int i = 0; i < printServices.length; i++) {
                PrintService service = printServices[i];
                System.out.println("[" + ( i + 1) + "] " + service.getName());
            }
            return printServices[1]; // TODO: improve it
            // job.setPrintService(printService);
        } catch (Exception e) {
        }
        return null;
    }

}
