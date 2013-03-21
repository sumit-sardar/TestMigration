package com.ctb.contentBridge.core.publish.media.ghostscript;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.ctb.contentBridge.core.exception.SystemException;
import com.ctb.contentBridge.core.publish.sax.util.TemporaryDirectory;
import com.ctb.contentBridge.core.util.Pipe;



public class GSWrapper {

    public GSWrapper() {

    }

    public void psToPdf(InputStream psStream, OutputStream os){
        File tempDir = null;

        try {
            //Create temporary directory
            tempDir = TemporaryDirectory.createTempDir(new File("tempdir"),"GHS");

            //create ghostscript input file
            File psFile = File.createTempFile("ps_", ".ps", tempDir);
            FileOutputStream fos = new FileOutputStream(psFile);
            Pipe pipeOut = new Pipe(psStream,fos);
            pipeOut.run();
            fos.close();
            psStream.close();

            //create ghostscript output file
            File pdfFile = new File(psFile.getPath() + ".pdf");

            GSExecutor executor = GSExecutorFactory.create();
            executor.execute(psFile,pdfFile);


            FileInputStream fis = new FileInputStream(pdfFile);
            Pipe pipeIn = new Pipe(fis,os);
            pipeIn.run();
            fis.close();

        } catch (IOException e) {
            throw new SystemException(e.getMessage());
        } finally {
            TemporaryDirectory.delete(tempDir);
        }

    }

}
