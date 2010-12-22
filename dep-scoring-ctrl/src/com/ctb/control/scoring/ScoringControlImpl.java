package com.ctb.control.scoring; 

import com.bea.control.*;
import com.ctb.lexington.domain.score.scorer.ScorerFactory;
import java.io.Serializable;
import org.apache.beehive.controls.api.bean.ControlImplementation;

/**
 * @editor-info:code-gen control-interface="true"
 */
@ControlImplementation(isTransient=true)
public class ScoringControlImpl implements ScoringControl
{ 
    /**
     * @common:control
     */
    @org.apache.beehive.controls.api.bean.Control()
    private com.ctb.control.db.OASDB oasdb;

    static final long serialVersionUID = 1L;

    /**
     * @common:operation
     */
    public String invokeScoring(String testRosterId)
    {
        try {
            ScorerFactory.invokeScoring(new Integer(Integer.parseInt(testRosterId)), false, true, false);
            return("success");
        } catch (Exception e) {
            e.printStackTrace();
            return("error: " + e.getMessage());
        }
    }
    
    /**
     * @common:operation
     */
    public String scoreCompletedRostersForAdmin(Integer testAdminId)
    {
        try {
            int [] rosters = oasdb.getCompletedRostersForAdmin(testAdminId.intValue());
            for(int i=0;i<rosters.length;i++) {
                ScorerFactory.invokeScoring(new Integer(rosters[i]), false, true, false);
            }
            return("success");
        } catch (Exception e) {
            e.printStackTrace();
            return("error: " + e.getMessage());
        }
    }
    
    /**
     * @common:operation
     */
    public String scoreCompletedRostersForProduct(Integer productId)
    {
        try {
            int [] rosters = oasdb.getCompletedRostersForProduct(productId.intValue());
            for(int i=0;i<rosters.length;i++) {
                try {
                    ScorerFactory.invokeScoring(new Integer(rosters[i]), false, true, false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return("success");
        } catch (Exception e) {
            e.printStackTrace();
            return("error: " + e.getMessage());
        }
    }
    
    /**
     * @common:operation
     */
    public String scoreCompletedRostersForCustomer(Integer customerId)
    {
        try {
            int [] rosters = oasdb.getCompletedRostersForCustomer(customerId.intValue());
            for(int i=0;i<rosters.length;i++) {
                try {
                    ScorerFactory.invokeScoring(new Integer(rosters[i]), false, true, false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return("success");
        } catch (Exception e) {
            e.printStackTrace();
            return("error: " + e.getMessage());
        }
    }
} 
