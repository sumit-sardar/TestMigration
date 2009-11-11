package com.ctb.lexington.domain.score.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.ctb.lexington.db.ConnectionFactory;
import com.ctb.lexington.db.data.AdminData;
import com.ctb.lexington.db.data.OrgNodeData;
import com.ctb.lexington.db.data.OrgNodeDetails;
import com.ctb.lexington.db.irsdata.IrsCustomerDimData;
import com.ctb.lexington.db.irsdata.IrsOrgNodeDimData;
import com.ctb.lexington.db.mapper.IrsCustomerDimMapper;
import com.ctb.lexington.db.mapper.IrsOrgNodeDimMapper;
import com.ctb.lexington.exception.DataException;
import com.ctb.lexington.util.SQLUtil;
import com.ctb.lexington.util.SafeHashMap;
import java.util.List;

public class OrgNodeController{
    private Connection conn;
	private OrgNodeData data;
    private AdminData adminData;
    private IrsCustomerDimMapper custMapper;
	private IrsOrgNodeDimMapper orgNodeMapper;

    public OrgNodeController(Connection conn, OrgNodeData data, AdminData adminData) {
        this.conn = conn;
        this.data = data;
        this.adminData = adminData;
    }

    public void run() throws SQLException, DataException{
        custMapper = new IrsCustomerDimMapper(conn);
        orgNodeMapper = new IrsOrgNodeDimMapper(conn);
        // create IRS beans from input data
        IrsCustomerDimData newCustomer = getIrsCustomerBean(data);
        IrsOrgNodeDimData newOrg = getIrsOrgNodeBean(data);
        // check if customer exists in IRS
        IrsCustomerDimData customer = custMapper.findByCustomerId(newCustomer.getCustomerid());
        if(customer == null) {
            // insert new customer
            custMapper.insert(newCustomer);
            // insert new org structure
            orgNodeMapper.insert(newOrg);
            IrsOrgNodeDimData org =(IrsOrgNodeDimData) orgNodeMapper.findByOrgNodeIds(newOrg).get(0);
            data.setOrgNodeId(org.getOrgNodeid());
        } else {
            // customer exists, check if needs update
            if(!customer.equals(newCustomer)) {
                // update customer
                custMapper.update(newCustomer);
            }
            // check if org exists in IRS
            List existingOrgs = orgNodeMapper.findByOrgNodeIds(newOrg);
            IrsOrgNodeDimData org = existingOrgs.size()>0?(IrsOrgNodeDimData)existingOrgs.get(0):null;
            boolean orgChanged = false;
            if (org == null) {
                // insert new org structure
                try {
                    orgNodeMapper.insert(newOrg);
                } catch (SQLException sqe) {
                    // contentious org inserts are common, try to handle here
                    existingOrgs = orgNodeMapper.findByOrgNodeIds(newOrg);
                    org = existingOrgs.size()>0?(IrsOrgNodeDimData)existingOrgs.get(0):null;
                    if(org == null) {
                        throw sqe;
                    } else if(!org.equals(newOrg)){
                        newOrg.setOrgNodeid(org.getOrgNodeid());
                        orgNodeMapper.update(newOrg);
                    }
                }
                org = (IrsOrgNodeDimData) orgNodeMapper.findByOrgNodeIds(newOrg).get(0);
                newOrg.setOrgNodeid(org.getOrgNodeid());
                orgChanged = true;
            } else {
                // org exists, check if needs update
                if(!org.equals(newOrg)) {
                    // update org
                    newOrg.setOrgNodeid(org.getOrgNodeid());
                    orgNodeMapper.update(newOrg);
                    orgChanged = true;
                }
            }
            if(orgChanged) {
                orgNodeMapper.updateOldOrgsLevelOne(newOrg);
                orgNodeMapper.updateOldOrgsLevelTwo(newOrg);
                orgNodeMapper.updateOldOrgsLevelThree(newOrg);
                orgNodeMapper.updateOldOrgsLevelFour(newOrg);
                orgNodeMapper.updateOldOrgsLevelFive(newOrg);
                orgNodeMapper.updateOldOrgsLevelSix(newOrg);
                orgNodeMapper.updateOldOrgsLevelSeven(newOrg);
            }
            data.setOrgNodeId(org.getOrgNodeid());
        }
        
    }
    
    private IrsOrgNodeDimData getIrsOrgNodeBean(OrgNodeData data) {
        IrsOrgNodeDimData org = new IrsOrgNodeDimData();
        
        org.setCustomerid(data.getNodes()[0].getCustomerId());
        org.setNumLevels(new Long((long) data.getNodes().length));
        
        if(data.getNodes().length >= 1) {
            org.setLevel1Id(data.getNodes()[0].getOrgNodeId());
            org.setLevel1Name(data.getNodes()[0].getOrgNodeName());
            org.setLevel1Type(data.getNodes()[0].getType());
        }
        if(data.getNodes().length >= 2) {
            org.setLevel2Id(data.getNodes()[1].getOrgNodeId());
            org.setLevel2Name(data.getNodes()[1].getOrgNodeName());
            org.setLevel2Type(data.getNodes()[1].getType());
        }
        if(data.getNodes().length >= 3) {
            org.setLevel3Id(data.getNodes()[2].getOrgNodeId());
            org.setLevel3Name(data.getNodes()[2].getOrgNodeName());
            org.setLevel3Type(data.getNodes()[2].getType());
        }
        if(data.getNodes().length >= 4) {
            org.setLevel4Id(data.getNodes()[3].getOrgNodeId());
            org.setLevel4Name(data.getNodes()[3].getOrgNodeName());
            org.setLevel4Type(data.getNodes()[3].getType());
        }
        if(data.getNodes().length >= 5) {
            org.setLevel5Id(data.getNodes()[4].getOrgNodeId());
            org.setLevel5Name(data.getNodes()[4].getOrgNodeName());
            org.setLevel5Type(data.getNodes()[4].getType());
        }
        if(data.getNodes().length >= 6) {
            org.setLevel6Id(data.getNodes()[5].getOrgNodeId());
            org.setLevel6Name(data.getNodes()[5].getOrgNodeName());
            org.setLevel6Type(data.getNodes()[5].getType());
        }
        if(data.getNodes().length >= 7) {
            org.setLevel7Id(data.getNodes()[6].getOrgNodeId());
            org.setLevel7Name(data.getNodes()[6].getOrgNodeName());
            org.setLevel7Type(data.getNodes()[6].getType());
        }
        
        return org;
    }
    
    private IrsCustomerDimData getIrsCustomerBean(OrgNodeData data) {
        IrsCustomerDimData customer = new IrsCustomerDimData();
        
        customer.setCustomerid(data.getNodes()[0].getCustomerId());
        customer.setName(data.getNodes()[0].getCustomerName());
        customer.setKey(adminData.getCustomerKey());
        
        return customer;
    }
}