package com.ctb.contentBridge.core.publish.iknowxml;


import org.jdom.output.*;


/**
 * Created by IntelliJ IDEA.
 * User: mwshort
 * Date: Sep 8, 2003
 * Time: 4:47:20 PM
 * To change this template use Options | File Templates.
 */
public class R2XmlOutputter extends XMLOutputter {

    public R2XmlOutputter() {
        super();
        // Sudha this.setLineSeparator("");
//      Sudhathis.setNewlines(false);
//      Sudhathis.setOmitDeclaration(true);
//      Sudhathis.setEncoding("UTF-8");
//      Sudhathis.setTrimAllWhite(true);
    }

    public String escapeElementEntities(String text) {
        if (text == null) {
            return null;
        }
        return R2Entities.HTML40.escape(text);
    }

}

