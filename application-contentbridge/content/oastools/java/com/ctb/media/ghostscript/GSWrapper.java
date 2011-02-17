package com.ctb.media.ghostscript;


import com.ctb.common.tools.SystemException;

import com.ctb.util.Pipe;
import com.ctb.media.ghostscript.GSExecutor;
import com.ctb.media.ghostscript.GSExecutorFactory;
import com.ctb.util.TemporaryDirectory;

import java.io.*;


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
            throw new SystemException(e.getMessage(),e);
        } finally {
            TemporaryDirectory.delete(tempDir);
        }

    }

}
