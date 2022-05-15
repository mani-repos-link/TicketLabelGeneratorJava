package com.visifan.axel.termalLabelGen.ByZPL;

import com.visifan.axel.termalLabelGen.ByPrinterService.ThermalPrinter;
import fr.w3blog.zpl.constant.ZebraFont;
import fr.w3blog.zpl.model.ZebraLabel;
import fr.w3blog.zpl.model.element.ZebraNativeZpl;

import javax.print.*;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class GenerateLabelWithZPL {

    public static void main(String[] args) throws PrintException, IOException, InterruptedException {
        PrintService service = ThermalPrinter.getPrinterService();
        PrinterJob.lookupPrintServices();
        ZebraLabel zebraLabel = new ZebraLabel(425, 248);
        zebraLabel.setDefaultZebraFont(ZebraFont.ZEBRA_ZERO);
//        ZebraNativeZpl qrCode = new ZebraBarCode39("^FO30,20^BQN,2,4^FDQA,HELLO^FS\n\r");
        ZebraNativeZpl qrCode = new ZebraNativeZpl("^FO30,20^BQN,2,4^FDQA,HELLO^FS\n\r");
        zebraLabel.addElement(qrCode);
        zebraLabel.addElement(new ZebraNativeZpl("^FO200,40^A0,20^FDTEST LABEL^FS\n\r"));
        zebraLabel.addElement(new ZebraNativeZpl("^FO200,70^A0,14^FD(My Code wth)^FS\n\r"));
        zebraLabel.addElement(new ZebraNativeZpl("^FO30,160^A0,14^FDA-0001-000161_05-06_2211800002^FS\n\r"));
        String commands = zebraLabel.getZplCode();
        commands += "\n" + commands;
        commands += "\n" + commands;
        commands += "\n" + commands;
        System.out.println(commands);
        var uri = URI.create("http://api.labelary.com/v1/printers/12dpmm/labels/1.41732x0.826772/");
        var request = HttpRequest.newBuilder(uri)
                .header("Accept", "application/pdf") // omit this line to get PNG images back
                .POST(HttpRequest.BodyPublishers.ofString(commands))
                .build();
        var client = HttpClient.newHttpClient();
        var response = client.send(request, HttpResponse.BodyHandlers.ofByteArray());
        var body = response.body();
        if (response.statusCode() == 200) {
            var file = new File("./data/label3.pdf"); // change file name for PNG images
            Files.write(file.toPath(), body);
        } else {
            var errorMessage = new String(body, StandardCharsets.UTF_8);
            System.out.println(errorMessage);
        }
    }
}
