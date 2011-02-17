/*
 * Created on Feb 3, 2004
 *
 */
package com.ctb.commands;

import org.apache.log4j.Logger;

import com.ctb.common.tools.OCSConfig;
import com.ctb.common.tools.SystemException;
import com.ctb.util.Email;

public class EmailGateway {
    protected static Logger logger = Logger.getLogger(EmailGateway.class);

    private final OCSConfig ocsConfig;
    public static final String SUCCESS_SUBJECT = " successful";
    public static final String FAILURE_SUBJECT = " failed";
    public static final String USAGE_ERROR_SUBJECT = "USAGE ERROR";

    public EmailGateway(OCSConfig ocsConfig) {
        this.ocsConfig = ocsConfig;
    }

    public void sendEmail(String subject, String msgText) {
        for (int i = 0; i < ocsConfig.getEmailTo().length; i++) {
            try {
                Email.sendMessage(
                    ocsConfig.getSmtpHost(),
                    ocsConfig.getEmailFrom(),
                    ocsConfig.getEmailTo()[i],
                    subject,
                    msgText);
            } catch (Exception e) {
                throw new SystemException(
                    "Unable to send e-mail."
                        + "\nSUBJECT: "
                        + subject
                        + "\n     TO: "
                        + ocsConfig.getEmailTo()[i]
                        + "\n   FROM: "
                        + ocsConfig.getEmailFrom()
                        + "\n   BODY: "
                        + msgText,
                    e);
            }
        }
    }

}
