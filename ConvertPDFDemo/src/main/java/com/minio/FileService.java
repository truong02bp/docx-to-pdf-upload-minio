package com.minio;

import com.pdftron.common.PDFNetException;
import com.pdftron.pdf.Convert;
import com.pdftron.pdf.PDFDoc;
import com.pdftron.sdf.SDFDoc;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

@Service
public class FileService {

    @Autowired
    MinioService minioService;

    public byte[] toPdf(String url) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        com.pdftron.pdf.PDFNet.initialize();
        com.pdftron.pdf.PDFNet.setResourcesPath("../../../Resources");
        try {
            PDFDoc pdfdoc = new PDFDoc();
            File file = new File("file/test.docx");
            System.out.println(file.getPath());
            FileUtils.writeByteArrayToFile(file, minioService.getFile(url));
            Convert.officeToPdf(pdfdoc, file.getPath(), null);
            pdfdoc.save(byteArrayOutputStream, SDFDoc.SaveMode.INCREMENTAL, null);
            file.delete();
        } catch (PDFNetException | IOException e) {
            System.out.println("Unable to convert MS Office document, error:");
            e.printStackTrace();
            System.out.println(e);
        }
        com.pdftron.pdf.PDFNet.terminate();
        return byteArrayOutputStream.toByteArray();
    }

//    public static void flexibleDocxConvert(String inputFilename) {
//        try {
//            OfficeToPDFOptions options = new OfficeToPDFOptions();
//            // create a conversion object -- this sets things up but does not yet
//            // perform any conversion logic.
//            // in a multithreaded environment, this object can be used to monitor
//            // the conversion progress and potentially cancel it as well
//            DocumentConversion conversion = Convert.streamingPdfConversion(
//                    inputFilename, options);
//
//            System.out.println(inputFilename + ": " + Math.round(conversion.getProgress() * 100.0)
//                    + "% " + conversion.getProgressLabel());
//
//            // actually perform the conversion
//            while (conversion.getConversionStatus() == DocumentConversion.e_incomplete) {
//                conversion.convertNextPage();
//                System.out.println(inputFilename + ": " + Math.round(conversion.getProgress() * 100.0)
//                        + "% " + conversion.getProgressLabel());
//            }
//
//            if (conversion.tryConvert() == DocumentConversion.e_success) {
//                int num_warnings = conversion.getNumWarnings();
//
//                // print information about the conversion
//                for (int i = 0; i < num_warnings; ++i) {
//                    System.out.println("Warning: " + conversion.getWarningString(i));
//                }
//
//                // save the result
//                PDFDoc doc = conversion.getDoc();
//                doc.save(outputFilename, SDFDoc.SaveMode.INCREMENTAL, null);
//                // output PDF doc
//
//                // done
//                System.out.println("Done conversion " + outputFilename);
//            } else {
//                System.out.println("Encountered an error during conversion: " + conversion.getErrorString());
//            }
//        } catch (PDFNetException e) {
//            System.out.println("Unable to convert MS Office document, error:");
//            e.printStackTrace();
//            System.out.println(e);
//        }
//    }
}
