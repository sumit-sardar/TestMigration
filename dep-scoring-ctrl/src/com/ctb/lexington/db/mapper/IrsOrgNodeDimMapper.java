package com.ctb.lexington.db.mapper; 

import java.sql.Connection;
import java.sql.SQLException;

import com.ctb.lexington.db.irsdata.IrsOrgNodeDimData;
import java.util.List;

/**
 * @author Rama_Rao
 *
 */

public class IrsOrgNodeDimMapper extends AbstractDBMapper{
	
	private static final String FIND_BY_ORGNODEIDS = "findByOrgNodeIds";
	private static final String INSERT_INTO_ORGNODEDIM = "insertOrgNodeDim";
	private static final String UPDATE_BY_ORGNODEID = "updateOrgNodeDim";
    private static final String DELETE_BY_ORGNODEID = "deleteOrgNodeDim";
    
    private static final String UPDATE_OLD_ORGS_ONE = "updateOldOrgsLevelOne";
    private static final String UPDATE_OLD_ORGS_TWO = "updateOldOrgsLevelTwo";
    private static final String UPDATE_OLD_ORGS_THREE = "updateOldOrgsLevelThree";
    private static final String UPDATE_OLD_ORGS_FOUR = "updateOldOrgsLevelFour";
    private static final String UPDATE_OLD_ORGS_FIVE = "updateOldOrgsLevelFive";
    private static final String UPDATE_OLD_ORGS_SIX = "updateOldOrgsLevelSix";
    private static final String UPDATE_OLD_ORGS_SEVEN = "updateOldOrgsLevelSeven";
  
	public IrsOrgNodeDimMapper(Connection conn){
		super(conn);
    }
	
	public List findByOrgNodeIds(IrsOrgNodeDimData record){
		return (List) findMany(FIND_BY_ORGNODEIDS,record);    
    }
	
	public void insert(IrsOrgNodeDimData record)throws SQLException {
		insert(INSERT_INTO_ORGNODEDIM, record);
    }
	
	public void update(IrsOrgNodeDimData record)throws SQLException{
        update(UPDATE_BY_ORGNODEID, record);
    }
	
	public void delete(Long orgNodeId)throws SQLException{
		delete(DELETE_BY_ORGNODEID, orgNodeId);
	}
    
    public void updateOldOrgsLevelOne(IrsOrgNodeDimData record)throws SQLException{
        update(UPDATE_OLD_ORGS_ONE, record);
    }
    
    public void updateOldOrgsLevelTwo(IrsOrgNodeDimData record)throws SQLException{
        update(UPDATE_OLD_ORGS_TWO, record);
    }
    
    public void updateOldOrgsLevelThree(IrsOrgNodeDimData record)throws SQLException{
        update(UPDATE_OLD_ORGS_THREE, record);
    }
    
    public void updateOldOrgsLevelFour(IrsOrgNodeDimData record)throws SQLException{
        update(UPDATE_OLD_ORGS_FOUR, record);
    }
    
    public void updateOldOrgsLevelFive(IrsOrgNodeDimData record)throws SQLException{
        update(UPDATE_OLD_ORGS_FIVE, record);
    }
    
    public void updateOldOrgsLevelSix(IrsOrgNodeDimData record)throws SQLException{
        update(UPDATE_OLD_ORGS_SIX, record);
    }
    
    public void updateOldOrgsLevelSeven(IrsOrgNodeDimData record)throws SQLException{
        update(UPDATE_OLD_ORGS_SEVEN, record);
    }
}