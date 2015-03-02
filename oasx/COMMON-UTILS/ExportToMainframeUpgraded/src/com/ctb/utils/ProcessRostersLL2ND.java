package com.ctb.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Logger;

import com.ctb.CreateFiles2ndEdition;
import com.ctb.dto.OrderFile;
import com.ctb.dto.Student;
import com.ctb.dto.TestRoster;
import com.ctb.dto.TfilLL2ND;
/**
 * Thread for processing and creating students data for each student
 * @author TCS
 *
 */
public class ProcessRostersLL2ND implements Runnable{

	static Logger logger = Logger.getLogger(ProcessRostersLL2ND.class.getName());
	
	private TestRoster roster;
	private String customerModelLevelValue;
	private String customerState;
	private String customerCity;
	private Map<Integer, String> customerDemographic;
	private OrderFile orderFile;
	private static List<TfilLL2ND> tfilList = new ArrayList<TfilLL2ND>();
	CreateFiles2ndEdition cFile = new CreateFiles2ndEdition();
	
	
	public ProcessRostersLL2ND(TestRoster roster,
			String customerModelLevelValue, String customerState,
			String customerCity, Map<Integer, String> customerDemographic,
			OrderFile orderFile, CreateFiles2ndEdition cFile) {
		super();
		this.roster = roster;
		this.customerModelLevelValue = customerModelLevelValue;
		this.customerState = customerState;
		this.customerCity = customerCity;
		this.customerDemographic = customerDemographic;
		this.orderFile = orderFile;
		this.cFile = cFile;
	}


	@Override
	public void run(){
		logger.info("Processing roster :: " + roster.getTestRosterId());
		Map<String, Object[]> invalidSubtestMap = new TreeMap<String, Object[]>();
		List<String> invalidSubtestList = new ArrayList<String>();
		TfilLL2ND tfil = new TfilLL2ND();
		String isClassNodeRequiredDummyName = "";
		isClassNodeRequiredDummyName = Configuration.getIsClassNodeRequiredDummyName();
		
		/**
		 * Checking for customer model level value
		 */
		if (null != isClassNodeRequiredDummyName
				&& !"".equalsIgnoreCase(isClassNodeRequiredDummyName)
				&& Integer.valueOf(this.customerModelLevelValue).intValue() < 7) {
			int value = Integer.valueOf(this.customerModelLevelValue)
					.intValue();
			value++;
			tfil.setModelLevel(new Integer(value).toString());
		} else {
			tfil.setModelLevel(this.customerModelLevelValue);
		}
		
		tfil.setState(this.customerState);
		tfil.setCity(this.customerCity);
		
		//tfil.setTestRosterId(String.valueOf(roster.getTestRosterId())); //Needed this field only for developing purpose
		
		try {
			//Student Information
			Student st = roster.getStudent();
			cFile.setStudentList(tfil, st, roster.getTestRosterId());
			
			// Organization
			cFile.createOrganization(tfil, roster, orderFile);
			
			// Test Session Details
			cFile.createTestSessionDetails(tfil, roster, orderFile);
			
			//Accommodation
			cFile.createAccomodations(st.getStudentDemographic(), customerDemographic, tfil , roster);
			
			//Invalidation Status
			cFile.createStudentItemStatusDetails(tfil, roster, invalidSubtestMap);
			
			//Skill Area Information : Scale Score, Proficiency Level, NCE, PR, Point Obtained, Percent Obtained, Lexile
			cFile.createSkillAreaScoreInformation(tfil, roster, invalidSubtestMap, invalidSubtestList);
			
			//Sub Skill Area Information 
			cFile.createSubSkillAreaScoreInformation(tfil, roster, invalidSubtestList);
			
			//Item response for a roster
			cFile.createItemResponseInformation(roster, tfil);
			
			//Sub test Indicator (Commented as Not Applicable for mainframe)
			//createSubtestIndicatorFlag(oascon, roster.getTestRosterId(), tfil);

			addToArrayList(tfil);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	/**
	 * populate a collection of flat file object
	 * @param tfil
	 */
	private static synchronized void addToArrayList(TfilLL2ND tfil) {
		ProcessRostersLL2ND.tfilList.add(tfil);
	}

	/**
	 * get populated collection object of flat file 
	 * @return List<TfilLL2ND>
	 */
	public static List<TfilLL2ND> getFinalList(){
		return ProcessRostersLL2ND.tfilList;
	}
}
