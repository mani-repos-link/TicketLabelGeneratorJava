package com.visifan.axel.termalLabelGen.ByPrinterService;

import javax.print.*;
import java.awt.print.PrinterJob;
import java.io.*;

public class TicketLabelGenerator {
    public static final float DPI = 300;
//    private static final String dataDir = "." + File.separator + "data" + File.separator;
    private static final String dataDir = "." + File.separator;

    public static void main(String[] args) throws PrintException, IOException {
        PrintService service = ThermalPrinter.getPrinterService();
        PrinterJob.lookupPrintServices();
//        InputStream stream = new BufferedInputStream(new FileInputStream("image.epl"));
//        InputStream stream = new BufferedInputStream(
//                new ByteArrayInputStream("Hello World".getBytes()));
//        DocFlavor flavor = DocFlavor.INPUT_STREAM.AUTOSENSE;
//        Doc myDoc = new SimpleDoc(stream, flavor, null);
//        DocPrintJob job = service.createPrintJob();
//        job.print(myDoc, null);

//        DocPrintJob job = service.createPrintJob();
//        String commands = "^XA\n\r^MNM\n\r^FO050,50\n\r^B8N,100,Y,N\n\r^FD1234567\n\r^FS\n\r^PQ3\n\r^XZ";
//        DocFlavor flavor = DocFlavor.BYTE_ARRAY.AUTOSENSE;
//        Doc doc = new SimpleDoc(commands.getBytes(), flavor, null);
//        job.print(doc, null);

//        if (service != null) {
//            LabelGeneratorForm lgf = new LabelGeneratorForm(dataDir, "Ticket Label Generator");
//            lgf.setPrinterService(service);
//            lgf.setVisible(true);
//        } else {
//            System.out.println("No Printer found!");
//        }
    }



    private static void generateLabelAndPrint() {

    }

}


