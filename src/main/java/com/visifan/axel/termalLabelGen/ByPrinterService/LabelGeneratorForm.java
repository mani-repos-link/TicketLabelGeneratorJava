package com.visifan.axel.termalLabelGen.ByPrinterService;

import com.google.zxing.WriterException;
import com.visifan.axel.termalLabelGen.QRCode.QRGenerator;

import javax.print.*;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Destination;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class LabelGeneratorForm extends JFrame {
    private JPanel mainPanel;
    private JTabbedPane tabbedPane1;
    private JButton generateButton;
    private JTextField prodCodeTxtField;
    private JTextField prodNameTxtField;
    private JTextField prodDescTxtField;
    private JLabel outputFilePathTxt;
    private JLabel QRLabel;
    private JTextField dpiValueField;
    private final int qrWidth = 200;
    private final int qrHeight = 200;
    private BufferedImage bi;
    private PrintService printerService;
    private final String dataDir;

    public LabelGeneratorForm(String dataDir, String title) {
        this.$$$setupUI$$$();
        this.dataDir = dataDir;
        this.setTitle(title);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(mainPanel);
        this.pack();
        this.setSize(500, 400);
        this.setLocationRelativeTo(null); // open frame in center
        this.initQRLabelDimension();
        this.createGenerateButtonActionEvent();
    }

    private void initQRLabelDimension() {
        Dimension qrLabelDim = new Dimension();
        qrLabelDim.setSize(500, 100);
        QRLabel.setMinimumSize(qrLabelDim);
    }

    private void createGenerateButtonActionEvent() {
        generateButton.addActionListener(actionEvent -> {
            try {
                this.generateBufferedImage();
                String output = prodNameTxtField.getText() + "\n" +
                        prodDescTxtField.getText() + "\n" +
                        prodCodeTxtField.getText();
                this.outputFilePathTxt.setText(output);
                this.showQRCodeOnFrame();
                String fileName = prodNameTxtField.getText().replace(" ", "");
                File outputFile = new File(dataDir + fileName + "-label.xps");
                PrintableLabelGenerator plg = getPrintableLabelGenerator();
                this.generateLabelAndSave(outputFile, plg);
            } catch (WriterException | IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void showQRCodeOnFrame() throws IOException, WriterException {
        BufferedImage bi = this.bi;
        this.QRLabel.setIcon(new ImageIcon(bi));
    }

    public PrintableLabelGenerator getPrintableLabelGenerator() {
        PrintableLabelGenerator plg = new PrintableLabelGenerator();
        plg.setProductName(prodNameTxtField.getText());
        plg.setProductDesc(prodDescTxtField.getText());
        plg.setProductCode(prodCodeTxtField.getText());
        plg.setDPI(Double.parseDouble(dpiValueField.getText()));
        plg.setQRImage(this.bi);
        return plg;
    }


    private void generateBufferedImage() throws IOException, WriterException {
        this.bi = QRGenerator.generateQRCodeToBitMatrix(prodCodeTxtField.getText(), qrWidth, qrHeight);
    }

    private void generateLabelAndSave(File outputFile, PrintableLabelGenerator plg) {
        try {
            Doc doc = new SimpleDoc(plg, DocFlavor.SERVICE_FORMATTED.PRINTABLE, null);
            PrintRequestAttributeSet attributes = new HashPrintRequestAttributeSet();
            attributes.add(new Destination(outputFile.toURI()));
            DocPrintJob job = printerService.createPrintJob();
            job.print(doc, attributes);
            outputFilePathTxt.setText(outputFile.getPath());
            Desktop.getDesktop().open(outputFile);
        } catch (Exception e) {
            System.out.println("kaboom" + e);
            e.printStackTrace();
        }
    }

    public void setPrinterService(PrintService printerService) {
        this.printerService = printerService;
    }

    private void $$$setupUI$$$() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(), "Ticket Label Generator", TitledBorder.CENTER, TitledBorder.TOP, null, null));
        tabbedPane1 = new JTabbedPane();
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        mainPanel.add(tabbedPane1, gbc);
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridBagLayout());
        tabbedPane1.addTab("Normal Label", panel1);
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridBagLayout());
        panel2.setEnabled(false);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        panel1.add(panel2, gbc);
        final JLabel label1 = new JLabel();
        label1.setText("Product Name");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 4;
        gbc.anchor = GridBagConstraints.WEST;
        panel2.add(label1, gbc);
        final JLabel label2 = new JLabel();
        label2.setText("Product desc");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 3;
        gbc.anchor = GridBagConstraints.WEST;
        panel2.add(label2, gbc);
        prodDescTxtField = new JTextField();
        prodDescTxtField.setText("");
        gbc = new GridBagConstraints();
        gbc.gridx = 4;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel2.add(prodDescTxtField, gbc);
        prodNameTxtField = new JTextField();
        prodNameTxtField.setText("");
        gbc = new GridBagConstraints();
        gbc.gridx = 4;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel2.add(prodNameTxtField, gbc);
        final JLabel label3 = new JLabel();
        label3.setText("Product code");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.WEST;
        panel2.add(label3, gbc);
        prodCodeTxtField = new JTextField();
        prodCodeTxtField.setText("");
        gbc = new GridBagConstraints();
        gbc.gridx = 4;
        gbc.gridy = 2;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel2.add(prodCodeTxtField, gbc);
        generateButton = new JButton();
        generateButton.setText("Generate");
        gbc = new GridBagConstraints();
        gbc.gridx = 4;
        gbc.gridy = 5;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel2.add(generateButton, gbc);
        final JLabel label4 = new JLabel();
        label4.setText("Output");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.anchor = GridBagConstraints.WEST;
        panel2.add(label4, gbc);
        outputFilePathTxt = new JLabel();
        outputFilePathTxt.setText("\"\"");
        gbc = new GridBagConstraints();
        gbc.gridx = 4;
        gbc.gridy = 6;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        panel2.add(outputFilePathTxt, gbc);
        QRLabel = new JLabel();
        QRLabel.setHorizontalAlignment(0);
        QRLabel.setHorizontalTextPosition(0);
        QRLabel.setText("");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 5;
        gbc.weighty = 1.0;
        panel2.add(QRLabel, gbc);
        dpiValueField = new JTextField();
        dpiValueField.setText("300");
        gbc = new GridBagConstraints();
        gbc.gridx = 4;
        gbc.gridy = 4;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel2.add(dpiValueField, gbc);
        final JLabel label5 = new JLabel();
        label5.setText("DPI");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.WEST;
        panel2.add(label5, gbc);
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridBagLayout());
        tabbedPane1.addTab("Rotate Label", panel3);
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        panel3.add(panel4, gbc);
        final JLabel label6 = new JLabel();
        label6.setText("Coming Soon");
        panel4.add(label6);
        label1.setLabelFor(prodNameTxtField);
        label2.setLabelFor(prodDescTxtField);
        label3.setLabelFor(prodCodeTxtField);
        label5.setLabelFor(prodCodeTxtField);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mainPanel;
    }


}
