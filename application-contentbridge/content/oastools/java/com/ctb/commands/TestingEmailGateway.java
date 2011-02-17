/*
 * Created on Feb 3, 2004
 *
 */
package com.ctb.commands;

import org.apache.log4j.Logger;

import com.ctb.common.tools.OCSConfig;
import com.ctb.common.tools.SystemException;


public class TestingEmailGateway extends EmailGateway {
    protected static Logger logger = Logger.getLogger(TestingEmailGateway.class);

    public TestingEmailGateway(OCSConfig ocsConfig) {
        super(ocsConfig);
    }

    public void sendEmail(String subject, String msgText) {

        if (subject.indexOf(EmailGateway.FAILURE_SUBJECT) != -1)
            throw new SystemException("TeamSiteAdapterTest failed - please check ctb_deploy.trace");
    }

}
