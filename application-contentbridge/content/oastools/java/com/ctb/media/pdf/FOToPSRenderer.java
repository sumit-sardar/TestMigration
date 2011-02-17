package com.ctb.media.pdf;

import org.apache.fop.apps.Driver;
import org.apache.fop.apps.FOPException;
import org.xml.sax.InputSource;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;

import com.ctb.common.tools.SystemException;

/**
 * User: mwshort
 * Date: Mar 21, 2004
 * Time: 7:29:49 AM
 * 
 *
 */
public class FOToPSRenderer {

    public FOToPSRenderer() {

    }
    public void process(InputStream is,OutputStream os) {
        Driver driver = new Driver();
        //Setup logging here: driver.setLogger(...
        driver.setRenderer(Driver.RENDER_PS);
        driver.setInputSource(new InputSource(is));
        driver.setOutputStream(os);
        try {
            driver.run();
        } catch (IOException e) {
            throw new SystemException(e.getMessage(),e);
        } catch (FOPException e) {
            throw new SystemException(e.getMessage(),e);
        }
    }
}
