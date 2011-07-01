package com.ctb.lexington.domain.score.controller;

import com.ctb.lexington.db.data.AdminData;
import com.ctb.lexington.db.data.ContextData;
import com.ctb.lexington.db.data.CurriculumData;
import com.ctb.lexington.db.data.CurriculumData.Composite;
import com.ctb.lexington.db.data.CurriculumData.ContentArea;
import com.ctb.lexington.db.data.CurriculumData.Item;
import com.ctb.lexington.db.data.CurriculumData.PrimaryObjective;
import com.ctb.lexington.db.data.CurriculumData.SecondaryObjective;
import com.ctb.lexington.db.irsdata.IrsCompositeDimData;
import com.ctb.lexington.db.irsdata.IrsContentAreaDimData;
import com.ctb.lexington.db.irsdata.IrsItemDimData;
import com.ctb.lexington.db.irsdata.IrsPrimObjDimData;
import com.ctb.lexington.db.irsdata.IrsSecObjDimData;
import com.ctb.lexington.db.irsdata.IrsSubjectDimData;
import com.ctb.lexington.db.mapper.IrsCompositeDimMapper;
import com.ctb.lexington.db.mapper.IrsContentAreaDimMapper;
import com.ctb.lexington.db.mapper.IrsItemDimMapper;
import com.ctb.lexington.db.mapper.IrsPrimObjDimMapper;
import com.ctb.lexington.db.mapper.IrsSecObjDimMapper;
import com.ctb.lexington.db.mapper.IrsSubjectDimMapper;
import com.ctb.lexington.exception.DataException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class CurriculumController {
    private CurriculumData data;
    private AdminData adminData;
    private ContextData context;
    private IrsSubjectDimMapper subMapper;
    private IrsCompositeDimMapper compMapper;
    private IrsContentAreaDimMapper caMapper;
    private IrsPrimObjDimMapper poMapper;
    private IrsSecObjDimMapper soMapper;
    private IrsItemDimMapper iMapper;

    public CurriculumController(Connection conn, CurriculumData data, AdminData adminData, ContextData context) {
        this.data = data;
        this.adminData = adminData;
        this.context = context;
        this.compMapper = new IrsCompositeDimMapper(conn);
        this.caMapper = new IrsContentAreaDimMapper(conn);
        this.poMapper = new IrsPrimObjDimMapper(conn);
        this.soMapper = new IrsSecObjDimMapper(conn);
        this.iMapper = new IrsItemDimMapper(conn);
        this.subMapper = new IrsSubjectDimMapper(conn);
    }

    public void run() throws SQLException, IOException, DataException {
        // handle composites
    	IrsCompositeDimData [] composites = getIrsCompositeBeans(data);
        for(int i=0;i<composites.length;i++) {
            IrsCompositeDimData newComposite = composites[i];
            // insert subject if necessary
            IrsSubjectDimData subject = subMapper.findBySubjectName(data.getComposites()[i].getSubject());
            if(subject == null) {
                subject = new IrsSubjectDimData();
                subject.setSubjectName(data.getComposites()[i].getSubject());
                subject = subMapper.insert(subject);
            }
            newComposite.setSubjectid(subject.getSubjectid());
            // check for existing composite record
            IrsCompositeDimData composite = compMapper.findByCompositeDimId(newComposite.getCompositeid());
            if(composite == null) {
                // insert new composite record
                compMapper.insert(newComposite);
            } else {
                if(!newComposite.equals(composite)) {
                    // update existing composite record
                    compMapper.update(newComposite);
                }
            }
        }
        
        // handle content areas
        IrsContentAreaDimData [] contentAreas = getIrsContentAreaBeans(data);
        for(int i=0;i<contentAreas.length;i++) {
            IrsContentAreaDimData newContentArea = contentAreas[i];
            // insert subject if necessary
            IrsSubjectDimData subject = subMapper.findBySubjectName(data.getContentAreas()[i].getSubject());
            if(subject == null) {
                subject = new IrsSubjectDimData();
                subject.setSubjectName(data.getContentAreas()[i].getSubject());
                subject = subMapper.insert(subject);
            }
            newContentArea.setSubjectid(subject.getSubjectid());
            // check for existing content ares record
            IrsContentAreaDimData contentArea = caMapper.findByContentAreaId(newContentArea.getContentAreaid());
            if(contentArea == null) {
                // insert new content area record
                caMapper.insert(newContentArea);
            } else {
                if(!newContentArea.equals(contentArea)) {
                    // update existing content area record
                    caMapper.update(newContentArea);
                }
            }
        }
        
        // handle primary objectives
        IrsPrimObjDimData [] primaryObjectives = getIrsPrimObjBeans(data);
        for(int i=0;i<primaryObjectives.length;i++) {
            IrsPrimObjDimData newPrimObj = primaryObjectives[i];
            // check for existing primary objective record
            IrsPrimObjDimData primObj = poMapper.findByPrimObjId(newPrimObj.getPrimObjid());
            if(primObj == null) {
                // insert new primary objective record
                poMapper.insert(newPrimObj);
            } else {
                if(!newPrimObj.equals(primObj)) {
                    // update existing primary objective record
                    poMapper.update(newPrimObj);
                }
            }
        }
        
        // handle secondary objectives
        IrsSecObjDimData [] secondaryObjectives = getIrsSecObjBeans(data);
        for(int i=0;i<secondaryObjectives.length;i++) {
            IrsSecObjDimData newSecObj = secondaryObjectives[i];
            // check for existing secondary objective record
            IrsSecObjDimData secObj = soMapper.findBySecObjId(newSecObj.getSecObjid());
            if(secObj == null) {
                // insert new secondary objective record
                soMapper.insert(newSecObj);
            } else {
                if(!newSecObj.equals(secObj)) {
                    // update existing secondary objective record
                    soMapper.update(newSecObj);
                }
            }
        }
        
        // handle items
        IrsItemDimData [] items = getIrsItemBeans(data);
        for(int i=0;i<items.length;i++) {
            IrsItemDimData newItem = items[i];
            // check for existing item record
            IrsItemDimData item = iMapper.findByOASItemIdAndSecObjId(newItem.getOasItemid(), newItem.getSecObjid());
            if(item == null) {
                // insert new item record
                 // For Laslink Scoring
            	if(newItem.getItemType().equals("CR"))
            		newItem.setCorrectResponse("N/A");
                item = iMapper.insert(newItem);
            } else {
                if(!newItem.equals(item)) {
                	if(newItem.getItemType().equals("CR"))
                		newItem.setCorrectResponse("N/A");
                    // update existing item record
                    newItem.setItemid(item.getItemid());
                    iMapper.update(newItem);
                }
            }
            newItem.setItemid(item.getItemid());
        }
        copySubjectIdsToCurrData(composites, contentAreas, data);
        copyItemIdsToCurrData(items, data);
    }
    
    private IrsCompositeDimData [] getIrsCompositeBeans(CurriculumData data) {
        IrsCompositeDimData [] cdd = new IrsCompositeDimData[data.getComposites().length];
        for(int i=0;i<data.getComposites().length;i++) {
            cdd[i] = new IrsCompositeDimData();
            Composite co = data.getComposites()[i];
            cdd[i].setCompositeid(co.getCompositeId());
            cdd[i].setCompositeType(co.getCompositeType());
            cdd[i].setName(co.getCompositeName());
            if("Total".equals(co.getCompositeName())) {
                cdd[i].setName("Total Score");
            }
            cdd[i].setNumItems(co.getCompositeNumItems());
            cdd[i].setPointsPossible(co.getCompositePointsPossible());
            cdd[i].setAssessmentid(context.getAssessmentId());
        }
        
        return cdd;
    }
    
    private IrsContentAreaDimData [] getIrsContentAreaBeans(CurriculumData data) {
        IrsContentAreaDimData [] cad = new IrsContentAreaDimData[data.getContentAreas().length];
        for(int i=0;i<data.getContentAreas().length;i++) {
            cad[i] = new IrsContentAreaDimData();
            ContentArea ca = data.getContentAreas()[i];
            cad[i].setContentAreaid(ca.getContentAreaId());
            cad[i].setContentAreaType(ca.getContentAreaType());
            cad[i].setName(ca.getContentAreaName());
            cad[i].setNumItems(ca.getContentAreaNumItems());
            cad[i].setPointsPossible(ca.getContentAreaPointsPossible());
            cad[i].setAssessmentid(context.getAssessmentId());
            cad[i].setContentAreaIndex(new Long("Reading".equals(ca.getContentAreaName())?1:
                                                "Language".equals(ca.getContentAreaName())?2:
                                                "Mathematics".equals(ca.getContentAreaName())?3:4));
        }
        
        return cad;
    }
    
    private IrsPrimObjDimData [] getIrsPrimObjBeans(CurriculumData data) {
        IrsPrimObjDimData [] pod = new IrsPrimObjDimData[data.getPrimaryObjectives().length];
        for(int i=0;i<data.getPrimaryObjectives().length;i++) {
            pod[i] = new IrsPrimObjDimData();
            PrimaryObjective po = data.getPrimaryObjectives()[i];
            pod[i].setContentAreaid(po.getContentAreaId());
            pod[i].setName(po.getPrimaryObjectiveName());
            pod[i].setNumItems(po.getPrimaryObjectiveNumItems());
            pod[i].setPointsPossible(po.getPrimaryObjectivePointsPossible());
            pod[i].setPrimObjid(new Long(Long.parseLong(String.valueOf(po.getProductId()) + String.valueOf(po.getPrimaryObjectiveId()))));
            pod[i].setPrimObjType(po.getPrimaryObjectiveType());
            pod[i].setPrimObjIndex(po.getPrimaryObjectiveIndex());
            pod[i].setAssessmentid(context.getAssessmentId());
        }
        
        return pod;
    }
    
    private IrsSecObjDimData [] getIrsSecObjBeans(CurriculumData data) {
        IrsSecObjDimData [] sod = new IrsSecObjDimData[data.getSecondaryObjectives().length];
        for(int i=0;i<data.getSecondaryObjectives().length;i++) {
            sod[i] = new IrsSecObjDimData();
            SecondaryObjective so = data.getSecondaryObjectives()[i];
            sod[i].setPrimObjid(new Long(Long.parseLong(String.valueOf(so.getProductId()) + String.valueOf(so.getPrimaryObjectiveId()))));
            sod[i].setName(so.getSecondaryObjectiveName());
            sod[i].setNumItems(so.getSecondaryObjectiveNumItems());
            sod[i].setPointsPossible(so.getSecondaryObjectivePointsPossible());
            sod[i].setSecObjid(new Long(Long.parseLong(String.valueOf(so.getProductId()) + String.valueOf(so.getSecondaryObjectiveId()))));
            sod[i].setSecObjType(so.getSecondaryObjectiveType());
            sod[i].setAssessmentid(context.getAssessmentId());
        }
        
        return sod;
    }
    
    private IrsItemDimData [] getIrsItemBeans(CurriculumData data) {
        IrsItemDimData [] idd = new IrsItemDimData[data.getItems().length];
        for(int i =0;i<data.getItems().length;i++) {
            idd[i] = new IrsItemDimData();
            Item item = data.getItems()[i];
            idd[i].setCorrectResponse(item.getItemCorrectResponse());
            idd[i].setItemIndex(item.getItemIndex());
            idd[i].setItemText(item.getItemText());
            idd[i].setItemType(item.getItemType());
            idd[i].setOasItemid(item.getOasItemId());
            idd[i].setPointsPossible(item.getItemPointsPossible());
            SecondaryObjective so = data.getSecObjById(item.getSecondaryObjectiveId());
            idd[i].setSecObjid(new Long(Long.parseLong(String.valueOf(so.getProductId()) + String.valueOf(item.getSecondaryObjectiveId()))));
            idd[i].setAssessmentid(context.getAssessmentId());
        }
        
        return idd;
    }
    
    private void copyItemIdsToCurrData(IrsItemDimData [] irsItems, CurriculumData data) {
        for(int i =0;i<irsItems.length;i++) {
            Item item = data.getItems()[i];
            item.setItemId(irsItems[i].getItemid());
        }
    }
    
     private void copySubjectIdsToCurrData(IrsCompositeDimData [] irsComposites, IrsContentAreaDimData [] irsContentAreas, CurriculumData data) {
        for(int i =0;i<irsComposites.length;i++) {
            Composite composite = data.getComposites()[i];
            composite.setSubjectId(irsComposites[i].getSubjectid());
        }
        for(int i =0;i<irsContentAreas.length;i++) {
            ContentArea contentArea = data.getContentAreas()[i];
            contentArea.setSubjectId(irsContentAreas[i].getSubjectid());
        }
    }
}