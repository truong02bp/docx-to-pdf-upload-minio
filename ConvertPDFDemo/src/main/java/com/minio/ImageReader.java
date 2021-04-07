package com.minio;

import com.pdftron.common.PDFNetException;
import com.pdftron.sdf.SDFDoc;

public class ImageReader {
    static String input_path = "/";
    static String output_path = "/home/huy/Desktop/";
    public static void main(String[] args) throws PDFNetException {
        com.pdftron.pdf.PDFNet.initialize();
        com.pdftron.pdf.PDFNet.setResourcesPath("../../../Resources");

        // first the one-line conversion interface
//        simpleDocxConvert("/home/huy/Desktop/ConvertPDF/file/test3.docx", "/home/huy/Desktop/test3.pdf");

        // then the more flexible line-by-line interface
        flexibleDocxConvert("/home/huy/Desktop/ConvertPDF/file/test3.docx", "/home/huy/Desktop/test3_flexible.pdf");
//
//        // conversion of RTL content
//        flexibleDocxConvert("factsheet_Arabic.docx", "factsheet_Arabic.pdf");
        com.pdftron.pdf.PDFNet.terminate();

    }
    public static void flexibleDocxConvert(String inputFilename, String outputFilename) {
        try {
            com.pdftron.pdf.OfficeToPDFOptions options = new com.pdftron.pdf.OfficeToPDFOptions();
            // create a conversion object -- this sets things up but does not yet
            // perform any conversion logic.
            // in a multithreaded environment, this object can be used to monitor
            // the conversion progress and potentially cancel it as well
            com.pdftron.pdf.DocumentConversion conversion = com.pdftron.pdf.Convert.streamingPdfConversion(
                    inputFilename, options);

            System.out.println(inputFilename + ": " + Math.round(conversion.getProgress() * 100.0)
                    + "% " + conversion.getProgressLabel());

            // actually perform the conversion
            while (conversion.getConversionStatus() == com.pdftron.pdf.DocumentConversion.e_incomplete) {
                conversion.convertNextPage();
                System.out.println(inputFilename + ": " + Math.round(conversion.getProgress() * 100.0)
                        + "% " + conversion.getProgressLabel());
            }

            if (conversion.tryConvert() == com.pdftron.pdf.DocumentConversion.e_success) {
                int num_warnings = conversion.getNumWarnings();

                // print information about the conversion
                for (int i = 0; i < num_warnings; ++i) {
                    System.out.println("Warning: " + conversion.getWarningString(i));
                }

                // save the result
                com.pdftron.pdf.PDFDoc doc = conversion.getDoc();
                doc.save(outputFilename, SDFDoc.SaveMode.INCREMENTAL, null);
                // output PDF doc

                // done
                System.out.println("Done conversion " + outputFilename);
            } else {
                System.out.println("Encountered an error during conversion: " + conversion.getErrorString());
            }
        } catch (PDFNetException e) {
            System.out.println("Unable to convert MS Office document, error:");
            e.printStackTrace();
            System.out.println(e);
        }
    }
}