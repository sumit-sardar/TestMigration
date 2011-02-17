package com.ctb.common.tools;


import com.ctb.sax.util.*;


/**
 * Created by IntelliJ IDEA.
 * User: mwshort
 * Date: Jul 31, 2003
 * Time: 11:23:12 AM
 * To change this template use Options | File Templates.
 */
public class PDFGeneratorFactory {

    public static PDFGenerator create() {
        return new DefaultPDFGenerator();
    }

    public static PDFGenerator create(XSLDocType type) {

        return new DefaultPDFGenerator(type);
    }

}
