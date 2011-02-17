package com.ctb.common.tools.oasmedia;

/**
 * User: mwshort
 * Date: Dec 12, 2003
 * Time: 11:39:23 AM
 * 
 *
 */
public class OASMedia {
	

    private char[] assessmentXML;
    private byte[] assessmentMovie;
    private byte[] assessmentMovieWithAnswerKeys;
    private byte[] pdf;
    private byte[] pdfAK;
    private byte[] pdfCRAK;
    private byte[] pdfCRIB;


    public char[] getAssessmentXML() {
        return assessmentXML;
    }

    public void setAssessmentXML(char[] assessmentXML) {
        this.assessmentXML = assessmentXML;
    }

    public byte[] getAssessmentMovie() {
        return assessmentMovie;
    }

    public void setAssessmentMovie(byte[] assessmentMovie) {
        this.assessmentMovie = assessmentMovie;
    }

    public byte[] getAssessmentMovieWithAnswerKeys() {
        return assessmentMovieWithAnswerKeys;
    }

    public void setAssessmentMovieWithAnswerKeys(byte[] assessmentMovieWithAnswerKeys) {
        this.assessmentMovieWithAnswerKeys = assessmentMovieWithAnswerKeys;
    }

    public byte[] getPDF() {
        return pdf;
    }

    public void setPDF(byte[] pdf) {
        this.pdf = pdf;
    }

    public byte[] getPDFAnswerKey() {
        return pdfAK;
    }

    public void setPDFAnswerKey(byte[] pdfAK) {
        this.pdfAK = pdfAK;
    }

    public byte[] getPDFAnswerKeyCROnly() {
        return pdfCRAK;
    }

    public void setPDFAnswerKeyCROnly(byte[] pdfCRAK) {
        this.pdfCRAK = pdfCRAK;
    }

    public byte[] getPDFCROnly() {
        return pdfCRIB;
    }

    public void setPDFCROnly(byte[] pdfCRIB) {
        this.pdfCRIB = pdfCRIB;
    }




//    public boolean publishMedia(org.jdom.Document inDoc, int itemSetId, int userIDint, boolean hasCRItem ) throws Exception
//    {
//        final String SUB_TRACE = TRACE_TAG + ".publishMedia()";
//        boolean returnBoolean = false;
//        GrndsTrace.enterScope( SUB_TRACE );
//        try
//        {
//            GrndsConfiguration gc = GrndsConfiguration.getInstance();
//            String cimDirectory = gc.getProperty("lexington",
//            "testpublish.directory.cim");
//            String genPdf= gc.getProperty("lexington",
//            "testpublish.script.generatepdf.file");
//            String genSwf = gc.getProperty("lexington",
//            "testpublish.script.generateswf.file");
//
//            String xmlNm = cimDirectory + "compdisp.xml";
//            String ibSwfNm = cimDirectory + "cab_ib.swf";
//            String akSwfNm = cimDirectory + "cab_ak.swf";
//            String pdfNm = cimDirectory + "compdisp_IBPDF.pdf";
//            // CR only media
//            String AKpdfNm = cimDirectory + "compdisp_AKPDF.pdf";
//            String CRAKpdfNm = null;
//            String CRIBpdfNm = null;
//            if ( hasCRItem )
//            {
//                CRAKpdfNm = cimDirectory + "compdisp_CRAKPDF.pdf";
//                CRIBpdfNm = cimDirectory + "compdisp_CRIBPDF.pdf";
//            }
//
//            Runtime rt = Runtime.getRuntime();
//            GrndsTrace.msg(SUB_TRACE, 7, "got runtime");
//
//            File compDisp = new File(xmlNm);
//            XMLOutputter xmlOut = new XMLOutputter();
//            FileOutputStream fileOut = new FileOutputStream( compDisp );
//            org.jdom.output.XMLOutputter xmlOutputter = new org.jdom.output.XMLOutputter();
//            xmlOut.output(inDoc, fileOut);
//            GrndsTrace.msg(SUB_TRACE, 7, "created xml file");
//
//            if ( cimDirectory.endsWith("/") ) {
//                cimDirectory = cimDirectory.substring(0, cimDirectory.length() - 1);
//            }
//            Process p1 = rt.exec( genSwf + " " + cimDirectory );
//            this.outputProcess( p1 );
//            Process p2;
//            if ( hasCRItem )
//                p2 = rt.exec( genPdf + " " + "2" + " " + cimDirectory );
//            else
//                p2 = rt.exec( genPdf + " " + "1" + " " + cimDirectory );
//            this.outputProcess( p2 );
//            int p1Result = p1.waitFor();
//            int p2Result = p2.waitFor();
//            GrndsTrace.msg( SUB_TRACE, 7, "genSwf return: " + String.valueOf( p1Result ) );
//            GrndsTrace.msg( SUB_TRACE, 7, "genPdf return: " + String.valueOf( p2Result ) );
//            if( p1Result == 0 && p2Result == 0 )
//            {
//                if( this.insertClob( xmlNm, itemSetId, userIDint )
//                && this.insertBlobs(ibSwfNm, akSwfNm, pdfNm, AKpdfNm, CRAKpdfNm, CRIBpdfNm, itemSetId, userIDint ))
//                {
//                    GrndsTrace.msg(SUB_TRACE, 7, "finished loading media");
//                    returnBoolean = true;
//                }
//            }
//        }
//        catch( Exception e ) {
//            GrndsTrace.msg( SUB_TRACE, 7, "Failed to Publish Testlet: " + e.getMessage());
//
//        }
//        finally {
//            GrndsTrace.exitScope();
//            return returnBoolean;
//        }
//    }



}
