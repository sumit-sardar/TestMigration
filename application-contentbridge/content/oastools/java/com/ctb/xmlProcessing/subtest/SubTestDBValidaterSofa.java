package com.ctb.xmlProcessing.subtest;

import java.util.List;

import com.ctb.common.tools.SystemException;

import net.sf.hibernate.Session;

/**
 * @author wmli
 */
public class SubTestDBValidaterSofa implements SubTestDBValidater{
    DBSubTestGateway gateway;

    public SubTestDBValidaterSofa(Session session) {
        this.gateway = new DBSubTestGateway(session);
    }

    public void validate(SubTestHolder holder) {
        validateScoreTypeCode(holder.getScoreTypeCode());
    }

    private void validateScoreTypeCode(String scoreTypeCode) {
        List scoreTypeCodes = gateway.getScoreTypeCodes();

        if (!scoreTypeCodes.contains(scoreTypeCode)) {
            throw new SystemException(
                "Invalid Score Type Code: [" + scoreTypeCode + "]");
        }
    }
}
