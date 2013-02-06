package com.ctb.contentBridge.core.publish.xml.subtest;

import java.util.List;

import com.ctb.contentBridge.core.exception.BusinessException;

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
            throw new BusinessException(
                "Invalid Score Type Code: [" + scoreTypeCode + "]");
        }
    }
}
